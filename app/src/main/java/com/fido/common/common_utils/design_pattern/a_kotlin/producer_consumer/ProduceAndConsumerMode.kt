package com.fido.common.common_utils.design_pattern.a_kotlin.producer_consumer

import android.util.Log
import com.fido.common.common_utils.design_pattern.a_kotlin.systemPrinterUtf8
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random


/**
 * @author: FiDo
 * @date: 2025/3/11
 * @des:  理解生产者消费者模式及 为什么使用 BlockingQueue
 */

fun main() {

    systemPrinterUtf8()
    println("任务开始了：1111")
    val manager = ImageDownloadManager()
    manager.addTask("111")
    manager.addTask("222")
    manager.addTask("333")

//    manager.stop()
    println("任务结束了？2222")
//    生产者（addTask()） 把 ImageDownloadTask 任务放入 BlockingQueue 中。
//    消费者（startConsumer()） 从 BlockingQueue 取出任务，并用线程池执行 task.download()。
//    如果生产速度大于消费速度，队列会自动缓存任务，防止丢失任务。
//    如果队列满了，生产者会阻塞等待，不会导致 OOM。
//    如果队列空了，消费者会阻塞等待，不会频繁轮询消耗 CPU。


    println("开启 loop")
    loop("123")
    println("结束 loop")
    loop("")
    println("loop 后 执行代码")
    manager.addTask("444")
}

val msgList = mutableListOf<String>()
fun loop(msg:String){
    if (msg.isNotBlank()) {
        msgList.add(msg)
    }
    while (true) {
        if (msg.isNullOrBlank()) { // 替换为你的退出条件
            println("退出循环")
            break
        }else{
            if (msgList.isEmpty()) break
            val index = msgList.indexOf(msg)
            val result = msgList[index]
            println("result=${result}")
            msgList.removeAt(index)
        }
    }
}

/**
 * 1. 创建图片下载任务类
 */
class ImageDownloadTask(private val imageUrl: String) {

    fun download() {
        try {
            Thread.sleep(Random.nextInt(100,3000).toLong()) // 模拟下载耗时
            println("ImageDownloadTask  load success:$imageUrl")
            Log.d("ImageDownloadTask", "load success：$imageUrl")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}

/**
 * 2. 创建任务管理类（生产者-消费者核心逻辑）
 */
class ImageDownloadManager{

    private val taskQueue:BlockingQueue<ImageDownloadTask> = LinkedBlockingQueue()

    private val executorService = Executors.newFixedThreadPool(3)

    private var isRunning = true

    init {

        startConsumer() // 启动消费线程

    }

    fun addTask(imageUrl: String){
        kotlin.runCatching {
            val task = ImageDownloadTask(imageUrl)
            taskQueue.put(task) // 将任务添加到任务队列中
        }
    }

    private fun startConsumer() {
        executorService.execute {
            while (isRunning){
                kotlin.runCatching {
                    val task = taskQueue.take()  // 阻塞等待任务
                    task.download()  // 执行任务
                }
            }
        }
    }

    fun stop(){
        isRunning = false
        executorService.shutdown()
    }

}