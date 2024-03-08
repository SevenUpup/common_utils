package com.fido.common.common_utils.crash.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des: java 层 crash 后台返回数据
 *
 * [  {    "class": "",    "message": "No space left on device",    "stack": [],
 *     "app_version": [],
 *     "clear_cache": 1,
 *     "finish_page": 0,
 *     "toast": "",
 *     "os_version": [],
 *     "model": []
 *   },
 *   {
 *     "class": "BadTokenException",
 *     "message": "",
 *     "stack": [],
 *     "app_version": [],
 *     "clear_cache": 0,
 *     "finish_page": 0,
 *     "toast": "",
 *     "os_version": [],
 *     "model": []
 *   }
 * ]
 *
 */
data class CrashPortray(
    @SerializedName("class_name")
    val className: String = "",
    val message: String = "",
    val stack: List<String> = emptyList(),
    @SerializedName("app_version")
    val appVersion: List<String> = emptyList(),
    @SerializedName("os_version")
    val osVersion: List<Int> = emptyList(),
    val model: List<String> = emptyList(),
    val type: String = "all",
    @SerializedName("clear_cache")
    val clearCache: Int = 0,
    @SerializedName("finish_page")
    val finishPage: Int = 0,
    val toast: String = ""
) {
    constructor() : this(
        "",
        "",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        "all",
        0,
        0,
        ""
    )

    fun valid(): Boolean {
        return className.isNotEmpty() || message.isNotEmpty() || stack.isNotEmpty()
    }
}