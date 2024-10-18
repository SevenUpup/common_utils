package com.fido.common.common_utils.test.reflect.singleton;

/**
 * @author: FiDo
 * @date: 2024/10/16
 * @des:  获取单例的方式：
 *        EnumSingleton singleton = EnumSingleton.INSTANCE;
 */
public enum EnumSingleton {

    //定义1个枚举的元素，即为单例类的1个实例
    INSTANCE;

    // 隐藏了1个空的、私有的 构造方法
    private EnumSingleton() {}

    public void testFun(){
        System.out.println("调用枚举单例中的testFun");
    }
}
