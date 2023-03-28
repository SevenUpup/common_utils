package com.fido.common.common_base_util.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/*
  ---------- Context ----------
 */
/**
 * 获取应用的版本名称
 *
 * @param pkgName 包名
 * @return App版本号  ""表示失败
 */
fun Context.getVersionName(pkgName: String = packageName): String {
    if (pkgName.isBlank()) return ""
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取App版本码
 *
 * @param pkgName 包名
 * @return App版本码  -1表示失败
 */
fun Context.getVersionCode(pkgName: String = packageName): Int {
    if (pkgName.isBlank()) return -1
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}

/**
 * 安装app
 * @param file
 */
fun Context.installApp(file: File) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    var apkUri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0以上版本，需要配置权限才能安装未知来源的程序:本代码的处理是使用FileProvider读取Uri资源
        apkUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fido.common.common_base_util.fileprovider", file)
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    } else {
        apkUri = Uri.fromFile(file)
    }
    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    startActivity(intent)
}


/**
 * 判断App是否安装
 *
 * @param pkgName 包名
 * @return
 */
fun Context.isInstallApp(pkgName: String): Boolean {
    return pkgName.isNotBlank() && packageManager.getLaunchIntentForPackage(packageName) != null
}

/**
 * 是否是平板设备
 *
 * @return true 是， false 不是
 */
fun Context.isTablet(): Boolean {
    return this.resources.configuration.screenLayout and Configuration
        .SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

/*
  ---------- Fragment ----------
 */
fun Fragment.getVersionName(pkgName: String = requireActivity().packageName): String {
    return activity?.getVersionName(pkgName) ?: ""
}

fun Fragment.getVersionCode(pkgName: String = requireActivity().packageName): Int {
    return activity?.getVersionCode(pkgName) ?: -1
}

fun Fragment.installApp(file: File) {
    activity?.installApp(file)
}

fun Fragment.isInstallApp(pkgName: String): Boolean {
    return activity?.isInstallApp(pkgName) ?: false
}

fun Fragment.isTablet(): Boolean {
    return activity?.isTablet() ?: false
}
