package com.fido.common.common_utils.design_pattern.factory.reflect;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class Camera implements Usb{
    @Override
    public void store() {
        System.out.println("camera usb store!");
    }
}
