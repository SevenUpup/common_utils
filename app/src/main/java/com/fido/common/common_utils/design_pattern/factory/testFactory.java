package com.fido.common.common_utils.design_pattern.factory;

import com.fido.common.common_utils.design_pattern.factory.reflect.Camera;
import com.fido.common.common_utils.design_pattern.factory.reflect.Phone;
import com.fido.common.common_utils.design_pattern.factory.reflect.UsbFactory;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class testFactory {

    public static void main(String[] args) {

        UsbFactory usbFactory = new UsbFactory();
        usbFactory.createProduct(Phone.class).store();
        usbFactory.createProduct(Camera.class).store();

    }

}
