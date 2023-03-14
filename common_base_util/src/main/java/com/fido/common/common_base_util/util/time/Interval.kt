package com.fido.common.common_base_util.util.time

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.runMain
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import java.io.Closeable
import java.io.Serializable
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * 创建一个轮询器
 *
 * 操作
 * 1. 开启 [start] 只有在闲置状态下才可以开始
 * 2. 停止 [stop]
 * 3. 暂停 [pause]
 * 4. 继续 [resume]
 * 5. 重置 [reset] 重置不会导致轮询器停止
 * 6. 开关 [switch] 开启|暂停切换
 * 7. 生命周期 [life]
 *
 * 函数回调: 允许多次订阅同一个轮询器
 * 1. 每个事件 [subscribe]
 * 2. 停止或者结束 [finish]
 *
 * @param end 结束值, -1 表示永远不结束
 * @param period 计时器间隔
 * @param unit 计时器单位
 * @param initialDelay 第一次事件的间隔时间, 默认0
 * @param start 开始值, 当[start]]比[end]值大, 且end不等于-1时, 即为倒计时, 反之正计时
 */
open class Interval @JvmOverloads constructor(
    private val end: Long,
    private val period: Long,
    private val unit: TimeUnit,
    private val start: Long = 0,
    private val initialDelay: Long = 0

) : Serializable, Closeable {

    @JvmOverloads
    constructor(
        period: Long,
        unit: TimeUnit,
        initialDelay: Long
    ) : this(-1, period, unit, 0, initialDelay)

    var count = start
        private set

    /**轮询器当前状态*/
    private var state = IntervalStatus.STATE_IDELE

    private val subscribeList: MutableList<Interval.(Long) -> Unit> = mutableListOf()
    private val finishList: MutableList<Interval.(Long) -> Unit> = mutableListOf()

    private var countTime = 0L
    private var delay = 0L
    private var scope: CoroutineScope? = null

    /**
     * 订阅定时器
     * 每次执行轮询会回调该函数
     *
     */
    fun subscribe(block: Interval.(Long) -> Unit) = apply {
        subscribeList.add(block)
    }

    /**
     * 定时器结束时会回调该函数
     */
    fun finish(block: Interval.(Long) -> Unit) = apply {
        finishList.add(block)
    }

    /**
     * 开始
     */
    fun start() = apply {
        if (state == IntervalStatus.STATE_ACTICE) return this
        state = IntervalStatus.STATE_ACTICE
        count = start
        launch()
    }

    /**
     * 停止-会执行finish 函数
     */
    fun stop() {
        if (state == IntervalStatus.STATE_IDELE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDELE
        finishList.forEach {
            it.invoke(this, count)
        }
    }

    /**
     * 取消
     * 区别于 stop() 不会执行finish函数
     */
    fun cancle() {
        if (state == IntervalStatus.STATE_IDELE) return
        scope?.cancel()
        state = IntervalStatus.STATE_IDELE
    }

    /** 等于[cancel] */
    override fun close() = cancle()

    fun switch() {
        when (state) {
            IntervalStatus.STATE_ACTICE -> pause()
            IntervalStatus.STATE_PAUSE -> resume()
            IntervalStatus.STATE_IDELE -> start()
        }
    }

    fun pause() {
        if (state == IntervalStatus.STATE_PAUSE) return
        scope?.cancel()
        state = IntervalStatus.STATE_PAUSE
        delay = abs(System.currentTimeMillis() - countTime)
    }

    fun resume() {
        if (state == IntervalStatus.STATE_ACTICE) return
        state = IntervalStatus.STATE_ACTICE
        launch(delay)
    }

    /**
     * 重置
     */
    fun reset(){
        count = start
        delay = unit.toMillis(initialDelay)
        scope?.cancel()
        if(state == IntervalStatus.STATE_ACTICE) launch()
    }


    /**
     * 自动在指定生命周期[cancel]轮询器
     */
    @JvmOverloads
    fun life(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Event = Event.ON_DESTROY
    ) = apply {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object :LifecycleEventObserver{
                override fun onStateChanged(source: LifecycleOwner, event: Event) {
                    if(lifeEvent == event) cancle()
                }
            })
        }
    }

    @JvmOverloads
    fun life(fragment: Fragment,lifeEvent: Event = Event.ON_DESTROY):Interval{
        fragment.viewLifecycleOwnerLiveData.observe(fragment){
            it?.lifecycle?.addObserver(object :LifecycleEventObserver{
                override fun onStateChanged(source: LifecycleOwner, event: Event) {
                    if (lifeEvent == event) cancle()
                }
            })
        }
        return this
    }

    fun LifecycleOwner.onlyResumed() = apply {
        runMain {
            lifecycle.addObserver(object :LifecycleEventObserver{
                override fun onStateChanged(source: LifecycleOwner, event: Event) {
                    if (event == Event.ON_RESUME) {
                        resume()
                    } else if (event == Event.ON_PAUSE) {
                        pause()
                    }
                }

            })
        }
    }

    /** 启动轮询器 */
    @OptIn(ObsoleteCoroutinesApi::class)
    private fun launch(deley: Long = unit.toMillis(initialDelay)) {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            val ticker = ticker(unit.toMillis(period), deley, mode = TickerMode.FIXED_DELAY)
            for (unit in ticker) {
                subscribeList.forEach {
                    it.invoke(this@Interval, count)
                }
                if (end != -1L && count == end) {
                    scope?.cancel()
                    state = IntervalStatus.STATE_IDELE
                    finishList.forEach {
                        it.invoke(this@Interval, count)
                    }
                }
                if (end != -1L && start > end) count-- else count++
                countTime = System.currentTimeMillis()
            }

        }
    }


}