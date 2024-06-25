package com.fido.common.common_utils.design_pattern.strategy_pattern;

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
public class testStrategy {

    public static void main(String[] args) {

        DiscountControl discountControl = new DiscountControl();

        OneCountPercent oneCountPercent = new OneCountPercent();
        discountControl.setDiscount(oneCountPercent);
        float finalPrice = discountControl.getFinalPrice(120);
        System.out.println("买一件最终价格："+finalPrice);

        TwoCountPercent twoCountPercent = new TwoCountPercent();
        discountControl.setDiscount(twoCountPercent);
        float finalPrice1 = discountControl.getFinalPrice(120);
        System.out.println("买两件最终价格："+finalPrice1);

        ThreeCountPercent threeCountPercent = new ThreeCountPercent();
        discountControl.setDiscount(threeCountPercent);
        float finalPrice2 = discountControl.getFinalPrice(120);
        System.out.println("买三件最终价格："+finalPrice2);

    }

}
