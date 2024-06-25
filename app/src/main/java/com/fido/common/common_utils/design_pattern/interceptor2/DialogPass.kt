package com.fido.common.common_utils.design_pattern.interceptor2

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  一般是由后台服务器控制，请求接口之后可以动态的根据多个变量控制哪一个弹窗展示，并且可以控制一个弹窗展示之后它对应的后续弹窗的展示。可以很方便的通过对象中的变量控制。
 */
data class DialogPass(
    val msg:String,
    var passType:Int,
)
