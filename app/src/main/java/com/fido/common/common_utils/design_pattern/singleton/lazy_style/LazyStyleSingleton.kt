package com.fido.common.common_utils.design_pattern.singleton.lazy_style

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class LazyStyleSingleton {

    companion object{

        private var INSTANCE:LazyStyleSingleton?=null
            get(){
                if (field == null) {
                    field = LazyStyleSingleton()
                }
                return field
            }

        fun getInstance() = INSTANCE!!

    }

}