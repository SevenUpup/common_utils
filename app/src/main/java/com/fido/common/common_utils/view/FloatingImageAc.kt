package com.fido.common.common_utils.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_utils.databinding.AcFloatingImgBinding

/**
 * @author: FiDo
 * @date: 2024/3/4
 * @des:
 */
class FloatingImageAc:AppCompatActivity() {

    val binding: AcFloatingImgBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.floatingIv.apply {

        }
    }

}