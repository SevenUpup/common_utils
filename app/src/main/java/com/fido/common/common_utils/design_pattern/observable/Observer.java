package com.fido.common.common_utils.design_pattern.observable;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:  定义一个观察者
 */
public interface Observer<T> {

    void onUpdate(T t);

}
