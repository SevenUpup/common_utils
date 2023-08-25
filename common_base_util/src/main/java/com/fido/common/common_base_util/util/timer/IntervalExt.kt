package com.fido.common.common_base_util.util.timer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.TimeUnit

/**
 * 轮询器,默认在 ON_DESTROY 销毁
 * @param period 每次执行间隔 默认 1s
 * @param unit   时间单位 默认TimeUnit.Senconds
 * @param initialDelay 首次执行间隔时间 默认0
 * @param lifecycleOwner 用于轮询器绑定life
 * @param lifecycleEvent 指定销毁的生命周期，默认在 ON_DESTROY 销毁
 * @param onSubscrible   每次轮询回调该方法
 * @param isOnlyResumed 是否启用resume时start(),pause时pause()
 */
fun Context.interval(
    period: Long = 1L,
    unit: TimeUnit = TimeUnit.SECONDS,
    initialDelay: Long = 0L,
    isOnlyResumed: Boolean = false,
    lifecycleOwner: LifecycleOwner? = null,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    onSubscrible: Interval.(Long) -> Unit
) =
    Interval(period, unit, initialDelay).apply {
        when (this@interval) {
            is AppCompatActivity -> {
                if(isOnlyResumed) this@interval.onlyResumed()
                life(this@interval, lifecycleEvent)
            }
            is Fragment -> {
                if (isOnlyResumed) this@interval.viewLifecycleOwner.onlyResumed()
                life(this@interval, lifecycleEvent)
            }
            else -> {
                if (lifecycleOwner == null) throw IllegalArgumentException("please set lifecycleOwner first prevent memory leak,you can check your context")
                if (isOnlyResumed) lifecycleOwner.onlyResumed()
                life(lifecycleOwner, lifecycleEvent)
            }
        }
        subscribe {
            onSubscrible.invoke(this@apply, it)
        }
        start()
    }

/**
 * 开启一个计时器,最好在Ac或Fm使用
 * start > end 倒计时 反之 正计时
 * @param end   结束时间
 * @param start 开始时间
 * @param period 执行间隔，默认1s
 * @param unit   执行时间单位 默认 TimeUnit.SECONDS
 * @param initialDelay 首次执行间隔时间 默认0
 * @param isOnlyResumed 是否启用resume时start(),pause时pause()
 */
fun Context.countDown(
    end: Long,
    start: Long,
    period: Long = 1L,
    unit: TimeUnit = TimeUnit.SECONDS,
    initialDelay: Long = 0L,
    isOnlyResumed:Boolean = false,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    onFinish: (Interval.(Long) -> Unit)? = null,
    onSubscrible: Interval.(Long) -> Unit,
) =
    Interval(end, period, unit, start, initialDelay).apply {
        when (this@countDown) {
            is AppCompatActivity -> {
                if(isOnlyResumed) this@countDown.onlyResumed()
                life(this@countDown, lifecycleEvent)
            }
            is Fragment -> {
                if (isOnlyResumed) this@countDown.viewLifecycleOwner.onlyResumed()
                life(this@countDown, lifecycleEvent)
            }
            else -> if (isOnlyResumed) throw IllegalArgumentException("isOnlyResumed only allowed with LifecycleOwner,check your context")
        }
        subscribe {
            onSubscrible.invoke(this, it)
        }
        finish {
            onFinish?.invoke(this@apply, it)
        }
        start()
    }