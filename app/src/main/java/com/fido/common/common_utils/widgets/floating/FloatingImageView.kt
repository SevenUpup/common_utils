package com.fido.common.common_utils.widgets.floating

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.windowHeight
import com.fido.common.common_base_util.windowWidth
import com.fido.common.common_utils.R
import com.fido.common.common_utils.a
import kotlin.math.abs

/**
 * @author: FiDo
 * @date: 2024/3/4
 * @des:
 */
class FloatingImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var lastX: Int = 0
    private var lastY: Int = 0
    private var downX: Int = 0
    private var downY: Int = 0

    private val windownWidth by lazy { context.windowWidth() }
    private val windownHeight by lazy { context.windowHeight() }
    // 移动变化量
    private var dx: Int = 0
    private var dy: Int = 0


    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            lastX = e?.rawX?.toIntDefult() ?: 0
            lastY = e?.rawY?.toIntDefult() ?: 0
            downX = lastX
            downY = lastY
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            // 记录当前位置
            val rawX = e2?.rawX?.toIntDefult()
            val rawY = e2?.rawY?.toIntDefult()
            //变化量
            dx = (rawX ?: 0) - lastX
            dy = (rawY ?: 0) - lastY
            //视图最新位置
            var left = left + dx
            var right = right + dx
            var top = top + dy
            var bottom = bottom + dy
            //控制上下左右不能超过屏幕
            if (left < 0) {
                left = 0
                right = left + width
            }
            if (right > windownWidth) {
                right = windownWidth
                left = right - width
            }
            if (top < 40) {
                top = 40
                bottom = top + height
            }
            if (bottom > windownHeight) {
                bottom = windownHeight
                top = bottom - height
            }

            //更新当前视图位置
            layout(left,top,right,bottom)
            //更新最后屏幕点信息
            lastX = rawX?:0
            lastY = rawY?:0

            return true
        }

    }

    private val gestureDetector: GestureDetector = GestureDetector(context, gestureListener)

    init {
        setImageResource(R.drawable.ic_basketball)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(50.dp,50.dp)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP->{
                //抬起后的x,y
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                //抬起点和最后一次按下点x、y距离大于视图宽的一半才执行
//                if (abs(x - downX) > width / 2 || abs(y - downY) > width  / 2) {
                //贴边距离判断，抬起后 左/右两边距离屏幕边沿距离
                if (x <= width * 2 || (windownWidth - x) <= width*2 ) {
                    val isRight = x > windownWidth / 2
                    //贴边
                    startAnimator(isRight, windownWidth - width, 0)

                    postDelayed({
                        startAnimator(isRight, windownWidth - width * 2 / 3, -width / 3)
                    },1500)
                }
                return true
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    private fun startAnimator(isRight:Boolean,rightValue:Int,leftValue:Int){
        ValueAnimator.ofInt(
            left,
            if (isRight) rightValue else leftValue
        ).apply {
            addUpdateListener {
                val value = it.animatedValue as Int
                //根据监听值不断改变当前视图位置
                layout(value, top, value + width, bottom)
            }
            interpolator = AccelerateInterpolator()
            duration = 400
            start()
        }
    }

    private fun Float?.toIntDefult(): Int = this?.toInt() ?: 0

}

