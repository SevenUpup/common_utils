package com.fido.common.common_utils.test.asm;

/**
 * @author: FiDo
 * @date: 2024/4/12
 * @des:
 */
public class TestTool {

    public static void useTool(long var){
        System.out.println("useTool " + var);
    }


    public static void tt(String s){
        if (isStrNumber(s)){
            System.out.println("6666");
        }
//        System.out.println("6666");
    }


    public static boolean isStrNumber(String str){
        return "666".equals(str);
    }

}
