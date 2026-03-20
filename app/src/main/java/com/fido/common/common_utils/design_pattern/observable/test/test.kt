package com.fido.common.common_utils.design_pattern.observable.test

/**
 * @author: HuTao
 * @date: 2025/7/23
 * @des:
 */

fun main() {
    DriveModeManager.handleSignal("4")
    DriveModeManager.handleSignal("3")
    DriveModeManager.handleSignal("2")

    println("进入了Xmode......")
    DriveModeManager.handleSignal("7")
    println("退出了Xmode......")
    DriveModeManager.handleSignal("3")
}