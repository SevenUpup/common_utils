package com.fido.common.common_utils.coroutine

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * @author: FiDo
 * @date: 2025/3/27
 * @des:
 */

suspend fun main(){

    val channel = Channel<Int>()

    coroutineScope {
        launch {
            // 这里可能是消耗大量 CPU 运算的异步逻辑，我们将仅仅做 5 次整数的平方并发送
            for (x in 1..5) channel.send(x * x)
        }
        // 这里我们打印了 5 次被接收的整数：
        repeat(3) { println(channel.receive()) }
    }

    println("Done!")
}