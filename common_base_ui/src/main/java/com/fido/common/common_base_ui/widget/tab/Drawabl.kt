package com.fido.common.common_base_ui.widget.tab

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable


/**
 * @author: FiDo
 * @date: 2024/5/21
 * @des:
 */
internal fun getCustomDrawable(solidColor: Int, radius: Int): Drawable? {
    return getCustomDrawable(solidColor, radius, 0, 0)
}

internal fun getCustomDrawable(solidColor: Int, radius: Int, storkColor: Int, storkWidth: Int): Drawable? {
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.RECTANGLE
    drawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
    drawable.setColor(solidColor)
    if (radius > 0) {
        drawable.cornerRadii = floatArrayOf(
            radius.toFloat(), radius.toFloat(),
            radius.toFloat(), radius.toFloat(),
            radius.toFloat(), radius.toFloat(),
            radius.toFloat(), radius
                .toFloat()
        )
    }
    if (storkWidth > 0) {
        drawable.setStroke(storkWidth, storkColor, 0f, 0f)
    }
    return drawable
}