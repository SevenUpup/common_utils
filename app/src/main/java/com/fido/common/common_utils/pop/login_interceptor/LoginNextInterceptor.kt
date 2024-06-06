package com.fido.common.common_utils.pop.login_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
class LoginNextInterceptor(val loginNextAction:()->Unit):BaseLoginInterceptorImp() {

    override fun interceptor(chain: LoginInterceptorChain) {
        super.interceptor(chain)
        if (chain.isLogin()) {
            loginNextAction()
        }
        chain.process()
    }

}