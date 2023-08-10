package com.fido.common.common_base_util.util.sp

/**
@author FiDo
@description:
@date :2023/8/9 17:26
 */

inline fun <reified T> String.spValue(defaultValue: T, spName: String = CommonSharedPrefs.DEFULT_SP_FILE_NAME) =
    CommonSharedPrefs.getInstance().getSpValue(this, defaultValue, spName)

/**
 * @param keyName 需要put 的 key 名称
 * @param spName sharedpreference的xml名
 * @see CommonSharedPrefs.DEFULT_SP_FILE_NAME (默认的spFileName，可以修改)
 */
fun Any.putSp(keyName:String,spName: String = CommonSharedPrefs.DEFULT_SP_FILE_NAME) =
    CommonSharedPrefs.getInstance().putSpValue(keyName,this,spName)

/**
 * 删除sp下对应的key和value
 */
fun removeSp(spKey:String,spName: String = CommonSharedPrefs.DEFULT_SP_FILE_NAME) = CommonSharedPrefs.getInstance().removeSp(spKey,spName)

/**
 * 清空sp
 */
fun clearSp(spName: String = CommonSharedPrefs.DEFULT_SP_FILE_NAME) = CommonSharedPrefs.getInstance().clearSp(spName)
