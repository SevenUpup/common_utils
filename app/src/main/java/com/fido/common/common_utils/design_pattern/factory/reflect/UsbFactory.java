package com.fido.common.common_utils.design_pattern.factory.reflect;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class UsbFactory extends AbsFactory{
    @Override
    public <T extends Usb> T createProduct(Class<T> clazz) {
        Usb usb = null;
        try {
            usb = (Usb) Class.forName(clazz.getName()).newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T) usb;
    }
}
