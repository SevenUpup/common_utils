package com.fido.common.common_utils.pop.base_interceptor

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:定义一个拦截器的基类
 */
abstract class BaseInterceptorImp:Interceptor {

    protected var mChain:InterceptorChain?=null
    override fun interceptor(chain: InterceptorChain) {
        mChain = chain;
    }
}