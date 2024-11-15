package com.fido.common.common_utils.design_pattern.memento

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  备忘录模式
 */

fun main(){

    //创建一个原始类
    val original = Original("111")
    //创建一个存储类存储备忘录
    val storage = Storage(original.createMemento())
    original.status = "222"
    //恢复原始状态
    original.restoreMemento(storage.memento)
    println(original.status)
}

//创建一个备忘录
class Memento(val status:String){}

//创建一个原始类
class Original(
    var status: String
){

    fun createMemento():Memento{
        return Memento(status)
    }

    fun restoreMemento(memento: Memento){
        status = memento.status
    }

}

//一个存储备忘录的类
internal class Storage(val memento: Memento){}