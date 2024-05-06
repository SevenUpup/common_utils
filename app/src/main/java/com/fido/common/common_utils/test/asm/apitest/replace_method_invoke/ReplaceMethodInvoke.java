package com.fido.common.common_utils.test.asm.apitest.replace_method_invoke;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
public class ReplaceMethodInvoke {

    public static void main(String[] args) {
        //直接运行会 throw NPE
        new Toast().show();
    }

}
