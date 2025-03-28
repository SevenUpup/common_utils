package com.fido.common.common_utils.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author: FiDo
 * @date: 2025/3/26
 * @des:  简单测试协程的join()方法，会等待协程中的方法执行完毕后在执行之后的方法
 */

//suspend fun main(){
//    val job = GlobalScope.launch {
//        delay(1000)
//        print(",World!")
//    }
//    print("Hello")
//    job.join()  // 加了job.join()后，程序就会一直等待，直到我们启动的协程结束。注意，这里的等待是非堵塞式的等待，不会将当前线程挂起。
//
//    println("after join")  // print => Hello ,World!  after join
//}

fun main() = runBlocking {
    println("runBlocking => ${Thread.currentThread().name}")
    launch() {
        println("\nlaunch=> ${Thread.currentThread().name}")
        delay(1000)
        print(",World")
    }
    print("Hello")
}