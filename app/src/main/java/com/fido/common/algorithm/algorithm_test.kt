package com.fido.common.algorithm

/**
 * @author: FiDo
 * @date: 2024/11/27
 * @des:  算法测试
 */

//==============================================有10个台阶，1步可以走1个台阶，也可以一次走2个台阶, 问10个台阶有多少种走法?

/**
 * 代码解释：
 *
 * 优化空间复杂度： 相比于 Java 版本使用数组，这里我们只使用了三个变量 first、second 和 third 来存储当前状态和计算下一个状态。这样可以将空间复杂度从 O(n) 优化到 O(1)。
 * Kotlin 特性：
 * 表达式体函数： climbStairs 函数使用了表达式体，使得代码更加简洁。
 * 区间表达式： for (i in 3..n) 这种写法在 Kotlin 中非常常见，用于遍历一个范围内的整数。
 * 类型推断： Kotlin 的类型推断机制使得我们可以省略很多类型声明。
 * 代码逻辑：
 *
 * 初始化： first 和 second 分别初始化为 1 和 2，对应爬 1 个和 2 个台阶的情况。
 * 动态规划：
 * 从第 3 个台阶开始迭代。
 * 每次迭代计算出爬到当前台阶的方法数 third，并更新 first 和 second 为下一轮计算做准备。
 * 返回结果： 最后返回 second，即爬到第 n 个台阶的方法数。
 * 为什么使用这种方法？
 *
 * 效率更高： 避免了数组的创建和访问，减少了内存开销。
 * 代码更简洁： Kotlin 的语法特性使得代码更加简洁易懂。
 * 更符合函数式编程风格： Kotlin 鼓励使用函数式编程风格，这种解法体现了函数式编程的思想。
 */
fun climbStairs(n: Int): Int {
    // 使用两个变量来优化空间复杂度，避免使用数组
    var first = 1
    var second = 2
    if (n <= 2) return n

    for (i in 3..n) {
        val third = first + second
        first = second
        second = third
    }
    return second
}

//简单点
fun climbStairs2(n:Int):Int{
    if (n == 1) return 1
    if (n == 2) return 2   //爬2阶梯 两种：1.只用一步去爬 2.用两步
    return climbStairs2(n-1) + climbStairs2(n-2)
}

//斐波那契
fun climbStairs3(n:Int):Int{
    if (n == 1) return 1
    var first = 1
    var second = 2
    for (i in 3..n) {
        val third = first + second
        first = second
        second = third
    }
    return second
}

fun main() {
    val n = 10
    val result = climbStairs(n)
    println("爬$n 个台阶共有$result 种走法")

    val r = climbStairs2(10)
    println("爬$n 个台阶 用递归算法共有$r 种走法")

    val result2 = climbStairs3(10)
    println("爬$n 个台阶 用斐波那契算法共有$result2 种走法")
}





