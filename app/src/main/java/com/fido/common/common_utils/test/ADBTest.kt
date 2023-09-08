package com.fido.common.common_utils.test

import com.fido.common.common_base_util.util.ShellUtils

class ADBTest {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            println("start adb")
//            ShellUtils.execCmd("adb shell input keyevent 26",false)
            ShellUtils.execCmd("adb shell screencap /sdcard/screenshot.png",false)
            println("end adb")
        }
    }
}