package com.fido.common.common_utils.asm

import android.widget.Toast
import com.fido.common.common_base_util.app
import com.fido.common.common_utils.asm.toast.HToaster

/**
 * @author: FiDo
 * @date: 2024/4/16
 * @des:
 */
object ASMTestUtils {

    var isT = true

    @JvmStatic
    fun asmToast(){
        toast("666666")
    }

    fun toast(str:String){
//        val cc = "cc"
        Toast.makeText(app,str,Toast.LENGTH_SHORT).show()
        HToaster.hToast(Toast.makeText(app,str,Toast.LENGTH_SHORT))
    }

}


class T{
    var isB = true
}

interface HToastInterface{
    fun hToaster(toast: Toast){
        toast.show()
    }
}