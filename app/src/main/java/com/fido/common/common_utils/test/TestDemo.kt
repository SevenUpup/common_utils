package com.fido.common.common_utils.test

import android.content.Context
import android.widget.Toast
import com.fido.common.common_base_util.util.time.Interval
import com.fido.common.common_utils.test.arithmetic.Test
import com.fido.common.common_utils.test.reflect.AbsReflectTest
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

class TestDemo {

    companion object{

        private var block:(Any.(Int)->Int)?=null

        private val typePool = mutableMapOf<String, Any.(Int) -> Int>()

        @JvmStatic
        fun main(args: Array<String>) {
            val a = -10
            var b = a.shl(2) // 按位左移
            var c = a.shr(2) // 右移运算符
            println("$a 的二进制 ${a.toString(2)} $a 左移两位 =$b  $a ->右移两位 = $c 二进制=${c.toString(2)}")

            val result = block?.invoke("123",1)

            typePool["1"] = {
                if (it == 0) {
                    0
                } else {
                    66
                }
            }
            typePool["2"] = {
                2
            }

            println("typePool=$typePool  typePool 1 = ${typePool["1"]?.invoke("112",0)} typePool 1 =${typePool["1"]?.invoke("1",1)} ")

            val list = listOf(1,2,3,4,5,6,7)
            val seed = 3L
            println(Random(seed).nextDouble())
            println(list.shuffled())
            println(list.shuffled(Random(seed)))

            absReflect()
            reflect()

            testArrangeActivity()
            //找零钱问题
//            Test.testGiveMoney()
            giveMoney()

            Test.getMaxWeightValue()
        }

        private fun absReflect() {
            val absClz = Class.forName("com.fido.common.common_utils.test.reflect.AbsReflectTest")
            val absInstanc = object :AbsReflectTest<Int>(){
                override fun getList(t: Int): List<Int> {
                    return emptyList()
                }
            }
            absClz.getDeclaredMethod("getMap66").run {
                invoke(absInstanc)
            }
            absClz.getDeclaredMethod("putMap99",Any::class.java).apply {
                invoke(absInstanc,996)
            }
            val absMap = absClz.getDeclaredField("map").run {
                isAccessible = true
                get(absInstanc)
            }
            println(absMap)

        }

        fun giveMoney(){
            val moneyArray = intArrayOf(50,10,5,1)
            val target = 99
            val kindM = giveYouMoney(moneyArray,target)
            println("找${target}钱的方案")
            kindM.forEachIndexed { index, i ->
                println("${kindM[index]}枚 面值：${moneyArray[index]}")
            }
        }

        private fun giveYouMoney(moneyArray: IntArray, target: Int):IntArray {
            var t = target
            val kindMoney = IntArray(moneyArray.size)
            for(i in moneyArray.indices){
                kindMoney[i] = t/moneyArray[i]
                t %= moneyArray[i]
            }
            return kindMoney
        }

        fun testArrangeActivity() {
            val start = intArrayOf(1, 3, 0, 5, 3, 5, 6, 8, 8, 2, 12)
            val end = intArrayOf(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14)
            val results = arrangeActivity(start, end)
            results.forEachIndexed { index, i ->
                val r = results[index]
                println("开始时间:" + start[r] + ",结束时间:" + end[r])
            }
        }
        fun arrangeActivity(s: IntArray, e: IntArray): List<Int> {
            val total = s.size
            var endFlag = e[0]
            val results: MutableList<Int> = ArrayList()
            results.add(0)
            for (i in 0 until total) {
                if (s[i] > endFlag) {
                    results.add(i)
                    endFlag = e[i]
                }
            }
            return results
        }

        private fun reflect() {

            try {
                /**
                 * 获取抽象类成员变量 需 依托 他的实现类 ，通过实现类 获取 抽象类 的 属性 方法 等
                 */
                println("======================获取抽象类属性/方法 Begin===========================")
                // 四种 instance 都行
                val absInstance = object : AbsReflectTest<String>(){
                    override fun getList(string: String): List<String> {
                        return emptyList()
                    }
                }
//                val absInstance = AbsReflectImp()
//                val absInstance = Class.forName("com.fido.common.common_utils.test.reflect.AbsReflectImp").kotlin.createInstance()
//                val absInstance = Class.forName("com.fido.common.common_utils.test.reflect.AbsReflectImp").newInstance()

                val impClz = Class.forName("com.fido.common.common_utils.test.reflect.AbsReflectImp").kotlin
                val absClz = impClz.superclasses[0]
                val absMap = absClz.declaredMemberProperties.find { it.name=="map" }?.run {
                    this as KProperty1<Any,Any>
                    get(absInstance)
                }
                absClz.declaredMemberFunctions.find { it.name == "getMap66" }.run {
                    this?.call(absInstance)
                }
                val absmap2 = absClz.declaredMemberProperties.find { it.name == "map" }.run {
                    this as KProperty1<Any,*>
                    get(absInstance)
                }
                println("abs=>map=${absMap} absMap2=${absmap2}")
                //动态添加抽象类成员变量值
                val absMuiltMap = absClz.declaredMemberProperties.find { it.name=="muiltMap" }.run {
                    this as KMutableProperty1<Any,Any>
                    set(absInstance, mutableMapOf("1" to "1",2 to 2, listOf("1","2") to listOf("3","4")))
                    get(absInstance)
                }
                println("muiltAbsMap =${absMuiltMap}")

                val impInstance = impClz.createInstance()
                absClz.declaredMemberFunctions.find { it.name == "getList" }.run {
                    this?.call(impInstance,"7788")
                }
                val list8 = impClz.declaredMemberProperties.find { it.name == "list8" }?.run {
                    this as KProperty1<Any,Any>
                    get(impInstance)
                }
                println("list8 =${list8}")

                println("======================获取抽象类属性/方法 End===========================")
                val absImp = Class.forName("com.fido.common.common_utils.test.reflect.AbsReflectImp")
                val listMethod = absImp.getDeclaredMethod("getList",String::class.java)
                val ins = absImp.newInstance()
                listMethod.invoke(ins,"abc")
                val member = absImp.kotlin.memberProperties.find { it.name == "list6" }
                (member as KProperty1<Any,Any>)?.get(ins)
                println("absInstance.getList= ${(member as KProperty1<Any,Any>)?.get(ins)}")

                absImp.getDeclaredMethod("put",String::class.java).apply {
                    invoke(ins,"7")
                }
                val map6 = absImp.kotlin.declaredMemberProperties.find { it.name == "map6" }.run {
                    this as (KProperty1<Any,Any>)
                    get(ins)
                }
                println("map6=${map6}")

                absImp.getDeclaredMethod("putElment",String::class.java).invoke(ins,"10")

                val map1 = absImp.kotlin.declaredMemberProperties.find { it.name == "map1" }.run {
                    this as (KProperty1<Any,Any>)
                    get(ins)
                    // var类型 可以通过 转换为 KMutableProperty1 调用 set方法
                    isAccessible = true
                    this as (KMutableProperty1<Any,Any>)
                    set(ins, mapOf("-1" to "-1"))
                    get(ins)
                }
                println("map1=${map1}")

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    interface T{
        companion object DEFULT:T{
            override fun onCreat(
                context: Context,
                message: CharSequence,
                duration: Int,
                tag: Any?
            ): Toast? {
                return Toast.makeText(context,message,duration)
            }
        }

        fun onCreat(
            context:Context,
            message:CharSequence,
            duration:Int,
            tag:Any?=null
        ):Toast?
    }

}