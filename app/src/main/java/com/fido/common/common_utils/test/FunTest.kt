package com.fido.common.common_utils.test

import com.fido.common.common_base_util.deepCopy
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
@author FiDo
@description:
@date :2023/5/12 9:33
 */
class FunTest {
    fun add(target:Int){
        var sum = 0
        var obj = target
        while (obj <= 0) {
            sum+=obj
            obj -=1
        }
    }

    companion object{

        var list = List(23) { it } // 创建一个包含50个元素的列表

        var newList = listOf<Int>()
        fun doTake(step:Int){
            val subList = list.take(step)
            list -= subList
            if (list.isEmpty()) {
                list = newList
            }
            println("subList=${subList} list=${list}")
        }

        @JvmStatic
        fun main(args: Array<String>) {

//            val list1 = List(11){it}
//            val step = 10
//            println(list1)
//            for (i in list1.indices step step) {
//                val subList = list1.subList(i, i + step)
////                val subList = list1.take(i+step)
//                println("subList => $subList")
//            }

            newList = list.deepCopy()
            doTake(10)
            doTake(10)
            doTake(10)
            doTake(10)
            doTake(10)
            doTake(10)
            /*val step = 10 // 每次最多可以取的元素个数
            var list = List(3) { it } // 创建一个包含50个元素的列表
            while (list.isNotEmpty()) {
                val subList = list.take(step)
                println(subList)
                list -= subList
            }*/

            val auto = AtomicBoolean(false)
            //m.compareAndSet(a,b),如果m==a ,返回true，同时将m置为b； 如果m==b，返回false。
            val boolenResult = auto.compareAndSet(true,false)
            println("compareAndSet(true,false) Result = $boolenResult  auto=${auto}")
            auto.set(true)
            println("AtomicBoolean Result = ${auto.get()} auto.compareAndSet(true,false)=${auto.compareAndSet(true,false)}")

            val str1 = "S_Android"
            println(stringMapperFun(str1).invoke(str1))

            println(str1.lastChar())

            testDeco()

            Job.print("abcd")

            val p = Person("xpz",15)
            println("person minus = ${20-p} person minus person=${Person("dpz",18) - p}")
            val result = highLevelFunction("Android"){
                it.length
//                return
            }
            println("highLevelFunction $result")
            println("我没有执行？")
            println("generateUniqueId=${generateUniqueId()}")
        }

        private fun generateUniqueId(): Int {
            // 生成唯一的 ID
            return UUID.randomUUID().hashCode()
        }

        /**
         * 解构声明 val (msg,code)
         * operator + componentN 关键字
         */
        private fun testDeco() {
            val (msg,code) = Result("test",66)
            println("msg=$msg code=$code")
        }
        class Result(private val msg:String,private val code:Int){
            operator fun component1(): String {
                return msg
            }

            operator fun component2(): Int {
                return code
            }
//            operator fun component1() = msg
//            operator fun component2() = code
        }

        /**
         * **inline 还可以让函数参数里面的 return 生效**。因为平常的高阶函数调用传入方法体不允许 return，
         * 但如果该高阶函数标注了 inline 就可以直接 return 整个外部函数。
         */
        private inline fun highLevelFunction(input: String, mapper: (String) -> Int): Int {
            return mapper(input)
        }

        //函数作为返回值
        fun stringMapperFun(input:String):(String)->Int{
            return {
                val newStr = input.substring(input.indexOf("A"))
                newStr.length
            }
        }

        fun <T> T.also(block:(T)->Unit):T{
            block(this)
            return this
        }

        fun String.lastChar() = this[length-1]

        //扩展属性
        val <T>List<T>.last:T get() = get(size-1)


    }
}

// 伴生对象的扩展函数和属性
class Job{
    companion object{}
}

fun Job.Companion.print(summary:String) = println(summary)

//运算符重载
data class Person(val name:String,val age:Int)

operator fun Int.minus(p:Person) = this - p.age

operator fun Person.minus(p:Person) = this.age - p.age