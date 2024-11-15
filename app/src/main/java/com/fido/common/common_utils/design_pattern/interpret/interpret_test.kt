package com.fido.common.common_utils.design_pattern.interpret

import android.util.AndroidException

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  解释器模式
 * 解释器模式的优点：
 * 易于扩展：可以通过增加新的非终结符表达式来扩展语法和功能。
 * 清晰的语法结构：通过将语法和表达式解析封装到对象中，代码变得更加清晰易懂。
 * 符合开闭原则：可以在不改变现有代码的基础上，添加新的解释规则，符合开闭原则（对扩展开放，对修改封闭）。
 * 适用于复杂的语言和表达式解析：在一些需要解释执行的语言中（如脚本语言或配置文件），解释器模式可以有效地简化解析逻辑。
 * 解释器模式的缺点：
 * 性能问题：解释器模式的递归解析和对象创建可能会导致性能问题，特别是在表达式较复杂时，计算开销较大。
 * 增加系统复杂性：对于简单的语法规则，解释器模式可能会显得过于复杂，使用时需要权衡是否真的需要。
 * 难以维护：如果语法规则变得非常复杂，解释器的设计也会变得复杂，导致后续维护和扩展困难。
 * 适用场景：
 * 表达式求值：当我们需要评估一个复杂的表达式（如数学计算、语言解析）时，解释器模式非常有用。
 * 编程语言的编译器：解释器模式常用于构建编程语言的解释器，例如，类似 SQL 或 DSL（领域特定语言）语言的解析器。
 * 配置文件解析：在需要解析自定义配置文件格式时，解释器模式也可以用于简化解析逻辑。
 */

fun main(){

    //创建不同的表达式，并将其组合起来
    val oneExpression = TerminalExpression(1)
    val twoExpression = TerminalExpression(2)

    val andExpression = AddExpression(oneExpression,twoExpression)
    val subtractExpression = SubtractExpression(oneExpression,twoExpression)

    val andValue = andExpression.interpret()
    val subtractValue = subtractExpression.interpret()
    println("and value=${andValue} subtractValue = $subtractValue")

    val and2Expression = AddExpression(andExpression,subtractExpression)
    val fiveExpression = TerminalExpression(5)
    val subValue2 = SubtractExpression(and2Expression,fiveExpression)
    println("combine value =${subValue2.interpret()}")

}

//数学表达式接口
internal interface Expression{
    fun interpret():Int
}
//2. 创建终结符表达式
//终结符表达式用于解释和执行基本的符号或操作，比如数字。
internal class TerminalExpression(
    private val num:Int
):Expression{
    override fun interpret(): Int  = num
}

//创建非终结符表达式
//非终结符表达式用于解释更复杂的语法规则，比如加法和减法
internal class AddExpression (
    private val expression1:Expression,
    private val expression2:Expression,
):Expression{
    override fun interpret(): Int {
        return expression1.interpret() + expression2.interpret()
    }
}

internal class SubtractExpression(
    private val expression1:Expression,
    private val expression2:Expression,
):Expression{
    override fun interpret(): Int {
        return expression1.interpret() - expression2.interpret()
    }
}