package com.fido.common.common_utils.design_pattern.strategy_pattern;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:  定义一个商品打折策略:1件9折，两件8.5折，三件减10元
 */
public interface IDiscount {

    float getPrice(float originalPrice);

}
