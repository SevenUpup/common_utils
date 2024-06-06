package com.fido.common.common_utils.pop.interceptor_by_coroutines

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.ext.loge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:  协程 登陆拦截操作 管理类
 */
class LoginInterceptorCoroutinesManager private constructor() : DefaultLifecycleObserver,
    CoroutineScope by MainScope() {

    companion object {
        private var instacne: LoginInterceptorCoroutinesManager? = null
            get() {
                if (field == null) {
                    field = LoginInterceptorCoroutinesManager()
                }
                return field
            }

        fun get() = instacne!!

        //测试标志用
        var isLogin = false
    }

    private val broadcastChannel = BroadcastChannel<Boolean>(Channel.BUFFERED)

    fun checkLogin(loginAction: () -> Unit, nextAction: () -> Unit) {
        launch {
            if (isLogin) {
                nextAction()
                return@launch
            }

            //未登陆，执行登陆逻辑
            loginAction()

            val receiveChannel = broadcastChannel.openSubscription()

            //协程等待广播接受
            val isLogin = receiveChannel.receive()

            loge("${this@LoginInterceptorCoroutinesManager::class.simpleName}收到消息：" + isLogin)

            if (isLogin) {
                nextAction()
            }
        }
    }

    fun loginFinished(){
        launch {
            loge("${this@LoginInterceptorCoroutinesManager::class.simpleName}发送消息：" + isLogin)
            broadcastChannel.send(isLogin)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        loge("${this::class.simpleName} onDestroy------")
        broadcastChannel.cancel()
        //coroutines cancel
        cancel()
    }

}