package com.fido.common.common_utils.test.asm;

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:
 */
public class PrintTest {

    public static boolean C = false;
    public static void main(String[] args) {
        test();
    }
    public static void test() {
        System.out.println("Hello world!");
    }

    public void testA(){
        long var1 = System.currentTimeMillis();
        System.out.println("i am A");
        long var3 = System.currentTimeMillis();
        System.out.println(new StringBuilder().append("cost:").append(var3-var1).toString());
//        TestTool.useTool(var1);
    }

}
