package com.fido.common.common_utils.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author: FiDo
 * @date: 2025/3/27
 * @des:
 *  runBlocking 只有当内部 相同作用域 的协程全部执行完毕后，runBlocking后面的代码才会执行，即runBlocking会阻塞所在线程
 *  此外，runBlocking 只会等待相同作用域的协程完成才会退出，而不会等待 GlobalScope 等其它作用域启动的协程
 *
 */
fun main() {

    GlobalScope.launch(Dispatchers.IO) {
        delay(600)
        log("GlobalScope")
    }
    runBlocking {
        delay(500)
        log("runBlocking")
    }
    //主动休眠两百毫秒，使得和 runBlocking 加起来的延迟时间多于六百毫秒
    Thread.sleep(200)
    log("after sleep")

    log("start")
    runBlocking {
        launch {
            repeat(3) {
                delay(100)
                log("launchA - $it")
            }
        }
        launch {
            repeat(3) {
                delay(100)
                log("launchB - $it")
            }
        }
        GlobalScope.launch {
            repeat(3) {
                delay(120)
                log("GlobalScope - $it")
            }
        }
    }
    log("end")
}



private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")