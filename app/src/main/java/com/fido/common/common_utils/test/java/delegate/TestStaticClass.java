package com.fido.common.common_utils.test.java.delegate;

/**
 * @author: FiDo
 * @date: 2024/4/2
 * @des:
 */
public class TestStaticClass {

    static String number = "123";
    static int count;

    static {
        System.out.println("cout 111 =" + count);
        count = 10;
        System.out.println("cout 222 =" + count);
    }

    public static String getNumber(){
        return number;
    }

}
