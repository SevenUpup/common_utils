package com.fido.common.common_utils.test.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * @author: FiDo
 * @date: 2024/11/12
 * @des:
 */


fun main() {
    val scope = runBlocking {
        val callbackFlow = callbackFlow<String> {

            trySend("66")

            awaitClose {
                println("callbackFlow awaitClose")

            }
        }
        kotlin.runCatching {
            withTimeout(1000){ // 超时 1 秒后自动取消 ,可以触发 awaitClose
                callbackFlow.collect {
                    println("callbackFlow collect $it")
                }
            }
        }.onFailure {
            println(it.message)
        }
    }
    println("end")

}