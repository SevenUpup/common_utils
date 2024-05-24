package com.fido.common.common_utils.test.asm.apitest.catch_method_invoke;

public class CatchMethodInvoke {

    public int calc() {
        return 1 / 0;
    }

    // 以下用例都是可以通过的.

//    public int catchCalc() {
//        try {
//            return 1 / 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    public String throwException() {
//        throw new RuntimeException("");
//    }
//
//    public void show() {
//
//    }

}
