package com.fido.common.common_utils.design_pattern.template

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  模板模式
 * 模板模式（Template Method Pattern） 是行为型设计模式的一种，它定义了一个操作中的算法框架，而将一些步骤的具体实现延迟到子类中。通过模板方法，父类可以控制算法的整体流程，而将某些步骤的实现交给子类去完成。这样，子类可以重新定义某些特定步骤的实现，而不改变算法的结构。
 *
 * 模板模式的结构
 * 抽象类（Abstract Class）：定义了一个模板方法，模板方法调用一系列的步骤方法，这些步骤方法中有一些是抽象的，需要由子类实现。
 * 具体类（Concrete Class）：实现了父类中定义的抽象方法，完成具体的步骤。
 * 模板模式的关键点
 * 模板方法（Template Method）：在抽象类中定义的算法结构，包含一系列的步骤。
 * 钩子方法（Hook Method）：父类中可以提供的空实现方法，子类可以选择性地覆盖它。
 * 抽象步骤（Abstract Method）：父类定义的抽象方法，需要子类去实现具体的步骤。
 * 模板模式的优缺点
 * 优点：
 * 代码复用：公共的算法流程由父类提供实现，子类只需实现特定步骤，避免重复代码。
 * 可扩展性：通过子类重写特定步骤，可以很方便地扩展算法的实现。
 * 控制流程：父类控制算法的整体流程，子类只需要关注特定步骤的实现。
 * 缺点：
 * 子类依赖父类：子类依赖父类的模板方法，因此父类的修改可能会影响子类。
 * 增加类的复杂度：模板方法有时会导致类层次的增多，增加了系统的复杂度。
 * 模板模式的示例
 * 假设我们有一个制作饮料的过程，制作饮料的步骤是相似的，但具体的饮料可能有不同的做法，例如咖啡和茶。
 */
fun main(){

    val coffee:Beverage = Coffee()
    val tea:Beverage = Tea()

    coffee.prepareRecipe()

    println("\n")

    tea.prepareRecipe()
}

//抽象类（Template）：定义了饮料制作的模板
internal abstract class Beverage{

    //准备配方
    public fun prepareRecipe(){
        boilWater()
        brew()
        pourInCup()
        if (hook()) {
            addCondiments()
        }
    }

    //水烧开
    private fun boilWater(){
        println("Boiling water")
    }
    //倒进杯子
    private fun pourInCup(){
        println("pouring into cup")
    }

    protected open fun hook():Boolean = true

    // 酿造-抽象方法交给子类实现
    protected abstract fun brew()

    //添加调理-抽象方法
    protected abstract fun addCondiments()
}

//具体类（Coffee）：实现了父类中的抽象方法，定义了咖啡的制作步骤
internal class Coffee : Beverage(){
    override fun brew() {
        println("brewing coffee")
    }

    override fun addCondiments() {
        println("add milk and sugar")
    }

    override fun hook() = true
}

internal class Tea :Beverage(){

    override fun brew() {
        println("brewing Tea")
    }

    override fun addCondiments() {
        println("add lemon")
    }

    override fun hook(): Boolean {
        return false
    }
}
