package com.fido.common.common_utils.coroutine.select

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.div
import kotlin.time.Duration.Companion.seconds

/**
 * @author: HuTao
 * @date: 2026/7/27
 * @des:
 */

//物流公司
data class QuoteProvider(
    //名称
    val name: String,
    //模拟接口获取信息的延迟时间
    val delayTime: Long,
    //价格
    val price: Float?=null,
)

suspend fun main() {

    //1. 5s 后 返回所有快递价格,但是用户等待可能较久
    loadAllQuote()
    //2. 添加超时处理，超过3s后的数据
    println("请求数据，并添加3s的超时处理")
    val timeoutValue = loadQuoteWithTimeout()
    println("请求结束，已超时，获取数据结果:$timeoutValue ${if (timeoutValue.isEmpty()) "loadAllQuote整个代码块被取消,所以无数据" else "有内鬼，检查超时时间设置" }")

    
}

private suspend fun loadQuoteWithTimeout() = coroutineScope {
    withTimeoutOrNull(3.seconds){
        loadAllQuote()
    }.orEmpty()
}

val quoteProviders = listOf(
    QuoteProvider("顺丰", 600, 16f),
    QuoteProvider("圆通", 2000, null),
    QuoteProvider("EMS", 5000, 12f)
)
private suspend fun loadAllQuote() = coroutineScope {
    val startTime = System.currentTimeMillis()
    println("start currentThreadName: ${Thread.currentThread().name}")
    val map = quoteProviders.map {
        async {
            it.name to getQuoteMockPrice(it)
        }
    }
        .awaitAll()
        .mapNotNull { (name, price) ->
            price?.let {
                name to it
            }
        }.toMap()
    println("map: $map")
    println("is end --> used: ${(System.currentTimeMillis() - startTime)/1000f}s currentThreadName: ${Thread.currentThread().name}")
    return@coroutineScope map
}

suspend fun getQuoteMockPrice(quoteProvider: QuoteProvider): Float? {
    delay(quoteProvider.delayTime)
    return quoteProvider.price
}