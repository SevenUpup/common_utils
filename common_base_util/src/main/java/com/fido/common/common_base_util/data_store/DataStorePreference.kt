package com.fido.common.common_base_util.data_store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @author: FiDo
 * @date: 2024/6/13
 * @des:
 */
/**
 * 默认的 DataStore 操作类 (Core)
 */
open class DataStorePreference<V>(
    private val dataStore: DataStore<Preferences>,  //DataStore对象
    val key: Preferences.Key<V>, //存储key
    open val default: V?   //存储的Value默认值
) {

    //KV的存储实现，通过 implicit receiver 特性简化this，存入新的值
    suspend fun put(block: suspend V?.(Preferences) -> V?): Preferences =
        dataStore.edit { preferences ->
            val value = block(preferences[key] ?: default, preferences)
            if (value == null) {
                preferences.remove(key)
            } else {
                preferences[key] = value
            }
        }

    //设置KV的快速入口
    suspend fun put(value: V?): Preferences = put { value }

    //转换为Flow对象接收
    fun asFlow(): Flow<V?> =
        dataStore.data.map { it[key] ?: default }

    //转换为LiveData对象接收
//    fun asLiveData(): LiveData<V?> = asFlow().asLiveData()

    //获取通过Flow获取到存储的Value
    suspend fun get(): V? = asFlow().first()

    //移除Key
    suspend fun remove(): Preferences = dataStore.edit { preferences ->
        preferences.remove(key)
    }
}
