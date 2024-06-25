package com.fido.common.common_utils.design_pattern.intercept_chain

import android.content.Context

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  责任链管理器
 */
class InterceptChain private constructor(val context: Context?,private val intercepts:MutableList<Intercept>?){

    companion object{
        @JvmStatic
        fun create():Builder{
            return Builder()
        }
    }

    private var index = 0

    fun process(){
        if (intercepts.isNullOrEmpty()) return
        when(index){
            in intercepts.indices -> {
                val mIntercept = intercepts[index]
                index ++
                mIntercept.intercept(this)
            }
            intercepts.size ->{
                clearIntercept()
            }
        }
    }

    //清空拦截器
    fun clearIntercept() {
        index = 0
        intercepts?.clear()
    }

    class Builder{

        private val _intercept = mutableListOf<Intercept>()
        private var _context:Context?=null

        fun attach(context: Context) = apply {
            _context = context
        }

        fun addIntercept(intercept: Intercept):Builder{
            _intercept.add(intercept)
            return this
        }

        fun build() = InterceptChain(_context,_intercept)

    }


}