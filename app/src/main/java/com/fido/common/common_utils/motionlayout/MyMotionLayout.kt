package com.fido.common.common_utils.motionlayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout

/**
@author FiDo
@description:
@date :2023/6/9 17:45
 */
class MyMotionLayout@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
)  :
    MotionLayout(context, attrs, defStyleAttr) {

    var block:(()->Unit)?=null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(event)
    }

    private var lastAction = -1
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (lastAction == MotionEvent.ACTION_DOWN) {
                block?.invoke()
            }
        } else {
            lastAction = event?.action?:-1
        }
        return super.onTouchEvent(event)
    }

}