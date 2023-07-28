package com.fido.common.common_base_util.ext

import android.util.Log
import com.fido.common.common_base_util.log.LogUtils

/**
 * Log 打印 扩展函数
 */

fun logv(msg: String, customTag: String = "") = LogUtils.v(msg, customTag)

fun logd(msg: String, customTag: String = "") = LogUtils.d(msg, customTag)

fun logi(msg: String, customTag: String = "") = LogUtils.i(msg, customTag)

fun logw(msg: String, customTag: String = "") = LogUtils.w(msg, customTag)

fun loge(msg: String, customTag: String = "") = LogUtils.e(msg, customTag)

fun logJson(msg: String, customTag: String = "") = LogUtils.json(msg, customTag)

fun logGson(msg: Any,customTag: String="") = LogUtils.gson(msg, customTag)

fun logGlobalTag(global: String) {
    LogUtils.logGlobalTag = global
}

fun logEnabled(logEnabled: Boolean) {
    LogUtils.logEnabled = logEnabled
}


/**
 * 支持超长日志输出的工具方法
 * */
private const val SEGMENT_SIZE = 3072
fun logD(content: String) {
    if(!LogUtils.logEnabled) return
    if (content.length < SEGMENT_SIZE) {
        Log.d(LogUtils.logGlobalTag, content)
        return
    } else {
        Log.d(LogUtils.logGlobalTag, content.substring(0, SEGMENT_SIZE))
        logD(content.substring(SEGMENT_SIZE))
    }
}