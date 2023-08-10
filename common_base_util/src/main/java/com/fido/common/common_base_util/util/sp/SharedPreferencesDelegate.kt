package com.fido.common.common_base_util.util.sp

import android.content.Context
import android.content.SharedPreferences
import com.fido.common.common_base_util.app
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
@author FiDo
@description:
@date :2023/8/9 10:28
 * @param spName SP存入的XML名字
 * @param defaultValue 默认值
 * @param key 存取数据时对应的key
 * */
class SharedPreferencesDelegate<T>(
//    private val context:Context,
    private val spName:String,
    private val defaultValue:T,
    private val key:String?=null,
) :ReadWriteProperty<Any?,T>{

    private val sp:SharedPreferences by lazy {
        app.getSharedPreferences(spName,Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>):T{
        val finalKey = if(key.isNullOrBlank()) property.name else key
        return when (defaultValue) {
            is Double -> sp.getFloat(finalKey,defaultValue.toFloat())
            is Int -> sp.getInt(finalKey, defaultValue)
            is Long -> sp.getLong(finalKey, defaultValue)
            is Float -> sp.getFloat(finalKey, defaultValue)
            is Boolean -> sp.getBoolean(finalKey, defaultValue)
            is String -> sp.getString(finalKey, defaultValue)
            is Set<*> -> sp.getStringSet(finalKey, defaultValue as? Set<String>)
            else -> throw IllegalStateException("Unsupported type")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val finalKey = if(key.isNullOrBlank()) property.name else key
        with(sp.edit()) {
            try{
                when (value) {
                    is Double -> putFloat(finalKey,value.toFloat())
                    is Int -> putInt(finalKey, value)
                    is Long -> putLong(finalKey, value)
                    is Float -> putFloat(finalKey, value)
                    is Boolean -> putBoolean(finalKey, value)
                    is String -> putString(finalKey, value)
                    is Set<*> -> putStringSet(finalKey, value.map { it.toString() }.toHashSet())
                    else -> throw IllegalStateException("Unsupported type")
                }
                //apply()是异步写入数据   commit()是同步写入数据
                apply()
            }catch (_:Exception){}
        }
    }



}