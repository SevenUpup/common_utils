package com.fido.common.common_utils.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * @author: FiDo
 * @date: 2025/3/26
 * @des:
 */
suspend fun main(){

    val mainScope = MainScope()

    val time = measureTimeMillis {
        coroutineScope {
            // ========================== 串行 ==============================
            val result = withContext(Dispatchers.IO){
                doSomeWasteTimeThing(100,"step one")
                "step one invoke finish"
            }
            println(result)

            val result2 = withContext(Dispatchers.IO){
                doSomeWasteTimeThing(500,"step two")
                "step two invoke finish"
            }
            println(result2)

            val result111 = async {
                doSomeWasteTimeThing(200, "111")
                "111"
            }.await()
            val result222 = async {
                doSomeWasteTimeThing(200, "222")
                "222"
            }.await()

            println("以下代码将并行执行")
            // ========================= 并行 ==============================
            // 1️⃣ 先启动所有任务（异步并发）
            val deferred1 = async() {
                doSomeWasteTimeThing(400, "step 1")
                "1"
            }
            val deferred2 = async() {
                doSomeWasteTimeThing(500, "step 2")
                "2"
            }
            // 2️⃣ 等待所有任务完成（但任务已经在运行了）
            val r1 = deferred1.await()
            val r2 = deferred2.await()

            println(r1)
            println(r2)
        }
    }
    println("total time=${time}")
}