package com.fido.common.common_utils.test.reflect

class AbsReflectImp : AbsReflectTest<String>() {

    var map1 = mutableMapOf("8" to "8","9" to "9")

    val list8 = mutableListOf("6", "66", "666")

    val map6
        get() = put("9999")

    val list6
        get() = getList("123")

    fun put(elment: String) = map.apply {
        put(elment,elment)
    }

    fun putElment(elment: String) = map1.put(elment,elment)

    override fun getList(string: String): List<String> {
        list8.add(string)
        return list8
    }

}