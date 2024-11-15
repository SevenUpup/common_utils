package com.fido.common.common_utils.design_pattern.order

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  命令模式
 * 命令模式（Command Pattern）
 * 命令模式 是行为型设计模式之一，它允许将请求（命令）封装为一个对象，从而使你能够使用不同的请求、队列或者日志请求等。该模式使得请求调用者和请求接收者解耦。命令模式通常用于实现请求的队列、请求的日志记录、以及支持撤销（undo）和重做（redo）操作的功能。
 *
 * 命令模式的角色：
 * Command（命令接口）：声明执行请求的接口。
 * ConcreteCommand（具体命令类）：实现命令接口，定义与接收者的绑定关系。调用接收者相应的操作。
 * Client（客户端）：创建一个具体命令对象并设定接收者。
 * Invoker（请求者）：要求命令执行请求。
 * Receiver（接收者）：知道如何实施与执行一个请求相关的操作。任何类可以作为接收者。
 * 示例代码：命令模式的实现
 * 假设有一个简单的遥控器控制多个设备（比如灯和风扇）。
 */

fun main(){

    val remoteControl = RemoteControl()

    val lightCommand = LightOnCommand(Light())
    val fanCommand = FanOnCommand(Fan())

    remoteControl.addCommand(lightCommand)
    remoteControl.addCommand(fanCommand)
    remoteControl.exeCommand()
}

//命令接口
internal interface Command{
    fun execute()
}

//具体命令实现类
internal class LightOnCommand(
    private val light: Light
):Command{
    override fun execute() {
        light.turnOn()
    }
}

internal class FanOnCommand(
    private val fan: Fan
):Command{
    override fun execute() {
        fan.turnOn()
    }
}

internal class Light{
    fun turnOn(){
        println("开灯")
    }
    fun turnOff(){
        println("关灯")
    }
}

internal class Fan{
    fun turnOn(){
        println("打开风扇")
    }
    fun turnOff(){
        println("关闭风扇")
    }
}

internal class RemoteControl{

    private val commands = mutableListOf<Command>()

    fun addCommand(command: Command){
        commands.add(command)
    }

    fun exeCommand(){
        commands.forEach {
            it.execute()
        }
    }
}
