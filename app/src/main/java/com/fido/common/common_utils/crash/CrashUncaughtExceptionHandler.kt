package com.fido.common.common_utils.crash

import android.os.Looper

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des:
 */
object CrashUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
    private var oldHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        oldHandler = Thread.getDefaultUncaughtExceptionHandler()
        oldHandler?.let {
            Thread.setDefaultUncaughtExceptionHandler(this)
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (CrashPortrayHelper.needBandage(e)) {
            bandage()
            return
        }

        //崩吧
        oldHandler?.uncaughtException(t, e)
    }

    /**
     * 让当前线程恢复运行
     */
    private fun bandage() {
        while (true) {
            try {
                if (Looper.myLooper() == null) {
                    Looper.prepare()
                }
                Looper.loop()
            } catch (e: Exception) {
                uncaughtException(Thread.currentThread(), e)
                break
            }
        }
    }
}