package com.fido.common.common_utils.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.lifecycleOwner
import com.fido.common.common_base_util.toJson
import com.fido.common.databinding.AcCoroutineTestBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * @author: FiDo
 * @date: 2024/11/7
 * @des:
 */
class CoroutineTestAc:AppCompatActivity() {

    private val binding:AcCoroutineTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            bt.throttleClick {

            }
        }

        lifecycleOwner.lifecycleScope.launch {
            //子job互不影响
            supervisorScope {
                runCatching {
                    val job1 = launch {
                        delay(300)
                        //如果抛出异常，job1停止执行，job2继续执行
                        throw RuntimeException()
                    }
                    val job2 = launch {
                        delay(400)
                        println("supervisorScope job2")
                    }
                    //如果执行cancel,job1，job2均取消
                    cancel()
                }.onFailure {
                    println(it.toJson())
                }
            }

            coroutineScope {
                runCatching {
                    val job1 = launch {
                        delay(500)
                        //如果抛出异常，job1停止执行，job2也会被取消，停止执行
                        throw RuntimeException()
                    }
                    val job2 = launch {
                        delay(1000)
                        println("coroutineScope job2")
                    }
                    //如果执行cancel,job1，job2均取消
//                    cancel()
                }.onFailure {
                    println(it.toJson())
                }
            }
        }

    }

}