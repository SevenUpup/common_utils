package com.fido.common.common_utils.design_pattern.a_kotlin.run_apply_let_also

/**
 * @author: FiDo
 * @date: 2025/3/10
 * @des:
 */
fun main() {

    var name = "hi-dhl"

    with(name){

    }

// 返回调用本身
    name = name.also {
        val result = 1 * 1
        "juejin"
    }
    println("name = ${name}") // name = hi-dhl

// 返回的最后一行
    name = name.let {
        val result = 1 * 1
        "hi-dhl.com"
    }
    println("name = ${name}") // name = hi-dhl.com

    var a = 1
    var b = 2
    // Kotlin
    a = b.also { b = a }
    println("a = $a b = $b") // a = 2 b = 1

    // also 返回调用者本身 ：也就是说 b.also { b = a } 会先将 a 的值 (1) 赋值给 b，此时 b 的值为 1，然后将 b 原始的值（2）赋值给 a，此时 a 的值为 2，实现交换两个变量的目的。

    val base: Base = Extended()
    base.`fun`()

    //当我们调用重载方法时，调度变为静态并且仅取决于编译时类型。
    val mBase:Base = Extended()
    foo(mBase)  // 在这种情况下，即使base本质上是Extended的实例，最终还是会执行Base的方法。


}

fun foo(base: Base){
    base.`fun`()
}

fun foo(extended: Extended){
    extended.`fun`()
}

open class Base {
    open fun `fun`() {
        println("I'm Base foo!")
    }
}

class Extended : Base() {
    override fun `fun`() {
        println("I'm Extended foo!")
    }
}