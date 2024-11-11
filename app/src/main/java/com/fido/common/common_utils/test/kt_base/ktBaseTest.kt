package com.fido.common.common_utils.test.kt_base

/**
 * @author: FiDo
 * @date: 2024/11/8
 * @des:
 */

private open class User{
    var name = ""
}

private class Stu: User() {
    var id=""
}

fun main(){

    //run返回lambda 最后一行
    val user1 = User()
    val result = user1.run {
        name = "11"
        this
    }
    println("user1 name=${user1.name} result=${result}")

    //返回调用者本身
    val user2 = User()
    user2.apply {
        name = "22"
    }
    println("user2 name=${user2.name}")

    //also (与apply区别 it指代当前对象) 是把自己以参数的形式回调出去给 block 使用，并且返回调用者本身
    val user3 = User()
    user3.also {
        it.name = "33"
    }
    println("user3 name=${user3.name}")
    //let 返回lambda 最后一行 it指代当前对象
    val user4 = User()
    user4.let {
        it.name = "44"
    }
    println("user4 name=${user4.name}")

    //with  返回闭包，接收一个对象，返回一个闭包，其中闭包中用this代表该对象
    val user5 = User()
    val result2 = with(user5){
        name = "55"
    }
    println("user5 name=${user5.name} result=>${result2}")


}