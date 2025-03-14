package com.fido.common.common_utils.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.databinding.AcHtCustomViewBinding

/**
 * @author: FiDo
 * @date: 2025/3/4
 * @des:  自定义View测试
 */
class HTCustomViewAc:AppCompatActivity() {

    private val binding:AcHtCustomViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {


        }
    }

}