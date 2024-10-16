package com.fido.common.common_utils.design_pattern.interceptor2

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  拦截器管理类
 */
class InterceptorHandler<T> {

    private var _firstInterceptor: InterceptorChain<T>? = null

    fun add(interceptorChain: InterceptorChain<T>) {
        if (_firstInterceptor == null) {
            _firstInterceptor = interceptorChain
            return
        }

        var node = _firstInterceptor
        while (true) {
            if (node?.next == null) {
                node?.next = interceptorChain
                break
            }
            node = node.next
        }
    }

    fun intercept(data: T) {
        _firstInterceptor?.intercept(data)
    }

}