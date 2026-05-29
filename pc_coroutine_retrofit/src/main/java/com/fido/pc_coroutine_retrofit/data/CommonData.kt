package com.fido.pc_coroutine_retrofit.data

/**
 * @author: HuTao
 * @date: 2026/5/22
 * @des:
 */
open class CommonData<T> {

    val data:T?=null
    var errorCode: Int? = 0
    var errorMsg: String? = null
}