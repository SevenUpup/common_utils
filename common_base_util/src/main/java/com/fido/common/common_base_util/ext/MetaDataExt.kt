package com.fido.common.common_base_util.ext

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentProvider
import android.content.pm.PackageManager
import com.fido.common.common_base_util.app

/**
@author FiDo
@description:
@date :2023/5/18 9:43
 */
fun applicationMetaDataOf(name: String): String? =
    app.packageManager.getApplicationInfo(
        packageName,
        PackageManager.GET_META_DATA
    ).metaData.getString(name)


inline fun <reified T : Activity> activityMetaDataOf(name: String): String? =
    app.packageManager.getActivityInfo(
        ComponentName<T>(),
        PackageManager.GET_META_DATA
    ).metaData.getString(name)

inline fun <reified T : Service> serviceMetaDataOf(name: String): String? =
    app.packageManager.getServiceInfo(
        ComponentName<T>(),
        PackageManager.GET_META_DATA
    ).metaData.getString(name)

inline fun <reified T : ContentProvider> providerMetaDataOf(name: String): String? =
    app.packageManager.getProviderInfo(
        ComponentName<T>(),
        PackageManager.GET_META_DATA
    ).metaData.getString(name)

inline fun <reified T : BroadcastReceiver> receiverMetaDataOf(name: String): String? =
    app.packageManager.getReceiverInfo(
        ComponentName<T>(),
        PackageManager.GET_META_DATA
    ).metaData.getString(name)

@PublishedApi
internal inline fun <reified T> ComponentName() = ComponentName(app, T::class.java)