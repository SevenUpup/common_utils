package com.fido.common.common_utils.test.asm;

/**
 * @author: FiDo
 * @date: 2024/4/9
 * @des:
 */
public class TimeAsmView {

    public static long timer;
    public static void m() throws Exception {
        timer -= System.currentTimeMillis();
//        System.out.println(timer);
        Thread.sleep(100);
        timer += System.currentTimeMillis();
//        System.out.println(timer);
    }
}
