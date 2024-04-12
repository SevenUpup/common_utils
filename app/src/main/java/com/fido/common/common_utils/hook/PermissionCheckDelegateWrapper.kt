package com.fido.common.common_utils.hook

import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.toast

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:
 */
object PermissionCheckDelegateWrapper {

    @JvmStatic
    fun hookRequestPermission(caller:Any,permissions:Array<String>,requestCode:Int){
        toast("hook permission")
        logd("""
            caller=>$caller
            permissions=>$permissions
            requestCode=>${requestCode}
        """.trimIndent())
    }


    @JvmStatic
    fun testHook(string: String,int: Int){
        toast("find hook point testHook")
        logd("""
            string=>$string
            int=>$int
        """.trimIndent())
    }

}