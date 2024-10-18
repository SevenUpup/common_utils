package com.fido.common.common_utils.design_pattern.adapter

/**
 * @author: FiDo
 * @date: 2024/10/17
 * @des:  适配器模式：主要是将适配的类的Api转换成目标Api
 */

fun main(){
    val target:TargetApi = Adapter()
    target.request()

    println("======================电压适配=====================")
    println("======================类适配=====================")
    val targetPower = Adapter220V()
    val importedMachine = ImportedMachine()
    targetPower.convert110V()
    importedMachine.work()
    println("======================对象适配=====================")
    val powerPort:IPowerPort = PowerPortAdapter(NormalPowerPortAdaptee())
    val importedTv = ImportedTv()
    powerPort.convertVoltage110V()
    importedTv.work()
}

// 目标Api
interface TargetApi{
    fun request()
}

// 源类
open class Adaptee{
    fun specificRequest(){}
}

//创建适配器类 继承自Adaptee，同时又实现了目标(Target)接口。
class Adapter :Adaptee(),TargetApi{

    //目标接口要求调用Request()这个方法名，但源类Adaptee没有方法Request()
    //因此适配器补充上这个方法名
    //但实际上Request()只是调用源类Adaptee的SpecificRequest()方法的内容
    //所以适配器只是将SpecificRequest()方法作了一层封装，封装成Target可以调用的Request()而已
    override fun request() {
        specificRequest()
    }

}

//类的适配器模式
//======================================== 部分电子产品需要110V电压，国内为220V 适配一下 =============================
interface TargetPower{
    public fun convert110V()
}

open class PowerPort220V{
    //输出220V
    fun output220V(){
        println("正在输出220V电压")
    }
}

class Adapter220V :PowerPort220V(),TargetPower{
    override fun convert110V() {
        this.output220V()
        println("已将220V电压转换为110V")
    }
}

//进口机器类只支持110V
class ImportedMachine{

    fun work(){
        println("进口机器正常运行")
    }

}


// ==============================  对象的适配器模式 ============================
//与类的适配器模式不同的是，对象的适配器模式不是使用继承关系连接到Adaptee类，而是使用委派关系连接到Adaptee类
class NormalPowerPortAdaptee{
    fun outputVoltage(value: Int = 220) {
        println("输出${value}V电压")
    }
}

interface IPowerPort{
    fun convertVoltage110V()
}

class PowerPortAdapter(
    val adaptee:NormalPowerPortAdaptee
):IPowerPort{
    override fun convertVoltage110V() {
        adaptee.outputVoltage()
        println("转换后输出110V")
    }
}

class ImportedTv{
    fun work(){
        println("进口电视开始工作")
    }
}