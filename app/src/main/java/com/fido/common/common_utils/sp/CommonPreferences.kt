package com.fido.common.common_utils.sp

import com.fido.common.common_base_util.util.sp.SharedPreferencesDelegate

/**
@author FiDo
@description:
@date :2023/8/9 11:01
 */
class CommonPreferences {

    companion object{
        const val DEULT_FILE_NAME = "Common_SP"
        const val DEULT_FILE_NAME2 = "Common_SP2"
    }

    var isFirst:Boolean by SharedPreferencesDelegate(DEULT_FILE_NAME,true,"isFirst")

    var userAge by SharedPreferencesDelegate(DEULT_FILE_NAME,18,"")

    //这里没有用key值，则会默认使用属性名来当做key值
    var userName by SharedPreferencesDelegate(DEULT_FILE_NAME2, "")

    //这里key 设置为  “”, 会默认使用属性名当key值
    var userHeight by SharedPreferencesDelegate(DEULT_FILE_NAME2,185.3f,"")

    fun <T>addValue(keyName:String,defultValue:T,spFileName:String = DEULT_FILE_NAME){
        var newValue by SharedPreferencesDelegate(spFileName,defultValue,keyName)
        newValue = defultValue
    }

}