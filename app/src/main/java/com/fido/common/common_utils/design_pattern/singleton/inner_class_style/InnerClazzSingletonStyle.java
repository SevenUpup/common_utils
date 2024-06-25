package com.fido.common.common_utils.design_pattern.singleton.inner_class_style;

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  内部类实现单例：这种方式相比懒汉式和饿汉式的特点是延迟加载，线程安全，同时也减少了内存消耗。
 */
public class InnerClazzSingletonStyle {

    private InnerClazzSingletonStyle() {
    }

    public static InnerClazzSingletonStyle getInstance(){
        return Holder.instance;
    }

    private static class Holder{

        private static InnerClazzSingletonStyle instance = new InnerClazzSingletonStyle();

    }

}
