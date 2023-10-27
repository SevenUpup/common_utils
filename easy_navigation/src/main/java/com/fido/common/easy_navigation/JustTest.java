package com.fido.common.easy_navigation;

/**
 * @author FiDo
 * @description:
 * @date :2023/10/26 16:05
 */
public class JustTest {

    interface Callback{
        void doSomething(String text);
    }

    private void laLala(Callback callback){
        System.out.println("before do somthing");
        callback.doSomething("test text");
        System.out.println("after do somthing");
    }

}
