package com.fido.common.common_base_util.data_store

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

/**
 * @author: FiDo
 * @date: 2024/6/13
 * @des:
 */

//别名
typealias EasyStore = EasyDataStore

object EasyDataStore : DataStoreOwner() {
    suspend fun <V> put(key: String, value: V) {
        when (value) {
            is Long -> putLongData(key, value)
            is String -> putStringData(key, value)
            is Int -> putIntData(key, value)
            is Boolean -> putBooleanData(key, value)
            is Float -> putFloatData(key, value)
            is Double -> putDoubleData(key, value)
//            is ByteArray -> putByteArrayData(key, value)
            is Set<*> -> {
                val set = value as Set<*>
                if (set.isEmpty() || set.first() is String) {
                    putStringSetData(key, set as Set<String>)
                } else {
                    throw IllegalArgumentException("This type can't be saved into DataStore")
                }
            }

            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    /**
     * 取数据
     */
    @Suppress("IMPLICIT_CAST_TO_ANY")
    suspend fun <V> get(key: String, defaultValue: V): V {
        val data = when (defaultValue) {
            is Int -> getIntData(key, defaultValue)
            is Long -> getLongData(key, defaultValue)
            is String -> getStringData(key, defaultValue)
            is Boolean -> getBooleanData(key, defaultValue)
            is Float -> getFloatData(key, defaultValue)
            is Double -> getDoubleData(key, defaultValue)
//            is ByteArray -> getByteArrayData(key, defaultValue)
            is Set<*> -> {
                val set = defaultValue as Set<*>
                if (set.isEmpty() || set.first() is String) {
                    getStringSetData(key, set as Set<String>)
                } else {
                    throw IllegalArgumentException("This value cannot be get form the Data Store")
                }
            }

            else -> throw IllegalArgumentException("This value cannot be get form the Data Store")
        }
        return data as V
    }

    /**
     * 移除数据
     */
    suspend inline fun <reified V> remove(key: String) {
        when (V::class) {
            Int::class -> removeIntData(key)
            Long::class -> removeLongData(key)
            String::class -> removeStringData(key)
            Boolean::class -> removeBooleanData(key)
            Float::class -> removeFloatData(key)
            Double::class -> removeDoubleData(key)
//            ByteArray::class -> removeByteArrayData(key)
            Set::class -> removeStringSetData(key)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    /**
     * 移除Int数据
     */
    suspend fun removeIntData(key: String) {
        DataStorePreference(dataStore, intPreferencesKey(key), null).remove()
    }

    /**
     * 移除Long数据
     */
    suspend fun removeLongData(key: String) {
        DataStorePreference(dataStore, longPreferencesKey(key), null).remove()
    }

    /**
     * 移除String数据
     */
    suspend fun removeStringData(key: String) {
        DataStorePreference(dataStore, stringPreferencesKey(key), null).remove()
    }

    /**
     * 移除Boolean数据
     */
    suspend fun removeBooleanData(key: String) {
        DataStorePreference(dataStore, booleanPreferencesKey(key), null).remove()
    }

    /**
     * 移除Float数据
     */
    suspend fun removeFloatData(key: String) {
        DataStorePreference(dataStore, floatPreferencesKey(key), null).remove()
    }

    /**
     * 移除Double数据
     */
    suspend fun removeDoubleData(key: String) {
        DataStorePreference(dataStore, doublePreferencesKey(key), null).remove()
    }

    /**
     * 移除ByteArray数据
     */
//    suspend fun removeByteArrayData(key: String) {
//        DataStorePreference(dataStore, byteArrayPreferencesKey(key), null).remove()
//    }

    /**
     * 移除StringSet数据
     */
    suspend fun removeStringSetData(key: String) {
        DataStorePreference(dataStore, stringSetPreferencesKey(key), null).remove()
    }

    /**
     * 取出Int数据
     */
    private suspend fun getIntData(key: String, default: Int = 0): Int =
        DataStorePreference(dataStore, intPreferencesKey(key), default).get() ?: default

    /**
     * 取出Long数据
     */
    private suspend fun getLongData(key: String, default: Long = 0L): Long =
        DataStorePreference(dataStore, longPreferencesKey(key), default).get() ?: default

    /**
     * 取出String数据
     */
    private suspend fun getStringData(key: String, default: String = ""): String =
        DataStorePreference(dataStore, stringPreferencesKey(key), default).get() ?: default

    /**
     * 取出Boolean数据
     */
    private suspend fun getBooleanData(key: String, default: Boolean = false): Boolean =
        DataStorePreference(dataStore, booleanPreferencesKey(key), default).get() ?: default

    /**
     * 取出Float数据
     */
    private suspend fun getFloatData(key: String, default: Float = 0F): Float =
        DataStorePreference(dataStore, floatPreferencesKey(key), default).get() ?: default

    /**
     * 取出Double数据
     */
    private suspend fun getDoubleData(key: String, default: Double = 0.0): Double =
        DataStorePreference(dataStore, doublePreferencesKey(key), default).get() ?: default

    /**
     * 取出ByteArray数据
     */
//    private suspend fun getByteArrayData(key: String, default: ByteArray = ByteArray(0)): ByteArray =
//        DataStorePreference(dataStore, byteArrayPreferencesKey(key), default).get() ?: default

    /**
     * 取出StringSet数据
     */
    private suspend fun getStringSetData(key: String, default: Set<String> = emptySet()): Set<String> =
        DataStorePreference(dataStore, stringSetPreferencesKey(key), default).get() ?: default


    /**
     * 存放Int数据
     */
    private suspend fun putIntData(key: String, value: Int) =
        DataStorePreference(dataStore, intPreferencesKey(key), value).put(value)

    /**
     * 存放Long数据
     */
    private suspend fun putLongData(key: String, value: Long) =
        DataStorePreference(dataStore, longPreferencesKey(key), value).put(value)

    /**
     * 存放String数据
     */
    private suspend fun putStringData(key: String, value: String) =
        DataStorePreference(dataStore, stringPreferencesKey(key), value).put(value)

    /**
     * 存放Boolean数据
     */
    private suspend fun putBooleanData(key: String, value: Boolean) =
        DataStorePreference(dataStore, booleanPreferencesKey(key), value).put(value)

    /**
     * 存放Float数据
     */
    private suspend fun putFloatData(key: String, value: Float) =
        DataStorePreference(dataStore, floatPreferencesKey(key), value).put(value)

    /**
     * 存放Double数据
     */
    private suspend fun putDoubleData(key: String, value: Double) =
        DataStorePreference(dataStore, doublePreferencesKey(key), value).put(value)

    /**
     * 存放ByteArray数据
     */
//    private suspend fun putByteArrayData(key: String, value: ByteArray) =
//        DataStorePreference(dataStore, byteArrayPreferencesKey(key), value).put(value)

    /**
     * 存放Set<String>数据
     */
    private suspend fun putStringSetData(key: String, value: Set<String>) =
        DataStorePreference(dataStore, stringSetPreferencesKey(key), value).put(value)


    /**
     * 清空数据（异步）
     */
    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}
