package com.fido.common.common_utils.test.strategy;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

/**
 * @author FiDo
 * @description:
 * @date :2023/4/24 15:07
 */
public class PersonalSale {

    private static final String CLAZZ_NAME = "com.fido.common.common_utils.test.strategy.SaleSstrategy";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sale(String saleName, Consumer<SaleStrategyInterface> consumer){
        String clazz = CLAZZ_NAME + saleName;
        try {
            Class<?> aClass = Class.forName(clazz);
            SaleStrategyInterface strategyInterface = (SaleStrategyInterface) aClass.getDeclaredConstructor().newInstance((Object[]) null);
            consumer.accept(strategyInterface);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
