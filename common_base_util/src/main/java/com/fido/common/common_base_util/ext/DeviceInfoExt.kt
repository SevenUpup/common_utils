package com.fido.common.common_base_util.ext

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.provider.Settings
import android.text.TextUtils
import java.util.*

/**
@author FiDo
@description:
@date :2023/8/4 17:06
 */


/**
 * Android 6.0 开始获取 IMEI 需要权限，并且从 Android 10+ 开始官方取消了获取 IMEI 的 API，无法获取到 IMEI 了
 * 需要READ_PHONE_STATE权限
 */
@SuppressLint("MissingPermission")
fun getIMEI(context:Context):String{
    var id=""
    try{
        id = context.telephonyManager?.deviceId?:""
    }catch (_:Exception){}
    return id
}

/**
 * Android ID（SSAID）
 * 无需任何权限
卸载安装不会改变，除非刷机或重置系统
Android 8.0 之后签名不同的 APP 获取的 Android ID 是不一样的
部分设备由于制造商错误实现，导致多台设备会返回相同的 Android ID
可能为空
 */
fun getAndroidId(context: Context): String {
    var androidId = ""
    try {
        // d55e1d246a7b4bba
        androidId = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    // 特殊处理，如果获取不到AndroidId或获取的值是返回同样的特定字符串
    if (TextUtils.isEmpty(androidId)
        || TextUtils.equals("000000000000000", androidId)
        || TextUtils.equals("00000000000000", androidId)
        || TextUtils.equals("9774d56d682e549c", androidId)
    ) {
        androidId = UUID.randomUUID().toString()
    }
    return androidId
}

/**
 * Widevine ID
DRM 数字版权管理 ID ，访问此 ID 无需任何权限
对于搭载 Android 8.0 的设备，Widevine 客户端 ID 将为每个应用软件包名称和网络源（对于网络浏览器）返回一个不同的值
可能为空
 */
fun getWidevineID(): String {
    try {
        val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        val mediaDrm = MediaDrm(WIDEVINE_UUID)
        val widevineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
        val sb = StringBuilder()
        for (byte in widevineId) {
            sb.append(String.format("%02x", byte))
        }
        return sb.toString()
    } catch (_: Exception) {
    } catch (_: Error) { }
    return ""
}



