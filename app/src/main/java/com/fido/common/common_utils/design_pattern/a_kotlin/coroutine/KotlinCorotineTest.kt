package com.fido.common.common_utils.design_pattern.a_kotlin.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * @author: FiDo
 * @date: 2025/3/7
 * @des:
 */

//suspend fun main() {
//    val job = GlobalScope.launch { // 启动一个新协程并保持对这个作业的引用
//        delay(1000L)
//        println("World!")
//    }
//    println("Hello,")
//    job.join() // 等待直到子协程执行结束
////    加了job.join()后，程序就会一直等待，直到我们启动的协程结束。注意，这里的等待是非堵塞式的等待，不会将当前线程挂起。
//}


//fun main() = runBlocking {
//    launch { doWorld() }
//    println("Hello,")
//}
//
//// 这是你的第一个挂起函数
//suspend fun doWorld() {
//    delay(1000L)
//    println("World!")
//}

suspend fun main() {

    //如果协程正在执行计算任务，并且没有检查取消的话，那么它是不能被取消的，就如如下示例代码所示：
    val startTime = System.currentTimeMillis()
    val job =  GlobalScope.launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
//        while (isActive) { // 改用isActive 可以被取消的计算循环
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("job: I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个作业并且等待它结束
    println("main: Now I can quit.")
// 执行结果  连续打印出了“I'm sleeping”，甚至在调用取消后， 作业仍然执行了五次循环迭代并运行到了它结束为止。
//    job: I'm sleeping 0 ...
//    job: I'm sleeping 1 ...
//    job: I'm sleeping 2 ...
//    main: I'm tired of waiting!
//    job: I'm sleeping 3 ...
//    job: I'm sleeping 4 ...
//    main: Now I can quit.


    //默认是按顺序执行
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")

    GlobalScope.launch {
        val time2 = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println("The answer2 is ${one.await() + two.await()}")
        }
        println("Completed in $time2 ms")
    }
    delay(2000)


}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // 假设我们在这里做了一些有用的事
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // 假设我们在这里也做了一些有用的事
    return 29
}

