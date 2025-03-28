package com.fido.common.common_utils.coroutine

import com.fido.common.common_base_util.ext.loge
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @author: FiDo
 * @date: 2025/3/27
 * @des:  withContext(NonCancellableTest)  不可取消的
 *
 *  可以使用 withContext 函数和 NonCancellable上下文将相应的代码包装在 withContext(NonCancellable) {...} 代码块中，NonCancellable 就用于创建一个无法取消的协程作用域
 */

fun main() = runBlocking {
    log("start")
    val launchA = launch {
        try {
            repeat(5){
                delay(50)
                log("launchA-$it")
            }
        }catch (e:Exception){
            loge("launchA=${e.message}")
        }finally {
            delay(50)
            log("launchA isCompleted")
        }
    }

    val launchB = launch {
        try {
            repeat(5){
                delay(50)
                log("launchB-$it")
            }
        }finally {
            withContext(NonCancellable){
                delay(50)
                log("launchB isCompleted")
            }
        }
    }
    //延时二百毫秒，保证两个协程都已经被启动了
    delay(200)
    launchA.cancel()
    launchB.cancel()
    log("end")
}

private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")