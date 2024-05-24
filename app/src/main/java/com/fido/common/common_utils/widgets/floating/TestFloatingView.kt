package com.fido.common.common_utils.widgets.floating

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import com.fido.common.common_base_util.dp
import com.fido.common.R
import com.gyf.immersionbar.ktx.statusBarHeight

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des:
 */
class TestFloatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ImageView(context, attrs) {

    init {

        setImageResource(R.drawable.ic_zelda)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("LL", "onMeasure: -------")
        setMeasuredDimension(50.dp,100.dp)
    }

    private var dx = 0f
    private var dy = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = this.x - event.rawX
                dy = this.y - event.rawY

                // event.x 获取当前按下坐标在view中的 x/y 值
                val eventX = event.x
                val eventY = event.y
                Log.d("FiDo", "ACTION_DOWN: this.x=${this.x}  event.rawX = ${event.rawX} dx = ${dx}\n" +
                        "this.y=${this.y}  event.rawY = ${event.rawY} dy = ${dy} eventX=$eventX  eventY=$eventY  statusBarHeight = ${context.statusBarHeight} ")
            }
            MotionEvent.ACTION_MOVE -> {
                this.x = event.rawX + dx
                this.y = event.rawY + dy

                val eventX = event.x
                val eventY = event.y
//                Log.d("FiDo", "ACTION_MOVE: event.rawX=${event.rawX} dx=${dx} this.x=${this.x} \nevent.rawY=${event.rawY}  dy=${dy} this.y=${this.y} eventX=$eventX  eventY=$eventY")
                Log.d("FiDo", "ACTION_MOVE: left =${this.left} right =${this.right} top =${this.top} bottom=${this.bottom}\n  this.x=${this.x} this.y=${this.y}")
            }
        }
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("LL", "onLayout: -------")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("LL", "onDraw: -------")
    }

}