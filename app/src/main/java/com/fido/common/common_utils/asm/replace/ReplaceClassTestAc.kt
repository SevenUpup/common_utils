package com.fido.common.common_utils.asm.replace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.databinding.AcReplaceClassTestBinding

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
class ReplaceClassTestAc:AppCompatActivity() {

    private val binding:AcReplaceClassTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            val h = iv.height
        }
    }

}