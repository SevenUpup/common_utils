package com.fido.common.common_utils.design_pattern.iterator

import java.util.Arrays

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  迭代器
 */
internal fun main(){

    val mList = MyList()
    val iterator = mList.getIterator()

    mList.add("小紫")
    mList.remove("张三")

    while (iterator.hasNext()) {
        val next = iterator.next()
        println("next=>$next")
    }
}

internal interface Iterator{
    fun hasNext():Boolean
    fun next():Any?
}

internal interface Collection{
    fun getIterator():Iterator
}

internal class MyList:Collection{
    private var nameArr = arrayOf("张三","李四","王五")
    private var index = 0

    fun add(value:Any){
        nameArr = nameArr.plus(value.toString())
    }

    fun remove(value: Any){
        val list = nameArr.toMutableList()
        if (list.contains(value.toString())) {
            list.remove(value.toString())  // 删除元素
            nameArr = list.toTypedArray()  // 更新 nameArr 数组
        }
    }

    override fun getIterator(): Iterator {
        return object :Iterator{
            override fun hasNext(): Boolean {
                return index < nameArr.size
            }

            override fun next(): Any? {
                if (hasNext()) {
                    return nameArr[index++]
                }
                return null
            }
        }
    }

}
