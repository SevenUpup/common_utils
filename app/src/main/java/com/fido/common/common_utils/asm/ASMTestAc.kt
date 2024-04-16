package com.fido.common.common_utils.asm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.databinding.AcAsmTestBinding

/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
class ASMTestAc:AppCompatActivity() {

    companion object{
        fun showMyStaticToast() {
            toast("static")
        }
    }

    private val binding:AcAsmTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            bt1.throttleClick {
                showMyToast()
            }
        }
    }

    fun showMyToast() {
        toast("111")
    }


    fun showMyToast2() {
        toast("666")
    }
}