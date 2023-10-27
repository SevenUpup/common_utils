package com.fido.common.common_utils.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FiDo
 * @description:
 * @date :2023/10/26 11:25
 */
public class HEventBus {

    private Handler mHandler;
    private ExecutorService mExecutorService;
    private Map<Object, List<SubscribeMethod>> mSubscribeMethodMap;

    private HEventBus() {
        mHandler = new Handler(Looper.myLooper());
        mSubscribeMethodMap = new HashMap<>();
        mExecutorService = Executors.newCachedThreadPool();

    }

    public static HEventBus getDefault() {
        return Holder.INSTANCE;
    }

    public void register(Object obj) {
        List<SubscribeMethod> subscribeMethods = mSubscribeMethodMap.get(obj);
        if (subscribeMethods == null) {
            subscribeMethods = getSubscribeMethods(obj);
            mSubscribeMethodMap.put(obj, subscribeMethods);
        }
    }

    private List<SubscribeMethod> getSubscribeMethods(Object obj) {
        List<SubscribeMethod> list = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            //如果类全名以这些字符开头，则认为是jdk的，不是我们自定义的，自然没必要去拿注解
            if (clazz.getName().startsWith("java.")
                    || clazz.getName().startsWith("javax")
                    || clazz.getName().startsWith("android.")
                    || clazz.getName().startsWith("androidx.")) {
                break;
            }
            try {
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    Subscribe subscribe = method.getAnnotation(Subscribe.class);
                    if (subscribe == null) {
                        continue;
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new RuntimeException("HEventBus 只能接收一个参数");
                    }
                    ThreadMode threadMode = subscribe.threadMode();
                    SubscribeMethod subscribeMethod = new SubscribeMethod(method,threadMode,parameterTypes[0]);
                    list.add(subscribeMethod);
                }
                clazz = clazz.getSuperclass();
            }catch (Exception e){
                clazz =  null;
            }

        }
        return list;
    }

    public void unRegister(Object obj) {
        if (mSubscribeMethodMap != null) {
            mSubscribeMethodMap.remove(obj);
        }
    }

    public void post(Object event){
        Iterator<Object> iterator = mSubscribeMethodMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            List<SubscribeMethod> subscribeMethods = mSubscribeMethodMap.get(object);
            for (SubscribeMethod subscribeMethod : subscribeMethods) {
                //有两个Class类型的类象，一个是调用isAssignableFrom方法的类对象（后称对象a），以及方法中作为参数的这个类对象（称之为对象b），这两个对象如果满足以下条件则返回true，否则返回false：
                //    a对象所对应类信息是b对象所对应的类信息的父类或者是父接口，简单理解即a是b的父类或接口
                //    a对象所对应类信息与b对象所对应的类信息相同，简单理解即a和b为同一个类或同一个
                if (subscribeMethod.getEventType().isAssignableFrom(event.getClass())) {
                    switch (subscribeMethod.getThreadMode()) {
                        case POST:
                            invoke(subscribeMethod.getMethod(),object,event);
                            break;
                        case MAIN:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribeMethod.getMethod(), object, event);
                            } else {
                                mHandler.post(() -> invoke(subscribeMethod.getMethod(),object,event));
                            }
                            break;
                        case ASYNC:
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                mExecutorService.execute(()->invoke(subscribeMethod.getMethod(),object,event));
                            } else {
                                invoke(subscribeMethod.getMethod(), object, event);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void invoke(Method method, Object object, Object event) {
        method.setAccessible(true);
        try {
            method.invoke(object,event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Holder {
        private static HEventBus INSTANCE = new HEventBus();
    }

}
