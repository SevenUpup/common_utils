package com.fido.common.common_utils.coroutine

import com.fido.common.common_utils.design_pattern.a_kotlin.systemPrinterUtf8
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

/**
 * @author: FiDo
 * @date: 2025/3/27
 * @des:  supervisorScope 特定：抛出异常不会连锁取消同级协程和父协程
 */

suspend fun main() {

    systemPrinterUtf8()

    runBlocking {
        launch(Dispatchers.Unconfined) {  //不受限制的,根据当前调度器
            log("Dispatchers.Unconfined ")
            delay(100)
            log("Task from runBlocking")
        }
        supervisorScope {
            launch {
                delay(100)
                println("11111")
                throw RuntimeException("故意的,测试supervisorScope是否会导致同级或父协程取消")
            }
            launch {
                delay(200)
                println("222222")
            }
        }

    }
    log("Coroutine scope is over")
}


//fun main() = runBlocking<Unit>(CoroutineName("RunBlocking")) {
//    log("start")
//    launch(CoroutineName("MainCoroutine")) {
//        launch(CoroutineName("Coroutine#A")) {
//            delay(400)
//            log("launch A")
//        }
//        launch(CoroutineName("Coroutine#B")) {
//            delay(300)
//            log("launch B")
//        }
//    }
//}

private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")