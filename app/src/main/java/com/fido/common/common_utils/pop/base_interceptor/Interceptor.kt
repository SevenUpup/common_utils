package com.fido.common.common_utils.pop.base_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:  定义拦截器接口
 */
interface Interceptor {

    fun interceptor(chain: InterceptorChain)

}