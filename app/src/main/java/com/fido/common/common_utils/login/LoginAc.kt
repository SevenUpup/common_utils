package com.fido.common.common_utils.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.showLoading
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_utils.pop.interceptor_by_coroutines.LoginInterceptorCoroutinesManager
import com.fido.common.common_utils.pop.login_interceptor.LoginInterceptorChain
import com.fido.common.databinding.AcLoginBinding

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
class LoginAc:AppCompatActivity() {

    private val binding:AcLoginBinding  by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            btLogin.throttleClick{
                showLoading("正在登陆...").delayDismissWith(1000){
                    //模拟保存token
                    LoginInterceptorChain.LOGIN_INTERCEPTOR_IS_LOGIN = "123456"
                    //会触发下一个拦截器
                    LoginInterceptorChain.loginFinished()

                    //协程方式的登陆拦截：在登陆成功后更改登陆状态(发送BroadCastChannel通知协程恢复)
                    LoginInterceptorCoroutinesManager.isLogin = true
                    LoginInterceptorCoroutinesManager.get().loginFinished()

                    finish()
                }
            }
        }
    }

}