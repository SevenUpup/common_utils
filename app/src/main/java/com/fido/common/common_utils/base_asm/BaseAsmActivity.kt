package com.fido.common.common_utils.base_asm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.showStatusBar

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
open class BaseAsmActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        immersionBar {
            showStatusBar()
            fitsSystemWindows(true)
            statusBarColor(R.color.colorWheelView)
        }
    }

}