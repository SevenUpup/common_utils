package com.fido.common.common_utils.design_pattern.a_kotlin

/**
 * @author: FiDo
 * @date: 2025/3/6
 * @des:  kotlin 的相关射击模式
 */

//下面通过委托来替代多继承实现需求:

interface CanFly {
    fun fly()
}
interface CanEat {
    fun eat()
}

open class Flyer : CanFly {
    override fun fly() {
        println("I can fly")
    }
}
open class Animal : CanEat {
    override fun eat() {
        println("I can eat")
    }
}
class Bird(flyer: Flyer, animal: Animal) : CanFly by flyer, CanEat by animal {}

fun main(args: Array<String>) {
    val flyer = Flyer()
    val animal = Animal()
    val b = Bird(flyer, animal)
    b.fly()
    b.eat()

    val produce = AbstractFactory(DellFactory()).produce()
    println("produce=${produce}")

    val asusFactory = AbstractFactory<Asus>()
    println("Factory=${asusFactory} produce = ${asusFactory.produce()}")

}


//内联函数简化抽象工厂模式
interface Computer
class Dell : Computer
class Asus : Computer
class Acer : Computer

class DellFactory : AbstractFactory() {
    override fun produce(): Computer {
        return Dell()
    }
}

class AsusFactory : AbstractFactory() {
    override fun produce(): Computer {
        return Asus()
    }
}

class AcerFactory : AbstractFactory() {
    override fun produce(): Computer {
        return Acer()
    }
}

abstract class AbstractFactory {
    abstract fun produce():Computer
    companion object{
        operator fun invoke(abstractFactory: AbstractFactory):AbstractFactory{
            return abstractFactory
        }

        // 使用内联简化
        inline operator fun <reified T:Computer>invoke():AbstractFactory{
            return when(T::class){
                Dell::class -> DellFactory()
                Asus::class -> AsusFactory()
                Acer::class -> AcerFactory()
                else -> throw IllegalArgumentException()
            }
        }

    }
}
