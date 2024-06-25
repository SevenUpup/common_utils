package com.fido.common.common_utils.design_pattern.singleton.inner_class_style

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class InnerClazzSingletonKt {

    companion object{
        val instance = Holder.holder
    }

    private object Holder{
        val holder = InnerClazzSingletonKt()
    }

}