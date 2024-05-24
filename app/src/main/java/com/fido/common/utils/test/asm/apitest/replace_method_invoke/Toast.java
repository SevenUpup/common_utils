package com.fido.common.common_utils.test.asm.apitest.replace_method_invoke;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
public class Toast {

    private String msg = null;

    public void show(){
        System.out.println("Toast: " + msg + ", msg.length: " + msg.length());
    }

}
