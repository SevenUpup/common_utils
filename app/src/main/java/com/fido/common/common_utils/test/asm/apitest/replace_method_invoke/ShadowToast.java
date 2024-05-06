package com.fido.common.common_utils.test.asm.apitest.replace_method_invoke;

import java.lang.reflect.Field;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
public class ShadowToast {

    public static void show(Toast toast,String clzName){
        wrap(toast,clzName).show();
    }

    private static Toast wrap(Toast toast, String clzName) {
        try {
            Field msg = toast.getClass().getDeclaredField("msg");
            msg.setAccessible(true);
            if (msg.get(toast) == null) {
                msg.set(toast,clzName);
            }

        }catch (Exception e){

        }
        return toast;
    }

}
