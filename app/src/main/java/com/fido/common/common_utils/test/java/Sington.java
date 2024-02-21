package com.fido.common.common_utils.test.java;

/**
 * @author: FiDo
 * @date: 2024/2/19
 * @des:
 */
public class Sington {

    public static Sington getInstance(){
        try {
            Class.forName("",false,null);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return Instance.SINGTON;
    }

    private static class Instance{
        private static final Sington SINGTON = new Sington();
    }

}
