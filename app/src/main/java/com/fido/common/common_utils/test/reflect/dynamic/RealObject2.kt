package com.fido.common.common_utils.test.reflect.dynamic

class RealObject2 :IOperation2{

    override fun operation(msg: String) {
        println("operation2 is Real object $msg")
    }
}