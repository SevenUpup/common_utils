package com.fido.common.common_utils.test

import com.fido.common.common_utils.test.FunTest.Companion.also
import com.fido.common.common_utils.test.decorator.Mi2ProtableBattery
import com.fido.common.common_utils.test.decorator.MiProtableBattery
import com.fido.common.common_utils.test.decorator.ProtableBattery
import kotlinx.coroutines.*

class JModelTest {

    companion object{



        @JvmStatic
        fun main(args: Array<String>) {
            val protableBattery:ProtableBattery = MiProtableBattery()
            protableBattery.charge()

            val mi2ProtableBattery = Mi2ProtableBattery(protableBattery)
            mi2ProtableBattery.charge()

            val mBlock = {i:Int -> i + 1 }
            println(mBlock.invoke(5))

            val result = Book().run {
                name = "test"
                price = "23"
                10
            }

            val result2 = Book().apply {
                name = "test1"
                price = "45"
                10
            }
            val result3 = Book().also {
                it.name = "test2"
                it.price = "56"
                "456"
            }
            println(result)
            println(result2)
            println(result3)
            main()
        }

        fun main() = runBlocking {
            launch {
                delay(1000)
                System.err.println("(Main.kt:19)    ")
            }

            launch {
                withContext(NonCancellable) {
                    delay(2000)
                    System.err.println("(Main.kt:26)    ")
                }
            }

            delay(500) // 防止launch还未开启withContext就被取消
            cancel()
        }

    }

    class Book(var name:String="",var price:String="")

}