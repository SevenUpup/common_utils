package com.fido.common.common_utils.pop.login_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
abstract class BaseLoginInterceptorImp:ILoginInterceptor {

    protected var mChain:LoginInterceptorChain?=null
    override fun interceptor(chain: LoginInterceptorChain) {
        mChain = chain
    }
}