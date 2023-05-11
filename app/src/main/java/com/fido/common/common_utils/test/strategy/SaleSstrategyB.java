package com.fido.common.common_utils.test.strategy;

public class SaleSstrategyB implements SaleStrategyInterface{
    @Override
    public void detail() {
        System.out.println("满200减50");
    }
}
