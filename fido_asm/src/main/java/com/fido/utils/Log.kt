package com.fido.utils

/**
 * @author: FiDo
 * @date: 2024/4/3
 * @des:
 */
object Log {

    const val DEFAULT_TAG = "Plugin"
    fun d(tag: String = DEFAULT_TAG, msg: String) {
        println("[$tag] [D]: $msg")
    }

    fun e(tag: String = DEFAULT_TAG, msg: String) {
        println("[$tag] [E]: $msg")
    }

}