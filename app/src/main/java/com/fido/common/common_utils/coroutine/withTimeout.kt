package com.fido.common.common_utils.coroutine

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

/**
 * @author: FiDo
 * @date: 2025/3/27
 * @des:
 */
fun main() = runBlocking {
    println("start")
    //withTimeout 函数用于指定协程的运行超时时间，如果超时则会抛出 TimeoutCancellationException，从而令协程结束运行
    /*val value = withTimeout(200){
        try {
            repeat(5){
                delay(50)
                println("$it")
            }
            200
        }catch (e:Exception){
            println(e.message)
            666
        }
    }
    println(value)
    println("end")*/


    try {
        val v2 = withTimeout(500){
//            delay(600)
//            20
            suspendCancellableCoroutine<Int> { cancelableCoroutine->
                this.launch {
                    delay(600)
                    cancelableCoroutine.resume(20)
                }
            }
        }
        println("v2 = $v2")
    }catch (e:Exception){
        if (e is TimeoutCancellationException) {
            println("invoke timeout=>${e.message}")
        }
    }

}