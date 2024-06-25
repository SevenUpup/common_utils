package com.fido.common.common_utils.design_pattern.interceptor2

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
//interface InterceptorChain<T> {
//
//    var next:InterceptorChain<T>?
//
//    fun intercept(data:T?){
//        next?.intercept(data)
//    }
//
//}

abstract class InterceptorChain<T>{
    var next:InterceptorChain<T>?=null

    open fun intercept(data:T?){
        next?.intercept(data)
    }
}