package com.fido.common.common_base_util.ext

import androidx.annotation.StringRes
import com.fido.common.common_base_util.util.toast.ToastConfig
import com.fido.common.common_base_util.util.toast.ThreadUtils.runMain

@JvmOverloads
fun toast(msg: CharSequence?, tag: Any? = null) {
    showToast(msg, 0, tag)
}

@JvmOverloads
fun toast(@StringRes msgId: Int, tag: Any? = null) {
    showToast(ToastConfig.context.getString(msgId), 0, tag)
}

fun longToast(msg: CharSequence?, tag: Any? = null) {
    showToast(msg, 1, tag)
}

fun longToast(@StringRes msgId: Int, tag: Any? = null) {
    showToast(ToastConfig.context.getString(msgId), 1, tag)
}

/**
 * 显示吐司
 * @param msg 吐司内容
 * @param duration 吐司显示时长 0 短时间显示 1 长时间显示
 * @param tag 标记, 标记用于[com.fido.common.common_base_util.util.toast.interfaces.ToastFactory]区分吐司
 */
fun showToast(msg: CharSequence?, duration: Int, tag: Any? = null) {
    msg ?: return
    ToastConfig.cancle()
    runMain {
        ToastConfig.toast =
            ToastConfig.toastFactory.onCreate(ToastConfig.context, msg, duration, tag)
        ToastConfig.toast?.show()
    }
}