package com.fido.common.common_base_util.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * @author: FiDo
 * @date: 2024/4/2
 * @des:  some Context Extentions
 */

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this as Activity
    is ContextWrapper -> (this as ContextWrapper).baseContext.findActivity()
    else -> null
}
