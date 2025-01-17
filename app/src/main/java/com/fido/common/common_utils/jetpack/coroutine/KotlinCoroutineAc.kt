package com.fido.common.common_utils.jetpack.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.databinding.AcKotlinCoroutineBinding
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @author: FiDo
 * @date: 2025/1/17
 * @des:  测试协程特性
 */
class KotlinCoroutineAc:AppCompatActivity() {

    private val binding:AcKotlinCoroutineBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

        }
    }


}


/**
 * 1.当子作用域内包含没有终止的任务, 将等待任务完成后才会取消(delay不存在, Thread.sleep可以模拟未结束的任务)
 * 2.抛出CancellationException视作结束异常, invokeOnCompletion也会执行(其中包含异常对象), 但是其他异常将不会执行invokeOnCompletion
 * 3.通过withContext和NonCancellable可以在已被取消的协程中继续挂起协程; 这种用法其实可以看做创建一个无法取消的任务
 */
fun main() = runBlocking {
    launch {
        System.err.println("launch start     ")
        delay(1000)
        System.err.println("(Main.kt:19)    ")
    }
    launch {
        withContext(NonCancellable) {
            System.err.println("withContext start     ")
            delay(2000)
            System.err.println("(Main.kt:26)    ")
        }
    }

    delay(500) // 防止launch还未开启withContext就被取消
    cancel()
}

/**
 * 打印结果分析：
 * launch start
 * withContext start
 * (Main.kt:26)
 * Exception in thread "main" kotlinx.coroutines.JobCancellationException: BlockingCoroutine was cancelled; job=BlockingCoroutine{Cancelled}@4a87761d
 */




