package com.fido.common.common_utils.test.decorator

class MiProtableBattery:PortableBattery() {

    override fun charge() {
        println("给小米手机充电")
    }

}