package com.fido.common.common_utils.test.reflect


abstract class  AbsReflectTest<T> {

    val map = mutableMapOf("1" to "1","2" to "2")

    var muiltMap = mutableMapOf<Any,Any>()

    fun getMap66() = map.apply {
       put("-6","-6")
    }

    abstract fun getList(string:String):List<T>
}




