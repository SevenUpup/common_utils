package com.fido.click;

import java.io.File;

/**
 * @author: FiDo
 * @date: 2024/4/8
 * @des:
 */
public class Test {

    public void hello(int a,int b){
        if (a == b) {
            System.out.println("....");
        }
    }

    public static String getClassFilePath(Class clazz) {
        // file:/Users/zhy/hongyang/repo/BlogDemo/app/build/intermediates/javac/debug/classes/
        String buildDir = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        String fileName = clazz.getSimpleName() + ".class";
        System.out.println("buildDir =" + buildDir + " fileName=" + fileName);
        File file = new File(buildDir + clazz.getPackage().getName().replaceAll("[.]", "/") + "/", fileName);
        return file.getAbsolutePath();
    }

}
