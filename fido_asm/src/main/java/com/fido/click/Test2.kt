package com.fido.click

/**
 * @author: FiDo
 * @date: 2024/4/8
 * @des:
 */
class Test2{

    fun sumN(a:Int,b:Int):Int{
        val list = ArrayList<Int>()
        list.add(a)
        list.add(b)

        RecordUtils.onEnter(list)
        val c = a+b
        RecordUtils.onExit(list)

        return c
    }

}

object RecordUtils{
    fun onEnter(list:ArrayList<Int>){
        list.add(111)
    }

    fun onExit(list:ArrayList<Int>){
        list.add(222)
    }

}