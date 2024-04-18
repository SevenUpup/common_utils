package com.fido.common.common_utils.asm

import android.widget.Toast
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.asm.toast.HToaster

/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
object ASMTestUtils {

    @JvmStatic
    fun asmToast(){
        toast("666666")
    }

    fun toast(){
        val cc = "cc"
//        val t = Toast.makeText(app,cc,Toast.LENGTH_SHORT)
        HToaster.hToast(Toast.makeText(app,cc,Toast.LENGTH_SHORT))
//        HToaster.hToast(t)
    }

}