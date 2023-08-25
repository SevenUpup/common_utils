package com.fido.common.common_utils.customgradle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FiDo
 * @description:
 * @date :2023/8/24 14:59
 */
public class ImageMaxFinder {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("aapt dump badging output.apk");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Pattern pattern = Pattern.compile("mipmap\\s+(.*?)\\s+(\\d+x\\d+)");
            String maxImageName = null;
            int maxWidth = 0;
            int maxHeight = 0;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String imageName = matcher.group(1);
                    int width = Integer.parseInt(matcher.group(2).split("x")[0]);
                    int height = Integer.parseInt(matcher.group(2).split("x")[1]);
                    if (width > maxWidth || (width == maxWidth && height > maxHeight)) {
                        maxImageName = imageName;
                        maxWidth = width;
                        maxHeight = height;
                    }
                }
            }
            System.out.println("Max image name: " + maxImageName);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}