package com.fido.common.common_base_util.listener

import android.view.View

abstract class BlockQuickClickListener(private val interval:Long = 800L):View.OnClickListener {

    private var lastClickTime: Long = 0

    abstract fun onBlockQuickClick(v: View)

    override fun onClick(v: View) {
        if (isFastClick()) return
        onBlockQuickClick(v)
    }

    private fun isFastClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastClickTime < interval) {
            true
        } else {
            lastClickTime = currentTime
            false
        }
    }

}