package com.fido.common.a_tips

/**
 * @author: FiDo
 * @date: 2024/6/27
 * @des:
 */

fun main() {

    val isEven = IntPredicate { it % 2 == 0 }
    println("isEven=${isEven.accept(7)}")

}
// 一个接口如果仅仅有一个抽象的方法，那么这个接口被称为函数式接口或者仅有一个抽象方法的接口（Single Abstract Method (SAM) interfaced)。这个接口可以有几个非抽象的方法，但是仅仅只能有一个抽象的方法。
fun interface IntPredicate{
    fun accept(num:Int):Boolean

    fun cc(){

    }
}
