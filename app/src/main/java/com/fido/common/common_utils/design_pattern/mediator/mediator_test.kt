package com.fido.common.common_utils.design_pattern.mediator

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  中介者模式
 * 中介者模式（Mediator Pattern） 是一种行为型设计模式，旨在减少对象之间的直接通信。它通过引入一个中介者对象来协调多个对象之间的交互，从而解耦对象之间的依赖关系。中介者模式使得对象不再直接与其他对象通信，而是通过中介者来进行交互，这样可以简化对象之间的协作和通信，并且使得系统更加灵活和易于维护。
 *
 * 中介者模式的核心思想：
 * 解耦对象之间的直接通信：各个对象不再直接交互，而是通过中介者对象来实现。中介者将多个对象之间的通信封装起来，减少了它们之间的依赖关系。
 * 集中管理对象间的交互：中介者对象负责管理各个同事对象之间的协作。通过集中管理交互，可以更容易地调整和修改系统中的对象关系。
 * 简化系统设计：通过减少对象间的耦合，中介者模式让系统更加清晰和易于扩展。
 * 结构
 * 中介者模式通常有以下几个角色：
 *
 * Mediator（中介者）：定义一个接口，用于与各个同事对象通信。中介者负责协调所有同事对象的交互。
 * ConcreteMediator（具体中介者）：实现中介者接口，负责实际的协调逻辑，维护同事对象之间的关系。
 * Colleague（同事类）：每个同事对象都知道中介者对象，并通过中介者来与其他同事对象进行交互。每个同事类一般包含一个中介者引用。
 * ConcreteColleague（具体同事类）：实现同事类，定义对象之间的具体交互逻辑。
 * 类图
 * lua
 * 复制代码
 *  +----------------+         +-------------------+
 *  |    Mediator   |         |   ConcreteMediator |
 *  +----------------+         +-------------------+
 *         ^                          |
 *         |                          |
 *  +------------------+          +-------------------+
 *  |  Colleague       |<-------->|   ConcreteColleague|
 *  +------------------+          +-------------------+
 * 示例：聊天室中的中介者模式
 * 假设有一个聊天室的系统，其中多个用户可以发送消息给其他用户。如果没有中介者，用户之间需要直接通信。使用中介者模式，我们可以将所有用户的交互都通过一个聊天室中介者来管理，避免了用户之间的直接耦合。
 */

fun main(){

    val chatRoom = ChatRoom()
    val user1 = User("张三",chatRoom)
    val user2 = User("李四",chatRoom)
    val user3 = User("王五",chatRoom)
    // 将用户添加到聊天室中介者
    chatRoom.addColleague(user1)
    chatRoom.addColleague(user2)
    chatRoom.addColleague(user3)

    // 用户发送消息,这里user 调用sendMessage时会交给Mediator中介者的实现类也就是ChatRoom去处理
    user1.sendMessage("大家好，欢迎来到聊天室！")
    user2.sendMessage("大家好！")
}

//1. 定义 Mediator 接口
internal interface Mediator{
    fun sendMessage(msg:String,colleague: Colleague)
}

internal class ChatRoom:Mediator{
    protected val colleagues = mutableListOf<Colleague>()

    fun addColleague(colleague: Colleague){
        if (!colleagues.contains(colleague)) {
            colleagues.add(colleague)
        }
    }

    override fun sendMessage(msg: String, colleague: Colleague) {
        colleagues.forEach {
            if (colleague != it) { // 自己发送的，自己不接收
                it.receiveMessage(msg)
            }
        }
    }
}

internal abstract class Colleague(
    protected val mediator: Mediator
){
    abstract fun sendMessage(msg:String)
    abstract fun receiveMessage(msg: String)
}

internal class User(
    private val name:String,
    mediator: Mediator
) :Colleague(mediator){
    override fun sendMessage(msg: String) {
        println("$name sendMessage=>$msg")
        mediator.sendMessage(msg,this)
    }

    override fun receiveMessage(msg: String) {
        println("$name receiveMessage=>${msg}")
    }

}