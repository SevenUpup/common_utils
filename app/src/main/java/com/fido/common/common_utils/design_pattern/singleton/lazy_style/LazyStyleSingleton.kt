package com.fido.common.common_utils.design_pattern.singleton.lazy_style

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class LazyStyleSingleton {

    companion object{

        private val INSTANCE:LazyStyleSingleton?=null
            get() = field ?: LazyStyleSingleton()

        fun getInstance() = INSTANCE!!

    }

}