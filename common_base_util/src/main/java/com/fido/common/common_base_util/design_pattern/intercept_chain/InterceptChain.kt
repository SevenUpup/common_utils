package com.fido.common.common_base_util.design_pattern.intercept_chain

/**
 * @author: FiDo
 * @date: 2024/11/22
 * @des:
 */
abstract class InterceptChain<T> {

    var next:InterceptChain<T>?=null

    fun intercept(t:T){
        next?.intercept(t)
    }
}

/**
 * 拦截器管理类
 * 使用示例：[InterceptorHandler<T>().add(...).add(...).intercept()]
 * @param T 用于定义拦截器内部判断是否放行，各个子拦截器可以更改其内部参数以改变之后拦截器的操作
 * 参考    [https://github.com/SevenUpup/common_utils/blob/master/app/src/main/java/com/fido/common/common_utils/design_pattern/DesignPatternAc.kt]
 */
class InterceptorHandler<T>{

    private var _firstChain:InterceptChain<T>?=null

    fun add(interceptChain: InterceptChain<T>) = apply {
        if (_firstChain == null) {
            _firstChain = interceptChain
            return@apply
        }
        var node = _firstChain
        while (true) {
            if (node?.next == null) {
                node?.next = interceptChain
                break
            }
            node = _firstChain?.next
        }
    }

    /**
     * 放行操作
     */
    fun intercept(t:T){
        _firstChain?.intercept(t)
    }

}