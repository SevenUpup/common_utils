package com.fido.common.common_utils.eventbus.flow_bus

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @author: FiDo
 * @date: 2024/6/24
 * @des:
 */
object FlowBus {

    private val busMap = mutableMapOf<String, EventBus<*>>()
    private val busStickMap = mutableMapOf<String, StickEventBus<*>>()

    @Synchronized
    fun <T> with(key: String): EventBus<T> {
        var eventBus = busMap[key]
        if (eventBus == null) {
            eventBus = EventBus<T>(key)
            busMap[key] = eventBus
        }
        return eventBus as EventBus<T>
    }

    @Synchronized
    fun <T> withStick(key: String): StickEventBus<T> {
        var eventBus = busStickMap[key]
        if (eventBus == null) {
            eventBus = StickEventBus<T>(key)
            busStickMap[key] = eventBus
        }
        return eventBus as StickEventBus<T>
    }

//    inline fun <reified T> post2(scope: CoroutineScope,event:T) {
//        with<T>(T::class.java.name).post(scope,event)
//    }

    /**
     * @param tag 用来标记 key 的额外值，防止key 可能是同一个类，如String,那么key always == java.lang.String
     */
    fun <T> post(scope: CoroutineScope,event:T,tag:Any?=null){
        if (event != null) {
            with<T>(event!!::class.java.name + (tag ?: "")).post(scope, event)
        }
    }

    inline fun <reified T> register(lifecycleOwner: LifecycleOwner,tag: Any?=null, noinline action:(T)->Unit){
        with<T>(T::class.java.name + (tag ?: "")).register(lifecycleOwner,action)
    }

    //真正实现类
    open class EventBus<T>(private val key: String) : LifecycleObserver {

        //私有对象用于发送消息
        private val _events: MutableSharedFlow<T> by lazy {
            obtainEvent()
        }

        //暴露的公有对象用于接收消息
        val events = _events.asSharedFlow()

        open fun obtainEvent(): MutableSharedFlow<T> = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)

        //主线程接收数据
        fun register(lifecycleOwner: LifecycleOwner, action: (t: T) -> Unit) {
            lifecycleOwner.lifecycle.addObserver(this)
            lifecycleOwner.lifecycleScope.launch {
                events.collect {
                    try {
                        action(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        loge("FlowBus - Error:$e")
                    }
                }
            }
        }

        //协程中发送数据
        suspend fun post(event: T) {
            _events.emit(event)
        }

        //主线程发送数据
        fun post(scope: CoroutineScope, event: T) {
            printMap()
            scope.launch {
                _events.emit(event)
            }
        }

        //自动销毁
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            val subscriptCount = _events.subscriptionCount.value
            logd("FlowBus - 自动onDestroy subscriptCount=${subscriptCount}")
            if (subscriptCount <= 0){
                busMap.remove(key)
            }
            printMap()
        }
    }

    private fun printMap(){
        logd("busStickMap = ${busStickMap} busMap = ${busMap}")

    }

    class StickEventBus<T>(key: String) : EventBus<T>(key) {
        override fun obtainEvent(): MutableSharedFlow<T> = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
    }


}