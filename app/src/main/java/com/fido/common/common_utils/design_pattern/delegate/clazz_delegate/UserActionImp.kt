package com.fido.common.common_utils.design_pattern.delegate.clazz_delegate

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:
 */
open class UserActionImp:IUserAction {
    override fun attack() {
        println("默认操作-开始执行攻击")
    }

    override fun defense() {
        println("默认操作-开始执行防御")
    }
}