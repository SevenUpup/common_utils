package com.fido.common.common_base_util.util.anim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.os.HandlerThread
import android.view.ViewPropertyAnimator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.ext.loge

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:
 */
class AnimLifecycleUtils private constructor():DefaultLifecycleObserver {

    private var mHandlerThread:HandlerThread? = HandlerThread("anim_run_in_thread")

    private var mHandler: Handler? = mHandlerThread?.run {
        start()
        Handler(this.looper)
    }

    private var mOwner: LifecycleOwner? = null
    private var mAnim: ViewPropertyAnimator? = null

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AnimUtil()
        }
    }

    //启动动画
    fun startAnim(owner: LifecycleOwner, animator: ViewPropertyAnimator) {
        runCatching {
            if (mOwner != owner) {
                mOwner = owner
                addLoopLifeCycle()
            }
            if (mHandlerThread?.isAlive != true) {
                loge("handlerThread restart")
                mHandlerThread = HandlerThread("anim_run_in_thread")
                mHandler = mHandlerThread?.run {
                    start()
                    Handler(this.looper)
                }
            }

            mHandler?.post {
                mAnim = animator.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        destory()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        destory()
                    }

                    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                        super.onAnimationEnd(animation, isReverse)
                        destory()
                    }
                })
                mAnim?.start()
            }
        }
    }

    private fun addLoopLifeCycle() {
        mOwner?.lifecycle?.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mAnim?.cancel()
        destory()
    }

    private fun destory() {
        kotlin.runCatching {
            mAnim?.cancel()

            mHandlerThread?.quitSafely()

            mAnim = null
            mOwner = null
            mHandler = null
            mHandlerThread = null
        }
    }

}
