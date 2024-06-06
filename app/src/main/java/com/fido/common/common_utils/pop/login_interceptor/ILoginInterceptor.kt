package com.fido.common.common_utils.pop.login_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
interface ILoginInterceptor {

    fun interceptor(chain:LoginInterceptorChain)

}