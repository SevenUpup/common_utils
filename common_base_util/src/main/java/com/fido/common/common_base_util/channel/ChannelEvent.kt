package com.fido.common.common_base_util.channel

/**
 * Channel承载事件的模型
 */
@PublishedApi
internal class ChannelEvent<T>(val event: T, val tag: String? = null)