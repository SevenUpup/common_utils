package com.fido.common.common_utils.test.java.delegate.clazz

/**
 * @author: FiDo
 * @date: 2024/3/6
 * @des:
 */
class ClazzDelegateTest {

    fun interface Fruit{
        fun type():Int
    }

    class FruitProxy(private val fruit: Fruit):Fruit by fruit
    class FruitProxy2(private val fruit: Fruit):Fruit{
        override fun type(): Int {
            return fruit.type()
        }

    }

    @JvmInline
    value class FruitProxy3(private val fruit: Fruit):Fruit by fruit

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val fruitProxy = FruitProxy {
                0
            }
            println(fruitProxy.type())

            val fruitProxy2 = FruitProxy2 {
                3
            }
            println(fruitProxy2.type())

            val fruitProxy3 = FruitProxy3{
                2
            }

        }
    }

}