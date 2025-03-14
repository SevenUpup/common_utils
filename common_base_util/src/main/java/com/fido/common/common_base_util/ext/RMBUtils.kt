package com.fido.common.common_base_util.ext

/**
 * @author: FiDo
 * @date: 2025/3/11
 * @des:  RMB 相关操作
 */
object RMBUtils {
    private val data: CharArray = charArrayOf('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖')
    private val units: CharArray = charArrayOf('元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿')

    /**
     * RMB大小写转换
     */
    fun convert(money: Int): String {
        var money = money
        val sbf = StringBuffer()
        var unit = 0
        while (money != 0) {
            sbf.insert(0, units[unit++])
//            println(sbf.toString())
            val number = money % 10
            sbf.insert(0, data[number])
            money /= 10
        }
        return sbf.toString()
    }


}
