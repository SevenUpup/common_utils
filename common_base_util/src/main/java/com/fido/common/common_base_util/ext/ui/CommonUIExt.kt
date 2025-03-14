package com.fido.common.common_base_util.ext.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import com.fido.common.common_base_util.ext.isFakeBoldText
import com.fido.common.common_base_util.ext.widthAndHeight

/**
 * @author: FiDo
 * @date: 2025/2/26
 * @des:  一些 UI 的 扩展
 */

fun createBoldText(
    context: Context,
    content: CharSequence,
    textSize: Float = 24f,
    textColor: Int = Color.parseColor("#333333"),
    gravity: Int = Gravity.CENTER,
    background: Drawable?=null,
    lineSpacingMult:Float = 1f,
) = createText(context, content, textColor, textSize,true,gravity,background,lineSpacingMult)

fun createText(
    context: Context,
    content: CharSequence,
    textColor: Int,
    textSize: Float,
    isBold: Boolean = false,
    gravity: Int = Gravity.CENTER,
    background: Drawable?=null,
    lineSpacingMult:Float = 1f,
): TextView {
    return TextView(context).apply {
        isFakeBoldText = isBold
        setTextSize(textSize)
        setTextColor(textColor)
        text = content
        this.gravity = gravity
        setLineSpacing(0f,lineSpacingMult)
        if (background != null) {
            this.background = background
        }
    }
}

/**
 * @param container TextView 的父容器
 */
fun addText(
    container: ViewGroup,
    text: CharSequence,
    isBold: Boolean = true,
    textSize: Float = 24f,
    textColor: Int = Color.parseColor("#333333"),
    gravity: Int = Gravity.CENTER,
    background:Drawable?=null,
    layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ),
    lineSpacingMult:Float = 1f,
): TextView {
    val textView = createText(container.context, text, textColor, textSize, isBold, gravity,background, lineSpacingMult = lineSpacingMult)
    container.addView(textView,layoutParams)
    return textView
}

fun addSpace(
    container: ViewGroup,
    space:Int,
    isVertical:Boolean = true
){
    container.addView(createSpaceView(container.context,space, isVertical))
}

/**
 * @param isVertical true 竖直方向占位/水平方向占位
 */
fun createSpaceView(context: Context,space:Int,isVertical:Boolean = true): Space {
    return Space(context).apply {
        widthAndHeight(if (isVertical) 0 else space,if (isVertical) space else 0)
    }
}


fun addLinear(
    container: ViewGroup,
    orientation:Int = LinearLayout.HORIZONTAL,
    layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ),
    background:Drawable?=null,
    linearInit:(LinearLayout.()->Unit)?=null
){
    val linearContainer = LinearLayout(container.context)
    linearContainer.orientation = orientation
    if (background != null) {
        linearContainer.background = background
    }
    container.addView(linearContainer,layoutParams)
    linearInit?.invoke(linearContainer)
}

/*fun ImageView.setBlurViewFromView(
    view: View,
    blurRadius:Int = 25,
){
    setBlurViewFromBitmap(view.toBitmap(),this,blurRadius)
}*/

/**
 * 将ImageView 设置为 传入的 bitmap 的高斯模糊效果
 * 依赖 implementation 'jp.wasabeef:glide-transformations:4.3.0'
 */
/*
fun setBlurViewFromBitmap(
    bitmap: Bitmap,
    targetView: ImageView,
    blurRadius:Int = 25,
){
    Glide.with(targetView.context)
        .load(bitmap)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(blurRadius,3)))
        .into(targetView)
}*/
