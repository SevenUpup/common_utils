package com.fido.common.common_base_ui.ext.imageview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.File

/**
 *  引擎类
 *  ImageView相关的图片加载
 */

/**
 * Glide加载图片
 * @param url 可以是网络，可以是File，可以是资源id等等Glide支持的类型
 * @param placeholderRes 默认占位图
 * @param error 失败占位图
 * @param isCircle 是否是圆形，默认false，注意：isCircle和roundRadius两个只能有一个生效
 * @param isCenterCrop 是否设置scaleType为CenterCrop，你也可以在布局文件中设置
 * @param roundRadius 圆角角度，默认为0，不带圆角，注意：isCircle和roundRadius两个只能有一个生效
 * @param isCrossFade 是否有过渡动画，默认有过渡动画
 * @param isForceOriginalSize 是否强制使用原图，默认false
 */
@SuppressLint("CheckResult")
fun ImageView.load(
    url: Any?,
    placeholderRes: Int = 0,
    error: Int = 0,
    isCircle: Boolean = false,
    isCenterCrop: Boolean = false,
    roundRadius: Int = 0,
    topLeftRadius: Float = 0f,
    topRightRadius: Float = 0f,
    bottomLeftRadius: Float = 0f,
    bottomRightRadius: Float = 0f,
    placeholder: Drawable? = null,
    isCrossFade: Boolean = false,
    isForceOriginalSize: Boolean = false
) {

    //配置选项
    val options = RequestOptions()

    if (placeholder != null) {
        options.placeholder(placeholder)
    } else {
        options.placeholder(placeholderRes)
    }

    options.error(error).apply {
        if (isCenterCrop && scaleType != ImageView.ScaleType.CENTER_CROP)
            scaleType = ImageView.ScaleType.CENTER_CROP

        if (isCircle) {
            if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                transforms(CenterCrop(), CircleCrop())
            } else {
                transform(CircleCrop())
            }

        } else if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {

            if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                transforms(CenterCrop(), GranularRoundedCorners(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius))
            } else {
                transform(GranularRoundedCorners(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius))
            }

        } else if (roundRadius != 0) {
            if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                transforms(CenterCrop(), RoundedCorners(roundRadius))
            } else {
                transform(RoundedCorners(roundRadius))
            }
        }

        if (isForceOriginalSize) {
            override(Target.SIZE_ORIGINAL)
        }
    }

    //以配置的方式加载图片
    Glide.with(context).load(url)
        .apply(options)
        .apply {
            if (isCrossFade) transition(DrawableTransitionOptions.withCrossFade())
        }
        .into(this)
}

/**
 * ImageView加载Gif的图片
 */
fun ImageView.loadGif() {
    //如果要体验好的Gif需要导入GifDrawable库，参考YYCircle
}

/**
 * 下载并返回File对象
 */
@SuppressLint("CheckResult")
fun Any.downloadImage(context: Context?, path: Any?, block: (file: File) -> Unit) {

    Glide.with(context!!).load(path).downloadOnly(object : SimpleTarget<File?>() {

        override fun onResourceReady(resource: File, transition: Transition<in File?>?) {

            block(resource)
        }
    })

}

/**
 * 加载出Drawable对象
 */
@SuppressLint("CheckResult")
fun Any.loadDrawable(context: Context?, path: Any?, placeholderRes: Int?, block: (drawable: Drawable?) -> Unit) {

    val options = RequestOptions()
    placeholderRes?.also {
        if (it > 0) {
            options.placeholder(placeholderRes)
        }
    }

    Glide.with(context!!).load(path).apply(options).into(object : SimpleTarget<Drawable>() {
        override fun onLoadStarted(placeholder: Drawable?) {
            super.onLoadStarted(placeholder)
            block(placeholder)
        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            block(resource)
        }

    })
}