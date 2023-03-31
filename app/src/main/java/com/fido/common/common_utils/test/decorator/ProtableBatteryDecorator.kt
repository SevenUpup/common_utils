package com.fido.common.common_utils.test.decorator

//abstract class ProtableBatteryDecorator(val protableBattery: ProtableBattery):ProtableBattery() {
//    override fun charge() {
//        protableBattery.charge()
//    }
//
//    abstract fun light()
//
//}


abstract class XiaoMiProtableBatteryDecorator(val protableBattery: ProtableBattery):XiaoMiProtableBattery{
    override fun charge() {
        protableBattery.charge()
    }

    abstract fun light()
}

interface XiaoMiProtableBattery{
    fun charge()
}