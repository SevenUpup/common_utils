package com.fido.common.common_utils.design_pattern.strategy_pattern;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des: 定义一个策略控制类
 */
public class DiscountControl {

    private IDiscount iDiscount;

    //设置打折的方式
    public void setDiscount(IDiscount iDiscount){
        this.iDiscount = iDiscount;
    }

    public float getFinalPrice(float originalPrice){
        return iDiscount.getPrice(originalPrice);
    }

}
