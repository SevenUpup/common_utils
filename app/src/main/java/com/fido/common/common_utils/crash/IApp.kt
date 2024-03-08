package com.fido.common.common_utils.crash

import android.content.Context
import java.io.File

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des:
 */
interface IApp {

    fun showToast(context: Context, msg: String)
    fun cleanCache(context: Context)
    fun finishCurrentPage()
    fun getVersionName(context: Context): String

    fun donwloadFile(url: String): File?
    fun readStringFromCache(key : String): String
    fun writeStringToCache(file: File, content: String)

}