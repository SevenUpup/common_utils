package com.fido.common.common_utils.test.reflect;

/**
 * @author: FiDo
 * @date: 2024/10/16
 * @des:
 */
public class TestSingleton {

    // 1. 加载该类时，单例就会自动被创建
    private static  TestSingleton ourInstance  = new  TestSingleton();

    // 2. 构造函数 设置为 私有权限
    // 原因：禁止他人创建实例
    private TestSingleton() {
    }

    // 3. 通过调用静态方法获得创建的单例
    public static  TestSingleton newInstance() {
        return ourInstance;
    }
}

