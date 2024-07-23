package com.fido.common.common_utils.customview.fake_taobao

/**
 * @author: FiDo
 * @date: 2024/7/23
 * @des:
 */
object HandleDataUtils {

    /**
     * 将原数组根据模数进行分组
     *  arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
     * Group 0: [3, 6, 9, 12, 15, 18]
     * Group 1: [1, 4, 7, 10, 13, 16, 19]
     * Group 2: [2, 5, 8, 11, 14, 17, 20]
     */
    fun<T> groupListByMod(data:List<T>,mod:Int): List<MutableList<T>> {
        // 创建一个可变列表的列表，大小为mod，来存储结果
        val result = List(mod) { mutableListOf<T>() }
        // 遍历数据数组，根据取模结果将元素添加到相应的子数组中
        data.forEachIndexed { index, t ->
            val i = index % mod
            result[i].add(t)
        }
        return result
    }

    fun<T> groupListIndexByMod(data:List<T>,mod:Int): List<MutableList<Int>> {
        // 创建一个可变列表的列表，大小为mod，来存储结果
        val result = List(mod) { mutableListOf<Int>() }
        // 遍历数据数组，根据取模结果将元素添加到相应的子数组中
        data.forEachIndexed { index, t ->
            val i = index % mod
            result[i].add(index)
        }
        return result
    }

}