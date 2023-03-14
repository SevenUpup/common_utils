package com.fido.common.common_utils.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner


object AnimUtils {

    /**
     * 缓慢下坠后弹起动画
     */
    private const val TOTAL_ROUND: Int = 10  // 动画总执行次数
    private const val TIME = 2000            // 动画时长
    private const val BOUNCE_HEIGHT = 300    // 弹起高度
    private var CUR_ROUND = 0                // 当前执行次数
    private var animator: ObjectAnimator? = null

    fun beginBounceAnima(view: View,lifecycleOwner: LifecycleOwner?=null){
        val owner = lifecycleOwner?:view.findViewTreeLifecycleOwner()
        owner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> cancle()
                    Lifecycle.Event.ON_RESUME -> start()
                    Lifecycle.Event.ON_DESTROY -> cancle()
                }
            }
            fun start(){
                beginTransAnimation(view)
            }
            fun cancle(){
                CUR_ROUND = 0
                animator?.removeAllListeners()
                animator?.removeAllUpdateListeners()
                animator?.cancel()
                animator = null
            }
        })
    }

    private fun beginTransAnimation(
        view: View,
    ) {
        val height: Int = if (CUR_ROUND % 2 == 0) { // 向下
            (-BOUNCE_HEIGHT * Math.pow(0.5, (CUR_ROUND / 2).toDouble())).toInt()
        } else { // 向上
            (-BOUNCE_HEIGHT * Math.pow(0.5, ((CUR_ROUND + 1) / 2).toDouble())).toInt()
        }
        var from = 0f
        var to = 0f
        if (CUR_ROUND % 2 == 0) {
            from = height.toFloat()
            to = 0f
        } else {
            from = 0f
            to = height.toFloat()
        }
        animator = ObjectAnimator.ofFloat(view, "translationY", from, to)
        animator?.interpolator = AccelerateInterpolator()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                CUR_ROUND++
                if (CUR_ROUND <= TOTAL_ROUND) {
                    beginTransAnimation(view)
                } else {
                    CUR_ROUND = 0
                    beginTransAnimation(view)
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        if (CUR_ROUND % 2 == 0) {
            animator?.interpolator = AccelerateInterpolator()
            animator?.setDuration((TIME * Math.pow(0.5, (CUR_ROUND / 2).toDouble())).toLong())
                ?.start()
        } else {
            animator?.interpolator = DecelerateInterpolator()
            animator?.setDuration((TIME * Math.pow(0.5, ((CUR_ROUND + 1) / 2).toDouble())).toLong())
                ?.start()
        }

    }

}