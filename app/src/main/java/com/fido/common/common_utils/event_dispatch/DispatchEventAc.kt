package com.fido.common.common_utils.event_dispatch

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcDispatchEventBinding

/**
 * @author: FiDo
 * @date: 2025/4/10
 * @des:
 */
class DispatchEventAc : AppCompatActivity() {

    private val binding: AcDispatchEventBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }


    private fun initView() {
        binding.apply {

            tvDes.text = "mTv.isClickable=${mTv.isClickable} mBt.isClickable=${mBt.isClickable} mIv.isClickable=${mIv.isClickable}" +
                    "\nView的onTouchEvent默认都会消费事件，返回true，除非它是不可点击的(clickable和longclickable都为false)，View的longClickable默认都是false，clickable对于Button等为true，而TextView等为false。" +
                    "\n如果事件从上往下一直传递到最底层子View,但是该View没有消费事件(不是clickable或 longclickable ),那么该事件会反序往上传递(从该View传递给自己的ViewGroup，然后再传给更上层的ViewGroup直至传递给Activity.onTouchEvent()). (冒泡式向上处理)."
            btSetClick.setOnClickListener {
                mBt.setOnClickListener {
                    toast("button 被点击了")
                }

                mTv.setOnClickListener {
                    toast("Text 被点击了")
                }

                mIv.setOnClickListener {
                    toast("Image 被点击了")
                }
            }

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        loge("Activity onTouchEvent event:${event?.action}")
        return super.onTouchEvent(event)
    }


}