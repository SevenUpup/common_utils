package com.fido.common.common_utils.test.asm

/**
 * @author: FiDo
 * @date: 2024/4/12
 * @des:
 */
class InsertCodeTest {

    companion object{
        var staticInt = 10
        fun staticTest(){
            staticInt=666
            val a = staticInt
            println("----staticInt=$staticInt")
        }
    }

    fun test() {
        println("=================6不6=================")
//        println("=================6的1=================")
    }


    fun testA() {
        val var1 = System.currentTimeMillis()
        println("i am A")
        val var3 = System.currentTimeMillis()
        println(StringBuilder().append("cost:").append(var3 - var1).toString())
        TestTool.useTool(var1)
    }

}
