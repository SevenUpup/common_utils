package com.fido.common.common_utils.test.reflect.dynamic

import java.lang.reflect.Proxy

/**
 *  1.我们提供了一个工厂类，用于获取代理实例，这个代理实例代理了我们的目标对象即被代理者。
    2.newProxyInstance方法需要三个参数，第一个是目标类的类加载器，第二个是目标类实现的接口，第三个是InvocationHandler接口，该接口有个invoke方法。
    3.代理逻辑实际上就是invoke方法中的逻辑，在这个方法中，我们首先打印了标识代理执行开始的语句，结果利用kotlin反射机制调用了被代理者的目标方法，最后我们打印了标识代理执行结束的语句。从这个流程可以看出，我们既可以在执行被代理者业务逻辑之前做一些工作，也可以在其之后做一些工作，这也是代理模式的另一部分优势！面向切面编程(AOP)就是利用了动态代理这一思想来实现的。
    4.在invoke方法中，我们对参数args进行了处理，主要是kotlin中method.invoke方法接收两个参数，第一个就是我们的被代理对象，第二个则是被代理的方法参数，但是由于这个参数是可变参数，而我们传入的实际上是个数组，这如果放在java中，编译器可以帮我们自动处理，但是kotlin中我们还需要显示进行处理，处理方法就是在数组之前加上星号(*)，这样数组就被转换为了可变参数。
    5.上述提到了method，那么这个到底是什么？实际上这个就是我们的接口方法，这也是动态代理为什么要求目标者实现接口的原因。打印method的值如下所示：
 */
class ProxyFactory {
    companion object {
        fun getInstance(targetObj: Any): Any? {
            return Proxy.newProxyInstance(targetObj.javaClass.classLoader, targetObj.javaClass.interfaces) { proxy, method, args ->
                println("proxy start, you can do some work here")
                val newArgs = args ?: arrayOfNulls(0)
                method.invoke(targetObj, *newArgs)
                println("proxy end, you can also do some work here")
            }
        }
    }
}