package com.fido.common.common_utils.design_pattern.observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:  定义一个被观察者
 */
public class Observable<T> {

    private List<Observer<T>> mObservers = new ArrayList<>();

    public void register(Observer<T> observer){
        if (observer == null) {
            throw new NullPointerException("observer was null");
        }
        synchronized (this) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    public void unRegister(Observer<T> observer){
        mObservers.remove(observer);
    }

    public void notifyObserver(T data){
        for (Observer<T> mObserver : mObservers) {
            mObserver.onUpdate(data);
        }
    }

}
