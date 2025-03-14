package com.fido.common.common_utils.design_pattern.a_kotlin.delegates

import com.fido.common.common_base_util.ext.RMBUtils
import com.fido.common.common_utils.design_pattern.a_kotlin.systemPrinterUtf8
import java.util.Observable
import java.util.Observer
import kotlin.properties.Delegates


/**
 * @author: FiDo
 * @date: 2025/3/7
 * @des:  Kotlin 委托
 */

//观察者模式测试
/**
 * ================================================== Java中标准库观察者 ================================
 */
//定义一个发布者  StockUpdate 类是被观察者。它继承自 Observable，并且维护了一个观察者列表 observers。当股票价格发生变化时，StockUpdate 会通知所有注册的观察者。
class StockUpdate:Observable(){
    val observers = mutableListOf<Observer>()

    fun setStockChanged(price:Int){
        observers.forEach {
            it.update(this,price)
        }
    }
}
//定义一个订阅者(及观察者)
class StockDisplay():Observer{
    override fun update(o: Observable?, arg: Any?) {
        if (o is StockUpdate){
            println("订阅者:$this => The lasted stock price is $arg")
        }
    }
}

fun main() {

    systemPrinterUtf8()

    println("Java中标准库观察者")
    val stockObservable = StockUpdate()
    val stockObserver1 = StockDisplay()
    val stockObserver2 = StockDisplay()
    stockObservable.observers.add(stockObserver1)
    stockObservable.observers.add(stockObserver2)
    stockObservable.setStockChanged(123)
    stockObservable.setStockChanged(456)

    println("Kotlin 通过 Delegate实现观察者模式")
    val display = StockDisplayer()
    val updater = StockUpdater()
    updater.listeners.add(display)
    updater.price = 100
    updater.price = 98
    updater.price = 98

    var a = 1
    var b = 2
    // Kotlin
//    a = b.also { b = a }
//    println("a = ${a} b = ${b}") // a = 2 b = 1
    // 也就是说 b.also { b = a } 会先将 a 的值 (1) 赋值给 b，此时 b 的值为 1，然后将 b 原始的值（2）赋值给 a，此时 a 的值为 2，实现交换两个变量的目的。

    a = b.apply { b = a }
    println("a = ${a} b = ${b}") // a = 2 b = 1

    println(RMBUtils.convert(2333))
}

interface StockUpdateListener{
    fun onRise(price: Int)
    fun onFall(price: Int)
}

class StockDisplayer:StockUpdateListener{
    override fun onRise(price: Int) {
        println("The lasted stock price has risen to $price")
    }

    override fun onFall(price: Int) {
        println("The lasted stock price has fell  to $price")
    }

}

class StockUpdater{

    val listeners = mutableListOf<StockUpdateListener>()

    var price by Delegates.observable(0){property, oldValue, newValue ->
        listeners.forEach {
            if (newValue>oldValue) it.onRise(newValue) else it.onFall(newValue)
        }
    }
}