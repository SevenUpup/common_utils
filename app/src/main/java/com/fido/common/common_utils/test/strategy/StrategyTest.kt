package com.fido.common.common_utils.test.strategy

import android.os.Build
import androidx.annotation.RequiresApi

/**
@author FiDo
@description:
@date :2023/4/24 15:16
 */
class StrategyTest {

    companion object{
        @RequiresApi(Build.VERSION_CODES.N)
        @JvmStatic
        fun main(args: Array<String>) {
            PersonalSale.sale(
                "A"
            ) { obj: SaleStrategyInterface -> obj.detail() }

            PersonalSale.sale("B") { saleStrategyInterface: SaleStrategyInterface ->
                saleStrategyInterface.detail()
                println("saleStrategyInterface = $saleStrategyInterface")
            }
        }
    }

}