package com.fido.common.common_base_ui.util

import android.view.View
import java.util.concurrent.TimeUnit

fun View.throttleClick(
    interval: Long = 500,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit,
){
    this.setOnClickListener(ThrottleClickListener(interval, unit, block))
}

internal class ThrottleClickListener(
    private val interval:Long = 500,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private val block: View.()->Unit,
): View.OnClickListener{
    private var lastTime:Long = 0
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v)
        }
    }

}