package com.fido.common.common_utils.asm.clickcheck

import android.os.SystemClock
import android.view.View
import com.fido.common.common_base_util.ext.loge

/**
 * @author: FiDo
 * @date: 2024/4/17
 * @des:
 */
object ClickViewMonitor {

    private const val MIN_DURATION = 500L

    private var lastClickTime = 0L

    private var clickIndex = 0

    @JvmStatic
    fun enableClick(view: View):Boolean{
        clickIndex ++
        val currentTime = SystemClock.elapsedRealtime()
        val enable = currentTime - lastClickTime > MIN_DURATION
        if (enable) {
            lastClickTime = currentTime
        }
        loge("onClick $clickIndex , isEnabled : $enable")
        return enable
    }

}