package com.fido.common.common_utils.customgradle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author FiDo
 * @description:
 * @date :2023/8/24 14:58
 */
public class ImageCounter {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("aapt dump badging output.apk");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("drawable")) {
                    int count = 0;
                    while ((line = reader.readLine()) != null && line.contains("drawable")) {
                        count++;
                    }
                    System.out.println("Total number of images: " + count);
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
