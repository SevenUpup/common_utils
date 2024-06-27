package com.fido.common.common_utils.design_pattern.delegate.clazz_delegate

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:
 */


//kotlin方式实现委托
class UserDelegate2Kt(private val userAction: IUserAction):IUserAction by userAction {
}

//如果 Kotlin 的实现不想默认的实现也可以重写部分的操作：
class UserDelegate3Kt(private val userAction: IUserAction):IUserAction by userAction{

    override fun attack() {
        println("UserDelegate3Kt - 只重写了攻击")
    }

}


