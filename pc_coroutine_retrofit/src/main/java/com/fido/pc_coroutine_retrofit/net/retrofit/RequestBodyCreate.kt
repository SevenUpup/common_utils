package com.fido.pc_coroutine_retrofit.net.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * @author: HuTao
 * @date: 2026/5/22
 * @des:
 */
object RequestBodyCreate {

    fun toBody(body: String) = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)

}