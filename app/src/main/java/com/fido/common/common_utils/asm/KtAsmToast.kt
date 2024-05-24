package com.fido.common.common_utils.asm

import android.content.Context
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.toast

/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
object KtAsmToast {

    @JvmStatic
    fun showKtToast() {
//        toast("KtAsmToast")
        logd("KtAsmToast showKtToast")
    }

    @JvmStatic
    fun log99(){
        logd("9999999")
    }


    @JvmStatic
    fun showAsmCustomToast(context: Context,text:String){
        toast(text)
    }

}
