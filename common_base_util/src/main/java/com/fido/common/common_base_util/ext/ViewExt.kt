package com.fido.common.common_base_util.ext

import android.animation.Animator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.graphics.*
import android.os.Build
import android.view.*
import androidx.annotation.FloatRange
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.RecyclerView

/**
 * View相关

view.width(100)           // 设置View的宽度为100
view.widthAndHeight(100)  // 改变View的宽度和高度为100
view.animateWidthAndHeight(600,900) //带有动画
view.animateWidth(600,900, action = { //监听动画进度，执行可选操作 }) //带有动画
view.margin(leftMargin = 100)  // 设置View左边距为100
view.click { toast("aa") }    // 设置点击监听，已实现事件节流，350毫秒内只能点击一次
view.longClick {             // 设置长按监听
toast("long click")
true
}
view.gone()
view.visible()
view.invisible()
view.isGone  // 属性
view.isVisible // 属性
view.isInvisible // 属性
view.toggleVisibility() // 切换可见性
view.toBitmap()           // 获取View的截图，支持RecyclerView长列表截图
// 遍历子View
layout.children.forEachIndexed { index, view ->

}
 */

/**
 * 设置View的高度
 */
fun View.height(height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置View高度，限制在min和max范围之内
 * @param h
 * @param min 最小高度
 * @param max 最大高度
 */
fun View.limitHeight(h: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    when {
        h < min -> params.height = min
        h > max -> params.height = max
        else -> params.height = h
    }
    layoutParams = params
    return this
}

/**
 * 设置View的宽度
 */
fun View.width(width: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.width = width
    layoutParams = params
    return this
}

/**
 * 设置View宽度，限制在min和max范围之内
 * @param w
 * @param min 最小宽度
 * @param max 最大宽度
 */
fun View.limitWidth(w: Int, min: Int, max: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    when {
        w < min -> params.width = min
        w > max -> params.width = max
        else -> params.width = w
    }
    layoutParams = params
    return this
}

/**
 * 设置View的宽度和高度
 * @param width 要设置的宽度
 * @param height 要设置的高度
 */
fun View.widthAndHeight(width: Int, height: Int): View {
    val params = layoutParams ?: ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    params.width = width
    params.height = height
    layoutParams = params
    return this
}

/**
 * 设置View的margin
 * @param leftMargin 默认保留原来的
 * @param topMargin 默认是保留原来的
 * @param rightMargin 默认是保留原来的
 * @param bottomMargin 默认是保留原来的
 */
fun View.margin(leftMargin: Int = Int.MAX_VALUE, topMargin: Int = Int.MAX_VALUE, rightMargin: Int = Int.MAX_VALUE, bottomMargin: Int = Int.MAX_VALUE): View {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (leftMargin != Int.MAX_VALUE)
        params.leftMargin = leftMargin
    if (topMargin != Int.MAX_VALUE)
        params.topMargin = topMargin
    if (rightMargin != Int.MAX_VALUE)
        params.rightMargin = rightMargin
    if (bottomMargin != Int.MAX_VALUE)
        params.bottomMargin = bottomMargin
    layoutParams = params
    return this
}

fun View.margin(horizontalMargin:Int = Int.MAX_VALUE,verticalMargin:Int=Int.MAX_VALUE) = apply {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    if (horizontalMargin != Int.MAX_VALUE) {
        params.leftMargin = horizontalMargin
        params.rightMargin = horizontalMargin
    }
    if (verticalMargin != Int.MAX_VALUE) {
        params.topMargin = verticalMargin
        params.bottomMargin = verticalMargin
    }
    layoutParams = params
}

/**
 * 设置宽度，带有过渡动画
 * @param targetValue 目标宽度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidth(targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null,
                      action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                width(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置高度，带有过渡动画
 * @param targetValue 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateHeight(targetValue: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null, action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                height(it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置宽度和高度，带有过渡动画
 * @param targetWidth 目标宽度
 * @param targetHeight 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.animateWidthAndHeight(targetWidth: Int, targetHeight: Int, duration: Long = 400, listener: Animator.AnimatorListener? = null, action: ((Float) -> Unit)? = null) {
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthAndHeight(it.animatedValue as Int, evaluator.evaluate(it.animatedFraction, startHeight, targetHeight))
                action?.invoke((it.animatedFraction))
            }
            if(listener!=null)addListener(listener)
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置点击监听, 并实现事件节流
 */
var _viewClickFlag = false
var _clickRunnable = Runnable { _viewClickFlag = false }
fun View.click(action: (view: View) -> Unit) {
    setOnClickListener {
        if (!_viewClickFlag) {
            _viewClickFlag = true
            action(it)
        }
        removeCallbacks(_clickRunnable)
        postDelayed(_clickRunnable, 350)
    }
}

/**
 * 设置长按监听
 */
fun View.longClick(action: (view: View) -> Boolean) {
    setOnLongClickListener {
        action(it)
    }
}

/*** 可见性相关 ****/
fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

var View.isGone: Boolean
    get() {
        return visibility == View.GONE
    }
    set(value){
        visibility = if (value) View.GONE else View.VISIBLE
    }

var View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value){
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.isInvisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }
    set(value){
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

/**
 * 切换View的可见性
 */
fun View.toggleVisibility() {
    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
}


/**
 * 获取View的截图, 支持获取整个RecyclerView列表的长截图
 * 注意：调用该方法时，请确保View已经测量完毕，如果宽高为0，则将抛出异常
 */
fun View.toBitmap(): Bitmap {
    if (measuredWidth == 0 || measuredHeight == 0) {
        throw RuntimeException("调用该方法时，请确保View已经测量完毕，如果宽高为0，则抛出异常以提醒！")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            val bmp = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)

            //draw default bg, otherwise will be black
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            this.draw(canvas)
            //恢复高度
            this.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
            bmp //return
        }
        else -> {
            val screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(screenshot)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)// 将 view 画到画布上
            screenshot //return
        }
    }
}


// 所有子View
inline val ViewGroup.children
    get() = (0 until childCount).map { getChildAt(it) }

/**
 * 为view添加OnGlobalLayoutListener监听并在测量完成后自动取消监听同时执行[globalAction]函数
 *
 * @param globalAction
 */
inline fun <T : View> T.afterMeasured(crossinline globalAction: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                globalAction()
            }
        }
    })
}

fun evaluateColor(@FloatRange(from = 0.0, to = 1.0) fraction: Float /*0-1*/, startColor: Int, endColor: Int): Int {
    val fr = MathUtils.clamp(fraction, 0f, 1f)
    val startA = startColor shr 24 and 0xff
    val startR = startColor shr 16 and 0xff
    val startG = startColor shr 8 and 0xff
    val startB = startColor and 0xff
    val endA = endColor shr 24 and 0xff
    val endR = endColor shr 16 and 0xff
    val endG = endColor shr 8 and 0xff
    val endB = endColor and 0xff
    return startA + (fr * (endA - startA)).toInt() shl 24 or
            (startR + (fr * (endR - startR)).toInt() shl 16) or
            (startG + (fr * (endG - startG)).toInt() shl 8) or
            startB + (fr * (endB - startB)).toInt()
}


fun View.expandClickArea(expandSize: Float) = expandClickArea(expandSize.toInt())

fun View.expandClickArea(expandSize: Int) =
    expandClickArea(expandSize, expandSize, expandSize, expandSize)

fun View.expandClickArea(top: Float, left: Float, right: Float, bottom: Float) =
    expandClickArea(top.toInt(), left.toInt(), right.toInt(), bottom.toInt())

fun View.expandClickArea(top: Int, left: Int, right: Int, bottom: Int) {
    val parent = parent as? ViewGroup ?: return
    parent.post {
        val rect = Rect()
        getHitRect(rect)
        rect.top -= top
        rect.left -= left
        rect.right += right
        rect.bottom += bottom
        val touchDelegate = parent.touchDelegate
        if (touchDelegate == null || touchDelegate !is MultiTouchDelegate) {
            parent.touchDelegate = MultiTouchDelegate(rect, this)
        } else {
            touchDelegate.put(rect, this)
        }
    }
}

var View.roundCorners: Float
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) {
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, value)
            }
        }
    }

fun View?.isTouchedAt(x: Float, y: Float): Boolean =
    isTouchedAt(x.toInt(), y.toInt())

fun View?.isTouchedAt(x: Int, y: Int): Boolean =
    this?.locationOnScreen?.contains(x, y) == true

fun View.findTouchedChild(x: Float, y: Float): View? =
    findTouchedChild(x.toInt(), y.toInt())

fun View.findTouchedChild(x: Int, y: Int): View? =
    touchables.find { it.isTouchedAt(x, y) }

/**
 * Computes the coordinates of this view on the screen.
 */
inline val View.locationOnScreen: Rect
    get() = IntArray(2).let {
        getLocationOnScreen(it)
        Rect(it[0], it[1], it[0] + width, it[1] + height)
    }

internal class MultiTouchDelegate(bound: Rect, delegateView: View) : TouchDelegate(bound, delegateView) {
    private val map = mutableMapOf<View, Pair<Rect, TouchDelegate>>()
    private var targetDelegate: TouchDelegate? = null

    init {
        put(bound, delegateView)
    }

    fun put(bound: Rect, delegateView: View) {
        map[delegateView] = bound to TouchDelegate(bound, delegateView)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                targetDelegate = map.entries.find { it.value.first.contains(x, y) }?.value?.second
            }
            MotionEvent.ACTION_CANCEL -> {
                targetDelegate = null
            }
        }
        return targetDelegate?.onTouchEvent(event) ?: false
    }
}