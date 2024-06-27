package com.fido.common.common_utils.jetpack

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.toast
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:
 */
class SuspendCancellableCoroutineTest private constructor(){

    companion object{
        private var INSTANCE:SuspendCancellableCoroutineTest?=null
            get(){
                if (field == null) {
                    field = SuspendCancellableCoroutineTest()
                }
                return field
            }

        fun getInstance() = INSTANCE!!
    }

    fun suspendCoroutine(lifecycleOwner: LifecycleOwner){
        lifecycleOwner.lifecycleScope.launch {
            getSomeThing(mResult)

            //下面的不会执行的，除非 suspendCancellableCoroutine 的 resume 来恢复协程，才会继续走下去

            logd("处理过后的Result:$mResult")
        }
    }

    private var mResult = "初始数据"

    private var mCancellableContinuation:CancellableContinuation<String>?=null
    private suspend fun getSomeThing(mResult: String):String {
        return suspendCancellableCoroutine<String> { cancellableContinuation ->
            mCancellableContinuation = cancellableContinuation

            toast("开启一个耗时线程，模拟网络耗时,开始处理初始数据：${mResult}")
            thread {
                Thread.sleep(5000)
                this.mResult = "我是模拟网络请求回调结果"
                toast("网络结果我已经返回了，哎，我就是不返回结果，就是玩")
            }
        }
    }

    fun resumeCoroutine(){
        mCancellableContinuation?:return
        toast("点击恢复线程-返回数据")
        if (mCancellableContinuation?.isCancelled == true) {
            return
        }
        val isCompleted = mCancellableContinuation?.isCompleted?:false
        if (!isCompleted) {
            runCatching {
                mCancellableContinuation?.resume(mResult,null)
            }
        }
    }

}