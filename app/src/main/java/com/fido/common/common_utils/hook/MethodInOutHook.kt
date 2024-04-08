package com.fido.common.common_utils.hook

import androidx.annotation.Keep
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * @author: FiDo
 * @date: 2024/4/3
 * @des:
 */

/**
 * For hook invoke.
 */
@Keep
object MethodInOutHook {

    val formatMs = ThreadLocal<SimpleDateFormat>().apply { set(
        SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS",
            Locale.US)
    ) }

    @JvmStatic
    fun methodIn() {
//        Log.d(TAG, "MethodIn Thread=${currentThread.name}, Class=${method.className}, Method=${method.methodName}")
        //计算方法执行开始时间
        val currentThread = Thread.currentThread()
        val method = currentThread.stackTrace[3]
//        Log.d(TAG, "methodIn: ${currentTimeString(format = formatMs.get())} Class=${method.className}, Method=${method.methodName}")
    }

    @JvmStatic
    fun methodOut() {
//        Log.d(TAG, "MethodOut Thread=${currentThread.name}, Class=${method.className}, Method=${method.methodName}")
        val currentThread = Thread.currentThread()
        val method = currentThread.stackTrace[3]
        val sb = StringBuilder()
        currentThread.stackTrace.forEach {
            sb.append(it.toString() + "\n")
        }
//        Log.e(TAG, "stackTrace.size = ${currentThread.stackTrace.size} \n${sb.toString()}")

//        Log.d(TAG, "methodOut: ${currentTimeString(format = formatMs.get())} Class=${method.className}, Method=${method.methodName}")
    }

    private const val TAG = "FiDo"
}