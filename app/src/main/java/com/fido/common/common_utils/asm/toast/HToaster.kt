package com.fido.common.common_utils.asm.toast

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Message
import android.widget.Toast

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
object HToaster {


    /**
     * 参数一定是Toast，因为要与插件中的方法对应
     */
    @JvmStatic
    fun hToast(toast: Toast){
        hookToastIfNeed(toast)
//        toast.setText("HToast")
        toast.show()
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun hookToastIfNeed(toast: Toast) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                val cToast = Toast::class.java
                val fTn = cToast.getDeclaredField("mTN")
                fTn.isAccessible = true
                val oTn = fTn.get(toast)
                val cTn = oTn.javaClass
                val fHandle = cTn.getDeclaredField("mHandler")
                fHandle.isAccessible = true
                fHandle.set(oTn, ProxyHandler(fHandle.get(oTn) as Handler))
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private class ProxyHandler(private val mHandler: Handler) : Handler(mHandler.looper) {

        override fun handleMessage(msg: Message) {
            try {
                mHandler.handleMessage(msg)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

}