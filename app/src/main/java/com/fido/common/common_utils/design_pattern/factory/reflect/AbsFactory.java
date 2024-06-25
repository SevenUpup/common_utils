package com.fido.common.common_utils.design_pattern.factory.reflect;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
abstract public class AbsFactory {

    public abstract <T extends Usb> T createProduct(Class<T> clazz);

}
