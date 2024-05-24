package com.fido.common.common_utils.asm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.fido.common.common_base_ui.base.dialog.createPop
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.databinding.AcAsmHookBinding

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
        }
    }

}