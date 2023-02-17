package com.fido.common.common_base_util.util.toast.interfaces

import android.content.Context
import android.widget.Toast

interface ToastFactory {

    companion object DEFULT : ToastFactory {
        override fun onCreate(
            context: Context,
            msg: CharSequence,
            duration: Int,
            tag: Any?
        ): Toast? {
            return Toast.makeText(context, msg, duration)
        }
    }

    /**
     * 创建吐司
     * @param context   - application
     * @param msg       - 吐司内容
     * @param duration  - 吐司时长
     * @param tag       - 吐司标签
     */
    fun onCreate(
        context: Context,
        msg: CharSequence,
        duration: Int,
        tag: Any? = null
    ): Toast?

}