package com.fido.common.common_utils.design_pattern.singleton.hung_style;

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  在程序启动或单例模式类被加载的时候，单件模式实例就已经被创建。并且以后不再改变，其天生是线程安全的。但是缺点是稍微消耗资源。
 */
public class HungStyleSingle {

    private static final HungStyleSingle instance = new HungStyleSingle();

    private HungStyleSingle() {}

    public static HungStyleSingle getInstance(){
        return instance;
    }

}
