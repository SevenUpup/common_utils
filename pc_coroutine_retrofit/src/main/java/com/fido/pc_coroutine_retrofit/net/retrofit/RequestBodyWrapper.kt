package com.fido.pc_coroutine_retrofit.net.retrofit

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

/**
 * @author: HuTao
 * @date: 2026/5/22
 * @des:
 */
class RequestBodyWrapper constructor(val postBody: String) : RequestBody() {

    override fun contentType(): MediaType? {
        return "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        sink.write(postBody.toByteArray(), 0, postBody.toByteArray().size)
    }
}