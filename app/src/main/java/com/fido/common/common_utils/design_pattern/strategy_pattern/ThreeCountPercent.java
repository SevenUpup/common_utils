package com.fido.common.common_utils.design_pattern.strategy_pattern;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class ThreeCountPercent implements IDiscount{
    @Override
    public float getPrice(float originalPrice) {
        return (originalPrice * 3 ) - 10;
    }
}
