package com.fido.common.common_utils.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.height
import com.fido.common.common_base_util.getScreenHeightPx
import com.fido.common.common_utils.databinding.AcCustomViewBinding

/**
 * @author: FiDo
 * @date: 2024/4/7
 * @des:
 */
class CustomViewAc:AppCompatActivity() {

    private val binding:AcCustomViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.codeRainView
        view.height(getScreenHeightPx())
        view.click {  }
    }

}