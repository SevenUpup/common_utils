package com.fido.common.common_utils.design_pattern.dynamic_proxy

import com.fido.common.common_base_util.toJson
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author: FiDo
 * @date: 2024/10/17
 * @des:
 */

fun main(){
    // 1. 创建调用处理器类对象
    val dynamicProxy = DynamicProxy()
    // 2. 创建目标对象实例
    val buyer1 = Buyer1()
    val buyer1DynamicProxy:Subject = dynamicProxy.newProxyInstance(buyer1) as Subject
    buyer1DynamicProxy.buySomething()

    val buyer2 = Buyer2()
    val buyer2DynamicProxy:Subject = dynamicProxy.newProxyInstance(buyer2) as Subject
    buyer2DynamicProxy.buySomething()

    println("==============================DynamicProxy2==========================")
    val dynamicProxy2 = DynamicProxy2(beforeMethodInvoke = { method,args->
        println("     接口方法执行前--->${method?.name} args=>${args?.toJson()}" )
    }, afterMethodInvoke = { method,args,result ->
        println("     接口方法执行后 ${method?.name} args=>${args?.toJson()} result=>${result}")
    })

    val buyer = Buyer1()
    val buyerSubject = dynamicProxy2.newInstance(buyer) as Subject
    buyerSubject.buySomething()
    buyerSubject.visaAgent(3)

    val buyer_2 = Buyer2()
    val buyer2Subject=dynamicProxy2.newInstance(buyer_2) as Subject
    buyer2Subject.buySomething()
    buyer2Subject.visaAgent(5)
}

class DynamicProxy2(
    private val beforeMethodInvoke:(( method: Method?, args: Array<out Any>?)->Unit)?=null,
    private val afterMethodInvoke:(( method: Method?, args: Array<out Any>?,result:Any?)->Unit)?=null,
) : InvocationHandler{
    private var proxyObj:Any?=null

    fun newInstance(proxy: Any):Any?{
        this.proxyObj = proxy
        return Proxy.newProxyInstance(proxy.javaClass.classLoader,proxy.javaClass.interfaces,this)
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val newArgs = args?: arrayOfNulls<Any>(0)
        beforeMethodInvoke?.invoke(method,args)
        val result = method?.invoke(proxyObj,*newArgs)
        afterMethodInvoke?.invoke(method,args,result)
        return result
    }

}


public interface Subject{
    // 代购物品
    fun buySomething()
    //代办签证
    fun visaAgent(personCount:Int):Int{
        return personCount*200
    }
}

public class Buyer1 :Subject{
    override fun buySomething() {
        println("流川枫要买Mac")
    }
}

class Buyer2:Subject{
    override fun buySomething() {
        println("三井寿要买iPhone18")
    }
}

// 为了能让DynamicProxy类能够在运行时才去实现真实对象类的方法操作
// 需要让DynamicProxy类实现JDK自带的java.lang.reflect.InvocationHandler接口
class DynamicProxy : InvocationHandler{

    private var proxyObject:Any?=null

    fun newProxyInstance(proxyObject: Any):Any{
        this.proxyObject = proxyObject
        return Proxy.newProxyInstance(proxyObject.javaClass.classLoader,proxyObject.javaClass.interfaces,this)
        // 作用：根据指定的类装载器、一组接口 & 调用处理器 生成动态代理类实例，最终返回一个动态代理类实例
        // 参数说明：
        // 参数1：指定产生代理对象的类加载器，需要将其指定为和目标对象同一个类加载器
        // 参数2：获取目标对象的实现接口
        // 参数3：指定被拦截方法在被拦截时需要执行InvocationHandler的invoke（）的所属对象
    }

    @Throws(Throwable::class)  // InvocationHandler接口的方法
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        println("代购出门了")
        var result: Any? = null
        val newArgs = args?: arrayOfNulls<Any>(0)
        // 通过Java反射机制调用目标对象方法
        result = method?.invoke(proxyObject, *newArgs)
        println("代购买好了 result=>${result}")
        return result
    }

}
