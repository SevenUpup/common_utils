package com.fido.common.common_base_util.ext

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.util.Objects.isNull
import java.util.regex.Pattern


/**
 * 字符串处理相关
 */

/**
 * 是否是手机号
 */
fun String.isPhone() =
    "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$".toRegex()
        .matches(
            this
        )

/**
 * 是否是邮箱地址
 */
fun String.isEmail() = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?".toRegex().matches(this)

/**
 * 是否是身份证号码
 */
fun String.isIDCard() = "[1-9]\\d{16}[a-zA-Z0-9]".toRegex().matches(this)

/**
 * 是否是中文字符
 */
fun String?.isChinese() = "^[\u4E00-\u9FA5]+$".toRegex().matches(this!!)

fun String?.checkEmpty(): Boolean {

    return !(this != null && !"".equals(
        this.trim({ it <= ' ' }),
        ignoreCase = true
    ) && !"null".equals(this.trim({ it <= ' ' }), ignoreCase = true))
}

@SuppressLint("NewApi")
fun Collection<Any>?.checkEmpty(): Boolean {
    if (this == null) return true
    return isNull(this) || this.isEmpty()
}

fun ArrayList<Any>?.checkEmpty(): Boolean {
    if (this == null) return true
    return this.isEmpty()
}

fun Map<Any, Any>?.checkEmpty(): Boolean {
    if (this == null) return false
    return this.isEmpty()
}

fun HashMap<Any, Any>?.checkEmpty(): Boolean {
    if (this == null) return true
    return this.isEmpty()
}

fun LinkedHashMap<Any, Any>?.checkEmpty(): Boolean {
    if (this == null) return true
    return this.isEmpty()
}


/**
 * 字符串逗号分隔为集合
 * 截取，逗号转集合，集合转逗号
 */
fun String.toCommaList(): List<String> {
    if (this.contains(",")) {
        return this.split(",").map { it.trim() }
    }
    return listOf()
}

/**
 * 集合转为逗号分隔
 */
fun Collection<String>.toCommaStr(): String {
    return this.let {
        val builder = StringBuilder()
        it.forEach {
            builder.append("$it,")
        }
        var str = builder.toString().trim()
        if (str.endsWith(",")) {
            str = str.substring(0, str.length - 1)
        }
        str
    }
}

/**
 * 判断字符串是否是数字类型
 */
fun CharSequence.checkNumberPoint(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        val pattern = Pattern.compile("-?[0-9]*.?[0-9]*")
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }
}

/**
 * 格式化数据,如果没有值默认显示0
 */
fun String.formatMoney(pointLenght: Int = 0): String {

    if (checkNumberPoint()) {

        val formatStr = when (pointLenght) {
            0 -> "###,###,##0.0#"  //最多2位长度，最少1位长度
            1 -> "###,###,##0.0"   //必定1位长度
            2 -> "###,###,##0.00"  //必定2位长度
            else -> {
                "###,###,##0.##"   //最多2位长度，最少0位长度
            }
        }

        val df = DecimalFormat(formatStr)
        return df.format(this.toDouble())
    }
    return this
}


