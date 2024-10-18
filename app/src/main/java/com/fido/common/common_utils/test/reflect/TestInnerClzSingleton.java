package com.fido.common.common_utils.test.reflect;

/**
 * @author: FiDo
 * @date: 2024/10/16
 * @des:
 */
public class TestInnerClzSingleton {

    private TestInnerClzSingleton() {
    }

    public static TestInnerClzSingleton newInstance(){
        return Instance.holder;
    }

    private static class Instance{
        public static TestInnerClzSingleton holder = new TestInnerClzSingleton();

        public void innerTestFun(){
            System.out.println("调用了TestInnerClzSingleton.Instance的innerTestFun");
        }
    }

}
