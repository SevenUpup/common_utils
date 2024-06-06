package com.fido.common.common_utils.pop.login_interceptor

import com.fido.common.common_base_util.ext.startActivity
import com.fido.common.common_utils.login.LoginAc

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
class LoginInterceptor:BaseLoginInterceptorImp() {

    override fun interceptor(chain: LoginInterceptorChain) {
        super.interceptor(chain)

        if (!LoginInterceptorChain.isLogin()) {
            startActivity<LoginAc>()
        }else{
            chain.process()
        }
    }

    fun loginFinished(){
        mChain?.process()
    }

}