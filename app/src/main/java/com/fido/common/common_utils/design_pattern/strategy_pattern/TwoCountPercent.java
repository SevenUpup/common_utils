package com.fido.common.common_utils.design_pattern.strategy_pattern;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class TwoCountPercent implements IDiscount{
    @Override
    public float getPrice(float originalPrice) {
        return (originalPrice * 2 ) * 0.85f;
    }
}
