package com.fido.common.common_utils.test.strategy;

public class SaleSstrategyA implements SaleStrategyInterface{
    @Override
    public void detail() {
        System.out.println("满100减10");
    }
}
