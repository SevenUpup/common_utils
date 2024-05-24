package com.fido.common.common_utils.asm

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.fido.common.common_utils.asm.toast.OpenAppMarketUtils
import com.fido.common.databinding.AcAsmTestBinding


/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
class ASMTestAc:AppCompatActivity() {

    private var A = true
    var B = "1"
    var G =  1
    private val INT_VAL:Int = 1
    protected val INT_PROT = -1
    private val D_VAL = 0f
    private val LLL_VAL = 0L
    private val CHAR_VAL = ""
    val INT_PUB = 99
    companion object{
        private var STATCI_VAL_DOUBLE = 20.0
        private var STATCI_VAL_LONG = 66L
        private var C = false
        private val D = true
        var H = "0"
        var hBoolean = false
        private val str = "str"
        private var mChar = "mChar"
        private val K = 0
        //const 来定义常量，但是这个常量跟 Java 的 static final 是有所区别的，如果它的值无法在编译时确定，
        //则编译不过，因此 const 所定义的常量叫编译时常量
        //所以插件修改失败
        private const val LL = "Liao"
        fun showMyStaticToast() {
            toast("static")
        }
    }
    private var numner = 0
    private val binding:AcAsmTestBinding by binding()

    private var hhIndex = 0
    fun toHWMarketByAppId(context: Context, appId: String) {
        val text1 = "market://com.huawei.appmarket.applink?appId=$appId"
        val uri = Uri.parse(text1)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    fun launchAppDetilOnMarket2() {
        val text1 = "appmarket://details?id=com.huawei.browser"
        val uri = Uri.parse(text1)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logd("""
            A=${A}
            B=${B}
            C=$C
            D=${D}
            str=${str}
            mChar=${mChar}
            H=${H}
            G=${G}
            K=${K}
            LL=${LL}
            INT_VAL=${INT_VAL}
            INT_PROT=${INT_PROT}
            INT_PUB=${INT_PUB}
            STATCI_VAL_DOUBLE=${STATCI_VAL_DOUBLE}
            STATCI_VAL_LONG=${STATCI_VAL_LONG}
        """.trimIndent())

        binding.apply {
            btReplaceClass.throttleClick {
                startActivity<ReplaceClassTestAc>()
            }

            bt1.setOnClickListener {
//                showMyToast66()
                if (hhIndex%2 == 0) {
                    OpenAppMarketUtils.openAppMarket(this@ASMTestAc, "cn.goodjobs.client")
                    hhIndex++
                } else {
                    toHWMarketByAppId(this@ASMTestAc,"cn.goodjobs.client")
                }
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
        toast(LL)
    }


    fun showMyToast2() {
        toast("666")
    }
}