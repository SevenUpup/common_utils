package com.fido.common.common_base_util.ext

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Process
import android.provider.Settings
import androidx.core.content.pm.PackageInfoCompat
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.interfaces.BaseInitiallizer

/**
@author FiDo
@description:
@date :2023/5/18 10:38
 */
inline val packageName: String get() = app.packageName

inline val packageInfo: PackageInfo
    get() = app.packageManager.getPackageInfo(packageName, 0)

inline val appName: String
    get() = app.applicationInfo.loadLabel(app.packageManager).toString()

inline val appIcon: Drawable get() = packageInfo.applicationInfo.loadIcon(app.packageManager)

inline val appVersionName: String get() = packageInfo.versionName

inline val appVersionCode: Long get() = PackageInfoCompat.getLongVersionCode(packageInfo)

inline val isAppDebug: Boolean get() = app.isAppDebug

inline val Application.isAppDebug: Boolean
    get() = packageManager.getApplicationInfo(
        packageName,
        0
    ).flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

inline val isAppDarkMode: Boolean
    get() = (app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun launchAppSettings(): Boolean =
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .apply { data = Uri.fromParts("package", packageName, null) }
        .startForActivity()

fun relaunchApp(killProcess: Boolean = true) =
    app.packageManager.getLaunchIntentForPackage(packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)
        if (killProcess) Process.killProcess(Process.myPid())
    }

fun doOnAppStatusChanged(
    onForeGround:(Activity.()->Unit)?=null,
    onBackground:(Activity.()->Unit)?=null,
){
    doOnAppStatusChanged(object :OnAppStatusChangedListener{
        override fun onForeground(activity: Activity) {
            onForeGround?.invoke(activity)
        }

        override fun onBackground(activity: Activity) {
            onBackground?.invoke(activity)
        }
    })
}

fun doOnAppStatusChanged(listener: OnAppStatusChangedListener){
    BaseInitiallizer.onAppStatusChangedListener = listener
}

interface OnAppStatusChangedListener {

    fun onForeground(activity: Activity)

    fun onBackground(activity: Activity)
}