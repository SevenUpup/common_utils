package com.fido.common.common_utils.coroutine

import com.fido.common.common_utils.design_pattern.a_kotlin.systemPrinterUtf8
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

/**
 * @author: FiDo
 * @date: 2025/3/26
 * @des:  协程作用域测试
 */

suspend fun main(){
    systemPrinterUtf8()

    /*val time = measureTimeMillis {
        //runningBlock 是常规函数
        runBlocking {
            printCurrentThreadName("runBlocking start")
            doSomeWasteTimeThing(2000,"step one")
            launch {
                doSomeWasteTimeThing(1000,"step three")
            }
            doSomeWasteTimeThing(500,"step two")


            doSomeWasteTimeThing(1000,"step 1")
            launch(Dispatchers.IO) {
                doSomeWasteTimeThing(1000,"step 2")
            }
            doSomeWasteTimeThing(3000,"step 3")
            printCurrentThreadName("runBlocking end")
        }
    }
    println("runBlocking total time = $time")*/


    //coroutineScope 是挂起函数,和 runBlocking 类似都会等待其协程体和所有子协程全部结束
    //区别在于runBlocking 会阻塞当前线程来等待，而coroutineScope 只是挂起，会释放底层线程用于其他用途
    val time2 = measureTimeMillis {
        coroutineScope {
            printCurrentThreadName("coroutineScope start")

            doSomeWasteTimeThing(500,"step one")
            launch() {
                printCurrentThreadName("step three")
                doSomeWasteTimeThing(700,"step three")
            }
            doSomeWasteTimeThing(800,"step two")

            doSomeWasteTimeThing(200,"step 1")
            launch() {
                printCurrentThreadName("step 2")
                doSomeWasteTimeThing(100,"step 2")
            }
            doSomeWasteTimeThing(200,"step 3")

            printCurrentThreadName("coroutineScope end")
        }
    }
    // 500 + 800 + (200 + 200) = 1700左右
    println("coroutineScope total time = $time2")

    val time3 = measureTimeMillis {
        coroutineScope {
            launch {
                doSomeWasteTimeThing(100,"111")
                printCurrentThreadName()
            }
            launch {
                doSomeWasteTimeThing(200,"222")
                printCurrentThreadName()
            }
            launch {
                doSomeWasteTimeThing(200,"333")
                printCurrentThreadName()
            }
            launch {
                doSomeWasteTimeThing(800,"444")
                printCurrentThreadName()
            }
        }
    }
    println("coroutineScope所有子协程用时 total time = $time3")
}

fun printCurrentThreadName(prefix:String = "") = println("$prefix current thread name = ${Thread.currentThread().name}")

suspend fun doSomeWasteTimeThing(delay:Long, msg:String){
    delay(delay)
    println(msg)
}


