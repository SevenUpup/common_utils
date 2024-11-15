package com.fido.common.common_utils.design_pattern.bridge

/**
 * @author: FiDo
 * @date: 2024/11/13
 * @des:  桥接模式
 */
fun main(){

    val redColor = RedColor()
    val blueColor = BlueColor()

    val redCircle = Circle(redColor)
    val blueRectangle = Rectangle(blueColor)
    val blueCircle = Circle(blueColor)

    redCircle.draw()

    blueRectangle.draw()

    blueCircle.draw()
}

internal interface Color{
    fun applyColor()
}

class RedColor : Color{
    override fun applyColor() {
        println("applying red color")
    }
}

class BlueColor:Color{
    override fun applyColor() {
        println("applying blue color")
    }
}

class GreenColor:Color{
    override fun applyColor() {
        println("applying green color")
    }

}

internal abstract class Shape(
    protected val color: Color
){
    abstract fun draw()
}

internal class Circle(color: Color) : Shape(color){
    override fun draw() {
        print("drawing a circle  ")
        color.applyColor()
    }
}

internal class Rectangle(color: Color) : Shape(color) {
    override fun draw() {
        print("drawing a rectangle  ")
        color.applyColor()
    }
}
