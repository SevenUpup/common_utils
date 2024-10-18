package com.fido.common.common_utils.test.reflect

import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberProperties

/**
 * @author: FiDo
 * @date: 2024/10/16
 * @des:
 */
fun main(){

    //测试静态类加载时机
    reflectSingletonClazz()

}

fun reflectSingletonClazz() {
    //java
    val field = TestSingleton::class.java.getDeclaredField("ourInstance")
    field.isAccessible = true
    println(field.get(null))

    val method = TestSingleton::class.java.getDeclaredMethod("newInstance")
    val instance = method.invoke(null)
    println(instance)

    //kt
    // 获取 Singleton 类的 KClass
    val singletonClass = Singleton::class

    // 通过反射获取 companion object
    val companionObject = singletonClass.companionObject

    // 获取 'instance' 属性
    val instanceProperty = companionObject?.memberProperties?.find { it.name == "instance" }

    // 通过反射获取 instance 的值
    val singletonInstance = instanceProperty?.getter?.call(companionObject.objectInstance)

    println(singletonInstance) // 打印实例

    val properties = Singleton::class.companionObject?.memberProperties?.find { it.name == "instance" }
//    // 通过反射获取 instance 的值
    val singletonInstance2 = properties?.getter?.call(Singleton::class.companionObject?.objectInstance)
    println("$singletonInstance2")
    //直接call也行
    val callInstance = properties?.call(Singleton::class.companionObject?.objectInstance)
    println("$callInstance")

    //java 调用kt的静态companion 方法
    val name = Singleton.Companion::class.java.name   // "com.fido.common.common_utils.test.reflect.Singleton\$Companion"
    println("使用java Api 反射调用Kt${name}")
    val clz = Class.forName("com.fido.common.common_utils.test.reflect.Singleton\$Companion")
    clz.declaredFields.forEach {
        println("field =>${it.name}")
    }
    clz.declaredMethods.forEach {
        println("method =>${it.name}")
    }
    val getInsMethod = clz.getDeclaredMethod("getInstance")
    getInsMethod.isAccessible = true
//    val getIns = getInsMethod.invoke(Singleton::class.companionObjectInstance)
    //或者调用私有构造方法
    val declaredConstructor = clz.getDeclaredConstructor()
    declaredConstructor.isAccessible = true
    val getIns = getInsMethod.invoke(declaredConstructor.newInstance())
    println("getInstance=>${getIns}")

    javaReflectKt()

    println("反射调用静态内部类获取单例")
    val clzInnerClzSingleton = TestInnerClzSingleton::class.java
    val innerClzSingleton = clzInnerClzSingleton.getDeclaredMethod("newInstance").invoke(null)
    println(innerClzSingleton)

    println("反射调用静态内部类中方法")
    invokeInnerClzStaticFun()

    println("反射调用枚举单例类中方法")
    invokeEnumClzFun()


}

fun invokeInnerClzStaticFun() {
    val clz = Class.forName("com.fido.common.common_utils.test.reflect.TestInnerClzSingleton\$Instance")
    clz.declaredMethods.forEach {
        println("method name=${it.name}")
    }
    val method = clz.getDeclaredMethod("innerTestFun")
    method.isAccessible = true
    //私有类所以需要
    val constructor = clz.getDeclaredConstructor()
    constructor.isAccessible = true
    method.invoke(constructor.newInstance())

}

fun invokeEnumClzFun() {
    val clz = Class.forName("com.fido.common.common_utils.test.reflect.singleton.EnumSingleton")
    val instanceField = clz.getDeclaredField("INSTANCE")
    val instance = instanceField.get(null)
    val declaredMethod = clz.getDeclaredMethod("testFun")
    declaredMethod.invoke(instance)
}

fun javaReflectKt(){
    try {
        // 获取 Singleton 类的 Class 对象
        val singletonClass = Class.forName("com.fido.common.common_utils.test.reflect.Singleton").kotlin
        //或者这样获取
//        val singletonClass = Singleton::class
        singletonClass.companionObject?.declaredFunctions?.forEach{
            println("declaredFunctions=>$it")
        }
        singletonClass.companionObject?.memberProperties?.forEach{
            println("memberProperties=>$it")
        }
        val properties = singletonClass.companionObject?.memberProperties?.find { it.name == "instance" }
        val singletonInstance = properties?.call(singletonClass.companionObjectInstance)
        println(singletonInstance) // 打印实例
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


class Singleton private constructor() {

    companion object {
        // 单例实例
        val instance: Singleton by lazy { Singleton() }
    }
}


