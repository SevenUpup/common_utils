package com.fido.common.common_utils.asm

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.asm.annotation.UnCheckViewOnClick
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
    private var numner = 0
    private val binding:AcAsmTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            bt1.setOnClickListener {
                showMyToast()
            }

            btUncheck.setOnClickListener {
                autoIncrementNumberWithUnCheck()
            }

            bt2.setOnClickListener(object :OnClickListener{
                override fun onClick(v: View?) {
                    autoIncrementNumber()
                }
            })
        }
    }

    private fun autoIncrementNumber() {
        numner++
        binding.tv.text = numner.toString()
    }

    @UnCheckViewOnClick
    private fun autoIncrementNumberWithUnCheck() {
        numner++
        binding.tv.text = numner.toString()
    }

    private fun showMyToast() {
        numner++
        binding.tv.text = numner.toString()
        binding.bt1.text = "asm插装"
    }


    fun showMyToast2() {
        toast("666")
    }
}