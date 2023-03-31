package com.fido.common.common_utils.test.decorator

class Mi2ProtableBattery(protableBattery: ProtableBattery):XiaoMiProtableBatteryDecorator(protableBattery) {

    override fun charge() {
        super.charge()
        light()
    }

    override fun light() {
        println("xiaomi2 开启照明")
    }
}