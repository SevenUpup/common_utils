package com.fido.common.common_base_util.util.toast

import android.os.Handler
import android.os.Looper

internal object ThreadUtils {
    fun runMain(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            Handler(Looper.getMainLooper()).post { block() }
        }
    }
}