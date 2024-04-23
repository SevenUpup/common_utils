package com.fido.common.common_utils.asm

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.startActivity
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.asm.annotation.UnCheckViewOnClick
import com.fido.common.common_utils.asm.replace.ReplaceClassTestAc
import com.fido.common.common_utils.databinding.AcAsmTestBinding

/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
class ASMTestAc:AppCompatActivity() {

    private var A = true
    var B = "1"
    var G =  1
    companion object{
        private var C = false
        private val D = true
        var H = "0"
        var hBoolean = false
        private val str = "str"
        private var mChar = "mChar"
        private val K = 0
        fun showMyStaticToast() {
            toast("static")
        }
    }
    private var numner = 0
    private val binding:AcAsmTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logd("""
            A=${A}
            B=${B}
            C=${C}
            D=${D}
            str=${str}
            mChar=${mChar}
            H=${H}
            G=${G}
            K=${K}
        """.trimIndent())

        binding.apply {
            btReplaceClass.throttleClick {
                startActivity<ReplaceClassTestAc>()
            }

            bt1.setOnClickListener {
                showMyToast66()
            }

            btUncheck.setOnClickListener(object :View.OnClickListener{
                @UnCheckViewOnClick
                override fun onClick(v: View?) {
                    autoIncrementNumberWithUnCheck()
                }
            })

            bt2.setOnClickListener(object :OnClickListener{
                override fun onClick(v: View?) {
                    autoIncrementNumber()
                }
            })

            btToast.setOnClickListener {
                Toast.makeText(this@ASMTestAc, str,Toast.LENGTH_SHORT).show()
            }

            bt66.setOnClickListener {
                log66()
            }
        }
    }

    private fun log66(){
        logd("66")
    }

    private fun autoIncrementNumber() {
        numner++
        binding.tv.text = numner.toString()
    }

    private fun autoIncrementNumberWithUnCheck() {
        numner++
        binding.tv.text = numner.toString()
    }

    private fun showMyToast66() {
        numner++
        binding.tv.text = numner.toString()
        binding.bt1.text = "asm插装"
    }


    fun showMyToast2() {
        toast("666")
    }
}