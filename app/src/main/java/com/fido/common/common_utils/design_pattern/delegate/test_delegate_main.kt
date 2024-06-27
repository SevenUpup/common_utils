package com.fido.common.common_utils.design_pattern.delegate

import com.fido.common.common_utils.design_pattern.delegate.clazz_delegate.UserActionImp
import com.fido.common.common_utils.design_pattern.delegate.clazz_delegate.UserDelegate1
import com.fido.common.common_utils.design_pattern.delegate.clazz_delegate.UserDelegate2Kt
import com.fido.common.common_utils.design_pattern.delegate.clazz_delegate.UserDelegate3Kt
import com.fido.common.common_utils.design_pattern.delegate.property_delegate.PropertyDelegateTest

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:  委托模式-类委托
 */

fun main() {

    println("==========类委托============")
    val actionImp = UserActionImp()

    val delegate1 = UserDelegate1(actionImp)
    delegate1.attack()
    delegate1.defense()

    val delegate2 = UserDelegate2Kt(actionImp)
    delegate2.attack()
    delegate2.defense()

    val delegate3 = UserDelegate3Kt(actionImp)
    delegate3.attack()
    delegate3.defense()
    println("==========类委托============")

    println()
    println("==========属性委托============")
    val propertyDelegateTest = PropertyDelegateTest()
    propertyDelegateTest.apply {
        println("textStr=${textStr}")

        textStr2 = "123"
        println("textStr2=${textStr2}")

        textStr3 = "456"
        println("textStr3=${textStr3}")

    }
    println("==========属性委托============")
    println("==========延迟委托============")
    println(propertyDelegateTest.name)

    println()
    println("==========观察者委托==============")
    propertyDelegateTest.observableValue = "第一次修改"
    propertyDelegateTest.observableValue = "第二次修改"
    propertyDelegateTest.observableValue = "第三次修改"
    
    propertyDelegateTest.apply {
        println("age:$age")
        age = 14
        println("age:$age")
        age = 20
        println("age:$age")
        age = 22
        println("age:$age")
        age = 20
        println("age:$age")
    }
    println("==========观察者委托==============")



}