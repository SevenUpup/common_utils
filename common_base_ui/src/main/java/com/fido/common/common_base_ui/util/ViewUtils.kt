package com.fido.common.common_base_ui.util

import android.content.res.Resources

val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/*converts sp value into px*/
val Number.sp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()