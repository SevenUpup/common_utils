package com.fido.common.common_utils.test.reflect.dynamic

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class Entrance {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val iOperation = ProxyFactory.getInstance(RealObject()) as IOperation
            iOperation.operation()
//
            val iOperation2 = ProxyFactory.getInstance(RealObject2()) as IOperation2
            iOperation2.operation("just do it!")


            val num1 = 5 // 二进制表示为 0101
            val num2 = 3 // 二进制表示为 0011
            val result = num1 and num2 // 按位与操作，结果为 0001，即十进制的 1

            num1.run {

            }

            println(result) // 输出结果为 1
            println("0 & 1=${0 and 1}")
            println("0 & 5192=${0 and 5192}")

//            val h = object :InvocationHandler{
//                override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
//                    return method?.invoke(proxy,args)
//                }
//            }

            /**
             * 接口动态代理
             */
            val handler = InvocationHandler { proxy, method, args ->
                println("InvocationHandler is running method.name = ${method.name} args =${args} ")
            }

//            val clazzFrontName = "com.fido.common.common_utils.test.reflect.dynamic."
            val clazzFrontName = "com.fido.common.easy_navigation."
//            try {
                val mClazz = Class.forName(clazzFrontName + "JustTest")
                val interfaceClazz = Class.forName(clazzFrontName + "JustTest${"$"}Callback")
                val proxyInstance = Proxy.newProxyInstance(
                    Entrance::class.java.classLoader,
                    arrayOf(interfaceClazz),
                    handler
                )
                val mMethod = mClazz.getDeclaredMethod("laLala", *arrayOf<Class<*>>(interfaceClazz))
                mMethod.isAccessible = true
                mMethod.invoke(mClazz.newInstance(),proxyInstance)
//            }catch (e:Exception){
//                println("eeeeee->${e.message}")
//            }

        }
    }

}

