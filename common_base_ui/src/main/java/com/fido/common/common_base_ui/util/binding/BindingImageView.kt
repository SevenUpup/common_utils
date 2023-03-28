package com.fido.common.common_base_ui.util.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.fido.common.common_base_ui.ext.imageview.load
import com.fido.common.common_base_ui.util.dp

/**
 * 设置图片的加载
 */
@BindingAdapter("imgUrl", "placeholder", "roundRadius", "isCircle", requireAll = false)
fun loadImg(
    view: ImageView,
    url: Any?,
    placeholder: Drawable? = null,
    roundRadius: Int = 0,
    isCircle: Boolean = false
) {
    url?.let {
        view.load(
            it,
            placeholder = placeholder,
            roundRadius = roundRadius.dp,
            isCircle = isCircle
        )
    }
}


@BindingAdapter("loadBitmap")
fun loadBitmap(view: ImageView, bitmap: Bitmap?) {
    view.setImageBitmap(bitmap)
}