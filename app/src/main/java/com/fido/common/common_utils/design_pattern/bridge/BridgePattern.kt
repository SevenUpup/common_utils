package com.fido.common.common_utils.design_pattern.bridge

import java.util.ServiceLoader

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


    //
    val androidTouchImplementor = AndroidTouchImp()
    val iosTouchImplementor = IOSTouchImp()

    val deviceTouch = DeviceTouch(androidTouchImplementor)
    val deviceTouch2 = DeviceTouch(iosTouchImplementor)

    deviceTouch.touch()
    deviceTouch2.touch()
}

// =========================== 适配不同平台 ============================
//1.定义一个触摸接口
internal interface TouchImplementor{
    fun touchAction()
}

internal class AndroidTouchImp :TouchImplementor{
    override fun touchAction() {
        println("Android touch 事件")
    }
}
internal class IOSTouchImp:TouchImplementor{
    override fun touchAction() {
        println("IOS touch 事件")
    }
}
//定义一个抽象屏幕触摸类
internal abstract class OsDeviceTouch(
    val touchImplementor: TouchImplementor
){
    abstract fun touch()
}

internal class DeviceTouch(touchImplementor: TouchImplementor) :OsDeviceTouch(
    touchImplementor
){
    override fun touch() {
        val osName = when (touchImplementor) {
            is AndroidTouchImp->"android"
            is IOSTouchImp -> "ios"
            else ->""
        }
        println("$osName 开始分发手势")
        touchImplementor.touchAction()
    }
}

// =========================== 适配不同平台 ============================

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
