package com.fido.common.common_utils.test.java.generic

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.fido.common.common_base_util.app


/**
 * @author: FiDo
 * @date: 2024/2/20
 * @des:  泛型测试
 */
class GenericTest {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val b = B<CharSequence>()
            b.set("abc")
            b.set(StringBuilder().append("def"))


            var animal = Animal()
            var dog = Dog()
//            animal = dog

            val dogBox:Box<Dog> = Box(Dog())
            val animBox:Box<Animal> = dogBox // 可以把 Box<Dog> 赋值给 Box<Animal>
            //在这个例子中，Box<out T> 是协变的，因为我们使用了 out 关键字。这意味着 Box<Dog> 可以赋值给 Box<Animal>，因为 Dog 是 Animal 的子类。

            val animalConsumer: Consumer<Animal> = Consumer()
            val dogConsumer: Consumer<Dog> = animalConsumer
            dogConsumer.consume(Dog())
        }
    }
}

// 逆变例子
class Consumer<in T> {
    fun consume(item: T) {
        println("Consuming $item")
    }
}

private open class Animal()
private class Dog:Animal(){}

private class Box<out T>(val value:T){}


// 泛型逆变/协变测试
class A<out T>(_item:T){
    private var item:T = _item

    fun get():T = item

}

class B<in T>(){

    fun set(t:T){
        println("你传入的是${t}")
    }

}