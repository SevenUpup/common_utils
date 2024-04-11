package com.fido.common.common_utils.privacy

import android.os.Build
import android.util.Log
import com.lody.whale.xposed.XC_MethodHook
import com.lody.whale.xposed.XposedBridge

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:    whale：安卓隐私合规调试工具
 *          https://github.com/DonaldDu/whale
 */
object WhaleHook {
    fun init() {
        hookAndroidId()
    }

    private fun hookAndroidId() {
        if (Build.VERSION.SDK_INT <= 28){  // 小于Android9
            try {
                XposedBridge.log("start androidId")
                //android.provider.Settings.Secure#getString
                val clazz = android.provider.Settings.Secure::class.java
                val getString = clazz.getDeclaredMethod(
                    "getString", android.content.ContentResolver::class.java, java.lang.String::class.java
                )
                XposedBridge.hookMethod(getString, object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val key = param.args.last()
                        Log.d("hook", "beforeHookedMethod: key=${key}")
                        if (key == android.provider.Settings.Secure.ANDROID_ID) {
                            val msg = "hookAndroidId to 0"
                            println(msg)
                            Log.d("hook", msg)
                            Exception(msg).printStackTrace()//print call StackTrace
                            param.result = "0"
                        }
                    }
                })
            }catch (e:Exception){
                e.printStackTrace()
                Log.d("hook", "hookAndroidId: ${e.message}")
            }
        }
    }
}
