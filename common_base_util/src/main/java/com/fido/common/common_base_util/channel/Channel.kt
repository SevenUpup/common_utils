@file:Suppress("ObjectPropertyName", "EXPERIMENTAL_API_USAGE")

package com.fido.common.common_base_util.channel

import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@PublishedApi
internal var channelFlow = MutableSharedFlow<ChannelEvent<Any>>()
internal val channelScope = ChannelScope()

// ======================= 粘性事件测试 Start =========================
@PublishedApi
internal val channelStickEvent = mutableMapOf<Any, Any>()

fun sendStickEvent(event: Any) {
    channelStickEvent[event::class.hashCode()] = event
}

inline fun <reified T> receiveStickEvent(
    block: T.() -> Unit
) {
    if (channelStickEvent.contains(T::class.hashCode()) && channelStickEvent[T::class.hashCode()] != null) {
        block.invoke(channelStickEvent[T::class.hashCode()] as T)
    }
}

inline fun <reified T> LifecycleOwner.receiveStickEvent(
    lifeEvent: Event = Event.ON_DESTROY,
    noinline block: suspend CoroutineScope.(event:T)->Unit,
): Job{
    return ChannelScope(this,lifeEvent).apply {
        cancelBlock = {
//            loge("receiveStickEvent is cancled -> event is ${T::class.simpleName} was removed")
            channelStickEvent.remove(T::class.hashCode())
        }
    }.launch {
        if (channelStickEvent.contains(T::class.hashCode()) && channelStickEvent[T::class.hashCode()] != null) {
            block(channelStickEvent[T::class.hashCode()] as T)
        }
    }
}

fun sendStickTag(tag: String) {
    channelStickEvent[tag] = tag
}

fun LifecycleOwner.receiveStickTag(
    tag:String,
    lifeEvent: Event = Event.ON_DESTROY,
    block: String.() -> Unit
):Job{
    return ChannelScope(this,lifeEvent).apply {
        cancelBlock = {
//            loge("receiveStickTag is cancled -> tag is $tag was removed")
            channelStickEvent.remove(tag)
        }
    }.launch {
        if (channelStickEvent.contains(tag) && channelStickEvent[tag] != null) {
            block.invoke(channelStickEvent[tag] as? String ?: "")
        }
    }
}

fun receiveStickTag(tag: String, block: String.() -> Unit) {
    if (channelStickEvent.contains(tag) && channelStickEvent[tag] != null) {
        block.invoke(channelStickEvent[tag] as? String ?: "")
    }
}

// ======================= 粘性事件测试 End =========================
/**
 * 发送事件
 * @param event 事件
 * @param tag 标签, 使用默认值空, 则接受者将忽略标签, 仅匹配事件
 */
fun sendEvent(event: Any, tag: String? = null) = channelScope.launch {
    channelFlow.emit(ChannelEvent(event, tag))
}


/**
 * 发送标签
 * @param tag 标签
 */
fun sendTag(tag: String?) = channelScope.launch {
    channelFlow.emit(ChannelEvent(ChannelTag, tag))
}

/**
 * 接收事件
 *
 * @param tags 可接受零个或多个标签, 如果标签为零个则匹配事件对象即可成功接收, 如果为多个则要求至少匹配一个标签才能成功接收到事件
 * @param block 接收到事件后执行函数
 */
inline fun <reified T> LifecycleOwner.receiveEvent(
    vararg tags: String? = emptyArray(),
    lifeEvent: Event = Event.ON_DESTROY,
    noinline block: suspend CoroutineScope.(event: T) -> Unit
): Job {
    return ChannelScope(this, lifeEvent).launch {
        channelFlow.collect {
            if (it.event is T && (tags.isEmpty() || tags.contains(it.tag))) {
                block(it.event)
            }
        }
    }
}

inline fun <reified T> LifecycleOwner.receiveEvent(vararg tags: String? = emptyArray()): Flow<T> {
    return channelFlow.filter { it.event is T && (tags.isEmpty() || tags.contains(it.tag)) }
        .map { it.event as T }
}

/**
 * 将消息延迟到指定生命周期接收, 默认为前台接受消息
 */
inline fun <reified T> LifecycleOwner.receiveEventLive(
    vararg tags: String? = emptyArray(),
    lifeEvent: Event = Event.ON_START,
    noinline block: suspend CoroutineScope.(event: T) -> Unit
): Job {
    return lifecycleScope.launch {
        channelFlow.flowWithLifecycle(lifecycle, lifeEvent.targetState).collect {
            if (it.event is T && (tags.isEmpty() || tags.contains(it.tag))) {
                block(it.event)
            }
        }
    }
}

/**
 * 接收事件, 此事件要求执行[kotlinx.coroutines.cancel]手动注销
 *
 * @param tags 可接受零个或多个标签, 如果标签为零个则匹配事件对象即可成功接收, 如果为多个则要求至少匹配一个标签才能成功接收到事件
 * @param block 接收到事件后执行函数
 */
inline fun <reified T> receiveEventHandler(
    vararg tags: String? = emptyArray(),
    noinline block: suspend CoroutineScope.(event: T) -> Unit
): Job {
    return ChannelScope().launch {
        channelFlow.collect {
            if (it.event is T && (tags.isEmpty() || tags.contains(it.tag))) {
                block(it.event)
            }
        }
    }
}

/**
 * 接收标签, 和[receiveEvent]不同之处在于该函数仅支持标签, 不支持事件+标签
 *
 * @param tags 可接受零个或多个标签, 如果标签为零个则匹配事件对象即可成功接收, 如果为多个则要求至少匹配一个标签才能成功接收到事件
 * @param block 接收到事件后执行函数
 */
fun LifecycleOwner.receiveTag(
    vararg tags: String?,
    lifeEvent: Event = Event.ON_DESTROY,
    block: suspend CoroutineScope.(tag: String) -> Unit
): Job {
    return ChannelScope(this, lifeEvent).launch {
        channelFlow.collect {
            if (it.event is ChannelTag && !it.tag.isNullOrBlank() && tags.contains(it.tag)) {
                block(it.tag)
            }
        }
    }
}

fun LifecycleOwner.receiveTag(vararg tags: String?): Flow<String> {
    return channelFlow.filter {
        it.event is ChannelTag && !it.tag.isNullOrBlank() && tags.contains(
            it.tag
        )
    }
        .map { it.tag!! }
}

/**
 * 将消息延迟到指定生命周期接收, 默认为前台接受消息
 */
fun LifecycleOwner.receiveTagLive(
    vararg tags: String?,
    lifeEvent: Event = Event.ON_START,
    block: suspend CoroutineScope.(tag: String) -> Unit
): Job {
    return lifecycleScope.launch {
        channelFlow.flowWithLifecycle(lifecycle, lifeEvent.targetState).collect {
            if (it.event is ChannelTag && !it.tag.isNullOrBlank() && tags.contains(it.tag)) {
                block(it.tag)
            }
        }
    }
}

/**
 * 接收标签, 和[receiveEvent]不同之处在于该函数仅支持标签, 不支持事件+标签, 此事件要求执行[kotlinx.coroutines.cancel]手动注销
 *
 * @param tags 可接受零个或多个标签, 如果标签为零个则匹配事件对象即可成功接收, 如果为多个则要求至少匹配一个标签才能成功接收到事件
 * @param block 接收到事件后执行函数
 */
fun receiveTagHandler(
    vararg tags: String?,
    block: suspend CoroutineScope.(tag: String) -> Unit
): Job {
    return ChannelScope().launch {
        channelFlow.collect {
            if (it.event is ChannelTag && !it.tag.isNullOrEmpty() && tags.contains(it.tag)) {
                block(it.tag)
            }
        }
    }
}


