package com.fido.common.common_base_util.util.sp

import android.content.Context
import com.fido.common.common_base_util.app


/**
@author FiDo
@description:
@date :2023/8/9 16:39
 */
class CommonSharedPrefs private constructor() {

    companion object {
        var DEFULT_SP_FILE_NAME = "common_sharedprefs"

        @JvmStatic
        fun getInstance() = CommonSharedPrefsHolder.holder
    }

    private object CommonSharedPrefsHolder {
        var holder = CommonSharedPrefs()
    }

    fun <T> putSpValue(keyName: String, defaultValue: T, spFileName: String = DEFULT_SP_FILE_NAME) {
        var value by SharedPreferencesDelegate(spFileName, defaultValue, keyName)
        value = defaultValue
    }

    fun <T> getSpValue(
        keyName: String,
        defaultValue: T,
        spFileName: String = DEFULT_SP_FILE_NAME
    ): T {
        val value by SharedPreferencesDelegate(spFileName, defaultValue, keyName)
        return value
    }


    fun removeSp(key: String, spName: String = DEFULT_SP_FILE_NAME) {
        val sp = app.getSharedPreferences(spName, Context.MODE_PRIVATE)
        with(sp.edit()) {
            try {
                remove(key)
                apply()
            } catch (_: Exception) {
            }
        }
    }

    fun clearSp(spName: String = DEFULT_SP_FILE_NAME) {
        val sp = app.getSharedPreferences(spName, Context.MODE_PRIVATE)
        with(sp.edit()) {
            try {
                clear()
                apply()
            } catch (_: Exception) {
            }
        }
    }

}