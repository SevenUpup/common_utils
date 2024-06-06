package com.fido.common.common_utils.pop.base_interceptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author: FiDo
 * @date: 2024/6/5
 * @des:
 */
class InterceptorChain private constructor(
    // 弹窗的时候可能需要Activity/Fragment环境
    val activity: FragmentActivity?=null,
    val fragment:Fragment?=null,
    private var interceptors:MutableList<Interceptor>?,
){

    companion object{
        @JvmStatic
        fun creator() = Builder()

    }

    private var index = 0;

    //拦截操作
    fun process(){
        interceptors?:return
        when (index) {
            in interceptors!!.indices ->{
                val interceptor = interceptors!![index]
                index++
                interceptor.interceptor(this)
            }
            interceptors!!.size ->{
                clearAllInterceptors()
            }
        }
    }

    private fun clearAllInterceptors() {
        interceptors?.clear()
        interceptors = null
        index = 0
    }


    //构造者模式
    open class Builder{
        private var activity:FragmentActivity?=null
        private var fragment:Fragment?=null
        private val _interceptor = ArrayList<Interceptor>();

        fun attach(activity: FragmentActivity):Builder{
            this.activity = activity;
            return this
        }

        fun attach(fragment: Fragment):Builder{
            this.fragment = fragment
            return this
        }

        fun addInterceptor(interceptor: Interceptor):Builder{
            _interceptor.add(interceptor)
            return this
        }

        fun build():InterceptorChain{
            return InterceptorChain(activity,fragment,_interceptor)
        }
    }
}