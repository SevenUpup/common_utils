package com.fido.common.common_utils.design_pattern.delegate.clazz_delegate

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:  java方式实现委托
 */
class UserDelegate1(val userAction: IUserAction) : IUserAction {

    override fun attack() {
        println("UserDelegate1-需要自己实现攻击")
    }

    override fun defense() {
        println("UserDelegate1-需要自己实现防御")
    }

}