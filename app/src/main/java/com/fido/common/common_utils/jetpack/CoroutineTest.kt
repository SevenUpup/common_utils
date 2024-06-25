package com.fido.common.common_utils.jetpack

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fido.common.common_base_ui.ext.imageview.downloadImageWithGlide
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.io.File
import kotlin.concurrent.thread
import kotlin.coroutines.resume

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class CoroutineTest private constructor():DefaultLifecycleObserver{

    companion object{

        private val INSTANCE:CoroutineTest?=null
            get() = field?:CoroutineTest()

        fun getInstance() = INSTANCE!!

    }


    fun startCoroutineWithTimeout(lifecycleOwner: LifecycleOwner,timeoutMills:Long = 15*1000){
        lifecycleOwner.lifecycle.addObserver(this)
        lifecycleOwner.lifecycleScope.launch {
            try {
                val result = withTimeout(timeoutMills){
                    doTimeoutThing(timeoutMills,this)
                }
                logd("startCoroutineWithTimeout result=${result}")
                toast(result.toString())
            }catch (e:TimeoutCancellationException){
                loge(e.message.toString())
                toast("执行结果超时：${e.message}")
            }catch (e:Exception){
                loge("执行结果错误：${e.message}")
                toast("错误：${e.message}")
            }
        }
    }

    fun downloadImgMaybeTimeout(lifecycleOwner: LifecycleOwner,imgUrl:String,timeoutMills: Long = 15*1000,success:(File)->Unit){
        lifecycleOwner.lifecycle.addObserver(this)
        lifecycleOwner.lifecycleScope.launch{
            runCatching {
                val file = withTimeout(timeoutMills){
                    downloadImageWithGlide(app,imgUrl)
                }
                logd("load img success file = ${file.absolutePath}")
                toast(file.absolutePath)
                success(file)
            }.onFailure {
                if (it is TimeoutCancellationException) {
                    toast("下载超时，执行时间只有${timeoutMills}毫秒，太短啦！图片还没下载完")
                }
                loge(it.toString())
            }
        }
    }

    private suspend fun doTimeoutThing(timeoutMills: Long, coroutineScope: CoroutineScope):Any {
        return suspendCancellableCoroutine<String> {cancellableContinuation->
            coroutineScope.launch {
                delay(timeoutMills + 1000)
                cancellableContinuation.resume("123")
            }
        }
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        loge(this::class.java.simpleName + "onDestroy LifecycleOwner = $owner ")
        Glide.get(app).clearMemory()
        thread {
            Glide.get(app).apply {
                clearDiskCache()
            }
        }
    }

}