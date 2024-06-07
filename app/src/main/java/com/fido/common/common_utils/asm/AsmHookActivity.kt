package com.fido.common.common_utils.asm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.fido.common.common_base_ui.base.dialog.createPop
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcAsmHookBinding
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

/**
 * @author: FiDo
 * @date: 2024/5/24
 * @des:
 */
class AsmHookActivity:AppCompatActivity() {

    private val binding:AcAsmHookBinding by binding()

    companion object{
        private fun showCustomPop(context: Context){
            context.createPop(R.layout.layout_header_view).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            btShowPop.throttleClick {
                showCustomPop(context)
            }

            btShowToast.throttleClick {
                KtAsmToast.showAsmCustomToast(context,"我本来是一个吐司啊")
            }

            //直接反射调用私有静态方法
            btPrivateStaticMethodReplace.throttleClick {
                val kClazz = AsmHookUtils::class.java.kotlin
                val method = kClazz.declaredFunctions.find { it.name == "privateStaticFun" }
                method?.isAccessible = true
                method?.call(AsmHookUtils)
            }

            //通过调用外部类静态方法中调用静态私有方法
            btPrivateStaticMethodIndirect.throttleClick {
                AsmHookUtils.indirectCallStaticFun()
            }

            //调用外部类静态方法
            btPrivateStaticMethodOuter.throttleClick {
                AsmHookUtils.pubStaticFun2()
            }

            btStaticMethodReplace.throttleClick {
                PrivateStaticUtils.pubStaticFun()
            }

        }
    }


    object PrivateStaticUtils{
        @JvmStatic
        fun pubStaticFun(){
            toast("111")
        }
//        private fun privateStaticFun(){
//
//        }
    }
}

object AsmHookUtils{
    @JvmStatic
    fun pubStaticFun2(){
        toast("333")
    }

    @JvmStatic
    fun indirectCallStaticFun(){
        privateStaticFun()
    }

    @JvmStatic
    private fun privateStaticFun(){
        toast("222")
    }
}


