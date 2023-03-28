package com.fido.common.common_base_ui.util.binding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * 设置控件的隐藏与显示
 */
@BindingAdapter("isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * 点击事件防抖动的点击
 */
@BindingAdapter("click")
fun click(view: View, action: () -> Unit) {
    view.click { action() }
}

/**
 * 设置点击监听, 并实现事件节流
 */
internal fun View.click(action: (view: View) -> Unit) {
    setOnClickListener {
        if (!_viewClickFlag) {
            _viewClickFlag = true
            action(it)
        }
        removeCallbacks(_clickRunnable)
        postDelayed(_clickRunnable, 350)
    }
}