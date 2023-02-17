package com.fido.common.common_base_util.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间日期相关
 */


/**
 *  日期转换为时间戳
 *  字符串日期格式（比如：2018-4-6)转为毫秒
 *  @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果格式不一样，则需要传入对应的格式
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss"): Long {

    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this).time
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

// =======================  时间戳转日期 begin ↓ =========================
/**
 * 时间戳转日期
 * Long类型时间戳转为字符串的日期格式
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果格式不一样，则需要传入对应的格式
 */
fun Long.formatDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {

    return try {

        //秒转毫秒
        var millisecond = this
        //转换PHP时间戳
        if (this.toString().length < 11) {
            millisecond = this * 1000
        }

        SimpleDateFormat(format, Locale.getDefault()).format(Date(millisecond))

    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}

/**
 * 时间戳转日期
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果格式不一样，则需要传入对应的格式
 */
fun String.formatDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {

    return this.toLong().formatDateString(format)

}

/**
 * 时间戳转日期
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果格式不一样，则需要传入对应的格式
 */
fun Int.formatDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {

    return this.toLong().formatDateString(format)

}
