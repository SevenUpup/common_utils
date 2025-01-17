package com.fido.common.common_utils.jetpack.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * @author: FiDo
 * @date: 2025/1/17
 * @des:
 */

//fun main() = runBlocking<Unit> {
//    val job = GlobalScope.launch {
//        var time = 0L
//        try {
//            time = System.currentTimeMillis()
//            println("start time=${time}")
//            delay(1000)
//            println("delay 1000")
//        } catch (e: Exception) {
//            println("e===> used:${System.currentTimeMillis() - time}")
//            e.printStackTrace()
//        }
//    }
//
//    job.cancel(CancellationException("自定义一个用于取消协程的异常"))
//    delay(2000)  // 主线程被 runBlocking 阻塞，程序在 delay(2000) 后才退出
//}

fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        // 第三, 这里的异常是第一个被抛出的异常对象
        println("捕捉的异常: $exception 和被嵌套的异常: ${exception.suppressed.contentToString()}")
    }
    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally { // 当父协程被取消时其所有子协程都被取消, finally被取消之前或者完成任务之后一定会执行
                throw ArithmeticException() // 第二, 再次抛出异常, 异常被聚合
            }
        }
        launch {
            delay(100)
            throw IOException() // 第一, 这里抛出异常将导致父协程被取消
        }
        delay(Long.MAX_VALUE)
    }
    job.join() // 避免GlobalScope作用域没有执行完毕JVM虚拟机就退出
}
