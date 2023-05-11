package com.fido.common.common_utils.test;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author FiDo
 * @description:
 * @date :2023/4/13 11:46
 */
public class MyMutableLiveData<T> extends MutableLiveData<T> {

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        try {
            hook(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void observeSticky(@NonNull LifecycleOwner owner,@NonNull Observer<T> observer){
        super.observe(owner,observer);
    }

    /* 水要继改observer.mLastVersion的值那么思考: (向思雄)
        mLastVersion-) observer-) iterator.next().getValue(]-) mObservers
        反射使用的时候正好相反
        mObservers-)医数(terator.next().getValue()) -)observer-) mLastVersion
        潘hook，您observer.mLastVersion  mVersion
     */
    private void hook(Observer<? super T> observer) throws Exception {
        Class<LiveData> liveDataClass = androidx.lifecycle.LiveData.class;
        Field fieldmObservers = liveDataClass.getDeclaredField("mObservers");
        fieldmObservers.setAccessible(true);
        Object mObservers = fieldmObservers.get(this);
        Class<?> mObserversClass = mObservers.getClass();

        Method methodget = mObserversClass.getDeclaredMethod("get", Object.class);
        methodget.setAccessible(true);
        Object entry = methodget.invoke(mObservers, observer);
        Object observerWrapper = ((Map.Entry<Object, Object>) entry).getValue();
        Class<?> mObserver = observerWrapper.getClass().getSuperclass();//observer
        Field mLastVersion = mObserver.getDeclaredField("mLastVersion");
        mLastVersion.setAccessible(true);
        Field mVersion = liveDataClass.getDeclaredField("mVersion");
        mVersion.setAccessible(true);
        Object mVersionObject = mVersion.get(this);
        mLastVersion.set(observerWrapper, mVersionObject);
    }
}
