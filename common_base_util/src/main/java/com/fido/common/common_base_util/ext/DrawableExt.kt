@file:JvmName("DrawableExt")

package com.fido.common.common_base_util.ext

import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.StateSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.getColor

/**
 * 内部参数已转换不需要二次转换 dp、color等
 * @param solidColor = R.color.xx
 * @param topLeftRadius = 10f
 * @param dashWidth 分割线  长度 已转换dp
 * @param dashGap   分割线  间距 已转换dp
 *
 */
fun View.rectangleCornerBg(
    solidColor: Int,
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float,
    storkColor: Int = android.R.color.white,
    storkWidth: Int = 0,
    dashWidth: Float = 0f,
    dashGap: Float = 0f,
) {
    this.background = getCustomDrawable(
        solidColor.getColor,
        topLeftRadius,
        topRightRadius,
        bottomLeftRadius,
        bottomRightRadius,
        storkColor,
        storkWidth,
        dashWidth,
        dashGap
    )
}

fun View.rectangleCornerBg(
    solidColor: String,
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float,
    storkColor: Int = android.R.color.white,
    storkWidth: Int = 0,
    dashWidth: Float = 0f,
    dashGap: Float = 0f,
) {
    this.background = getCustomDrawable(
        solidColor.getColor,
        topLeftRadius,
        topRightRadius,
        bottomLeftRadius,
        bottomRightRadius,
        storkColor,
        storkWidth,
        dashWidth,
        dashGap
    )
}

fun View.rectangleCornerBg(
    solidColor: Int,
    radius: Float,
    isTopCorner: Boolean = true,
    isBottomCorner: Boolean = true,
    storkColor: Int = android.R.color.white,
    storkWidth: Int = 0,
) {
    val topRadius = if (isTopCorner) radius else 0f
    val bottomRadius = if (isBottomCorner) radius else 0f
    rectangleCornerBg(
        solidColor,
        topRadius,
        topRadius,
        bottomRadius,
        bottomRadius,
        storkColor,
        storkWidth
    )
}

fun View.rectangleCornerBg(
    solidColor: String,
    radius: Float,
    isTopCorner: Boolean = true,
    isBottomCorner: Boolean = true,
    @ColorRes storkColor: Int = android.R.color.white,
    storkWidth: Int = 0,
) {
    val topRadius = if (isTopCorner) radius else 0f
    val bottomRadius = if (isBottomCorner) radius else 0f
    this.background = getCustomDrawable(
        solidColor.getColor,
        topRadius,
        topRadius,
        bottomRadius,
        bottomRadius,
        storkColor,
        storkWidth
    )
}

fun View.gradientShapeDrawableBg(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float,
    colors: IntArray,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
) = this.apply {
    background = getCustomDrawable(
        solidColor = -1,
        topLeftRadius = topLeftRadius,
        topRightRadius = topRightRadius,
        bottomLeftRadius = bottomLeftRadius,
        bottomRightRadius = bottomRightRadius,
        colors = colors,
        orientation = orientation
    )
}

fun View.gradiendShapeDrawableBg(
    radius: Float,
    colors: IntArray,
    isTopCorner: Boolean = true,
    isBottomCorner: Boolean = true,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
) = this.apply {
    val topRadius = if (isTopCorner) radius else 0f
    val bottomRadius = if (isBottomCorner) radius else 0f
    background = getCustomDrawable(
        -1,
        topRadius,
        topRadius,
        bottomRadius,
        bottomRadius,
        colors = colors,
        orientation = orientation
    )
}

/**
 * 添加指定状态的Drawable
 * isDefultDrawable 很关键，设置默认状态drawable 为true时,[addStatusableDrawableBg] 必须放在最后调用哦! 否则默认状态不生效
 * @param status 选择状态 默认Normal
 * @param isDefultDrawable 是否是默认状态的drawable,
 * PRESSED,SELECTED,ENABLED
 */
fun View.addStatusableDrawableBg(
    solidColor: Int,
    radius: Float = 0f,
    status: DrawableStatus = DrawableStatus.SELECTED,
    isTopCorner: Boolean = true,
    isBottomCorner: Boolean = true,
    isDefultDrawable: Boolean = false,
) = apply {
    val stateSet = when (status) {
        DrawableStatus.PRESSED -> intArrayOf(android.R.attr.state_pressed)
        DrawableStatus.SELECTED -> intArrayOf(android.R.attr.state_selected)
        DrawableStatus.ENABLED -> intArrayOf(android.R.attr.state_enabled)
        DrawableStatus.FOCUSED -> intArrayOf(android.R.attr.state_focused)
        DrawableStatus.CHECKED -> intArrayOf(android.R.attr.state_checked)
    }
    val topRadius = if (isTopCorner) radius else 0f
    val bottomRadius = if (isBottomCorner) radius else 0f
    val drawable =
        getCustomDrawable(solidColor.getColor, topRadius, topRadius, bottomRadius, bottomRadius)

    // StateSet.WILD_CARD 表示非所有状态，也就是正常状态下的drawable
    // 有一点需要注意的是，StateSet.WILD_CARD的drawable必须要放在最后
    if (background is StateListDrawable) {
        val sd: StateListDrawable = background as StateListDrawable
        // 解决 backGround 都是引用了同一个xml资源
        sd.mutate()
        sd.addState(if (isDefultDrawable) StateSet.WILD_CARD else stateSet, drawable)
    } else {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(if (isDefultDrawable) StateSet.WILD_CARD else stateSet, drawable)
        background = stateListDrawable
    }
}

enum class DrawableStatus {
    PRESSED,
    SELECTED,
    ENABLED,
    FOCUSED,
    CHECKED,
}

fun getCustomDrawable(
    solidColor: Int,
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float,
    storkColor: Int = android.R.color.white,
    storkWidth: Int = 0,
    dashWidth: Float = 0f,
    dashGap: Float = 0f,
    shape: Int = GradientDrawable.RECTANGLE,
    colors: IntArray = emptyArray<Int>().toIntArray(),
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM,
    gradientType: Int = GradientDrawable.LINEAR_GRADIENT
) = GradientDrawable().apply {
    this.shape = shape
    this.orientation = orientation
    if (colors.isNotEmpty()) {
        this.gradientType = gradientType
        this.colors = colors
//        this.setGradientCenter(0.5f,0.5f)
    } else {
        setColor(solidColor)
    }
    val tlRadius = topLeftRadius.dp.toFloat()
    val trRadius = topRightRadius.dp.toFloat()
    val blRadius = bottomLeftRadius.dp.toFloat()
    val brRadius = bottomRightRadius.dp.toFloat()
    val radius = floatArrayOf(
        tlRadius, tlRadius,
        trRadius, trRadius,
        brRadius, brRadius,
        blRadius, blRadius
    )
    cornerRadii = radius

    if (storkWidth > 0) {
        setStroke(storkWidth.dp, storkColor.getColor, dashWidth.dp.toFloat(), dashGap.dp.toFloat())
    }
}

fun View.gradientColorBgAnim(
    startColor: Int,
    endColor: Int,
    animDuration: Long = 500L,
    radius: Float = 0f,
    autoStart: Boolean = true,
    infinite:Boolean = false,
    animAction: (View.(valueAnimator:ValueAnimator?) -> Unit)? = null
) {
    // set default bg
    updateViewGradientColorBg(this, 0f, startColor, endColor, radius)

    val valueAnim = ValueAnimator.ofFloat(0f, 1.1f)
    valueAnim.addUpdateListener {
        updateViewGradientColorBg(this, it.animatedValue as Float, startColor, endColor, radius)
    }
    if (infinite) {
        valueAnim.repeatCount = -1
    }
    valueAnim.setDuration(animDuration).apply {
        if (autoStart) {
            start()
        } else {
            animAction?.invoke(this@gradientColorBgAnim,this)
        }
    }
    if (this.context is AppCompatActivity) {
        (this.context as AppCompatActivity).lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    valueAnim?.cancel()
                }
            }
        })
    }
}

internal fun updateViewGradientColorBg(
    view: View,
    animValue: Float,
    startColor: Int,
    endColor: Int,
    radius: Float,
) {
    val colors = intArrayOf(startColor, endColor)
    val offsets = floatArrayOf(animValue - 0.1f, animValue)

    val drawable = GradientDrawable()
    drawable.gradientType = GradientDrawable.LINEAR_GRADIENT
    drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        drawable.setColors(colors, offsets)
    }
    drawable.cornerRadii = floatArrayOf(
        radius, radius,
        radius, radius,
        radius, radius,
        radius, radius
    )
    view.background = drawable
}




