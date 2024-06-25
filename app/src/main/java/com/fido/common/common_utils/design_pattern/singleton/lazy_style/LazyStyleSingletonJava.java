package com.fido.common.common_utils.design_pattern.singleton.lazy_style;

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 *
 *  懒汉式相比饿汉式的优点和缺点
 * 优点：延迟加载（需要的时候才去加载）
 * 缺点：线程不安全，在多线程中很容易出现不同步的情况
 */
public class LazyStyleSingletonJava {

    private static LazyStyleSingletonJava instance;

    private LazyStyleSingletonJava() {
    }

    public static LazyStyleSingletonJava getInstance(){
        if (instance == null) {
            instance = new LazyStyleSingletonJava();
        }
        return instance;
    }

}
