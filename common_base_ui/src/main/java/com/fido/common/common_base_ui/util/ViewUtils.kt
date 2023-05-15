package com.fido.common.common_base_ui.util

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.DrawableRes

val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/*converts sp value into px*/
val Number.sp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()

// ============================ TextView ===============================

fun TextView.setDrawable(
    @DrawableRes leftDrawable: Int = 0,
    @DrawableRes topDrawable: Int = 0,
    @DrawableRes rightDrawable: Int = 0,
    @DrawableRes bottomDrawable: Int = 0
) {
    setCompoundDrawablesWithIntrinsicBounds(leftDrawable,topDrawable,rightDrawable,bottomDrawable)
}

fun TextView.setDrawable(
    left:Drawable?=null,
    top:Drawable?=null,
    right:Drawable?=null,
    bottom:Drawable?=null,
){
    setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}