package com.fido.common.common_base_util.data_store

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fido.common.common_base_util.app

/**
 * @author: FiDo
 * @date: 2024/6/13
 * @des:
 */
open class DataStoreOwner(name: String = app.packageName) : IDataStoreOwner {
    private val Context.dataStore by preferencesDataStore(name)
    override val dataStore get() = context.dataStore
}

interface IDataStoreOwner {
    val context: Context get() = application
    val dataStore: DataStore<Preferences>

    companion object {
        internal var application: Application = app
    }
}