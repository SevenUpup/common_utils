package com.fido.common.common_base_ui.ext.permission

import android.content.Context
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import java.util.ArrayList
import com.fido.common.common_base_ui.R
import com.permissionx.guolindev.dialog.RationaleDialog

internal const val positiveText = "允许"
internal const val nagetiveText = "拒绝"

fun FragmentActivity.extRequestPermission(
    permissions: List<String>,
    explainTitleBeforeRequest: String = "",
    explainDialog:RationaleDialog?=null,
    block: () -> Unit
) {
    PermissionX.init(this).permissions(permissions).apply {
        if (explainTitleBeforeRequest.isNotEmpty()) {
            explainReasonBeforeRequest()
            onExplainRequestReason { scope, deniedList, beforeRequest ->
                if (explainDialog != null) {
                    scope.showRequestReasonDialog(explainDialog)
                } else {
                    scope.showRequestReasonDialog(
                        deniedList,
                        explainTitleBeforeRequest,
                        positiveText,
                        nagetiveText
                    )
                }
            }
        }
    }
        .onForwardToSettings { scope, deniedList ->
            val message = getPermissionHint(this, deniedList)
            if (message?.isNotEmpty() == true) {
                scope.showForwardToSettingsDialog(deniedList, message, positiveText, nagetiveText)
            }
        }
        .request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                block()
            }
        }
}

fun FragmentActivity.extRequestPermission(vararg permission:String,block: () -> Unit){
    extRequestPermission(listOf(*permission), block = block)
}

fun Fragment.extRequestPermission(vararg permissions: String, block: () -> Unit) {
    extRequestPermission(listOf(*permissions), block = block)
}

fun Fragment.extRequestPermission(
    permissions: List<String>,
    explainTitleBeforeRequest: String = "",
    explainDialog:RationaleDialog?=null,
    block: () -> Unit,
) = activity?.extRequestPermission(permissions, explainTitleBeforeRequest,explainDialog, block)

/**
 * 根据权限获取提示
 */
private fun getPermissionHint(context: Context, permissions: List<String?>?): String? {
    if (permissions == null || permissions.isEmpty()) {
        return context.getString(R.string.common_permission_fail_2)
    }
    val hints: MutableList<String> = ArrayList()
    for (permission in permissions) {
        when (permission) {
            Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE, Permissions.MANAGE_EXTERNAL_STORAGE -> {
                val hint = context.getString(R.string.common_permission_storage)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.CAMERA -> {
                val hint = context.getString(R.string.common_permission_camera)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.RECORD_AUDIO -> {
                val hint = context.getString(R.string.common_permission_microphone)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.ACCESS_FINE_LOCATION, Permissions.ACCESS_COARSE_LOCATION, Permissions.ACCESS_BACKGROUND_LOCATION -> {
                val hint: String = if (!permissions.contains(Permissions.ACCESS_FINE_LOCATION) &&
                    !permissions.contains(Permissions.ACCESS_COARSE_LOCATION)
                ) {
                    context.getString(R.string.common_permission_location_background)
                } else {
                    context.getString(R.string.common_permission_location)
                }
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.BLUETOOTH_SCAN, Permissions.BLUETOOTH_CONNECT, Permissions.BLUETOOTH_ADVERTISE -> {
                if (Build.VERSION.SDK_INT >= 31) {
                    val hint = context.getString(R.string.common_permission_bluetooth)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
            }
            Permissions.READ_PHONE_STATE, Permissions.CALL_PHONE, Permissions.ADD_VOICEMAIL, Permissions.USE_SIP, Permissions.READ_PHONE_NUMBERS, Permissions.ANSWER_PHONE_CALLS -> {
                val hint = context.getString(R.string.common_permission_phone)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.GET_ACCOUNTS, Permissions.READ_CONTACTS, Permissions.WRITE_CONTACTS -> {
                val hint = context.getString(R.string.common_permission_contacts)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.READ_CALENDAR, Permissions.WRITE_CALENDAR -> {
                val hint = context.getString(R.string.common_permission_calendar)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.READ_CALL_LOG, Permissions.WRITE_CALL_LOG, Permissions.PROCESS_OUTGOING_CALLS -> {
                val hint =
                    context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.common_permission_call_log else R.string.common_permission_phone)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.BODY_SENSORS -> {
                val hint = context.getString(R.string.common_permission_sensors)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.ACTIVITY_RECOGNITION -> {
                val hint = context.getString(R.string.common_permission_activity_recognition)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.SEND_SMS, Permissions.RECEIVE_SMS, Permissions.READ_SMS, Permissions.RECEIVE_WAP_PUSH, Permissions.RECEIVE_MMS -> {
                val hint = context.getString(R.string.common_permission_sms)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.REQUEST_INSTALL_PACKAGES -> {
                val hint = context.getString(R.string.common_permission_install)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.SYSTEM_ALERT_WINDOW -> {
                val hint = context.getString(R.string.common_permission_window)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.WRITE_SETTINGS -> {
                val hint = context.getString(R.string.common_permission_setting)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.NOTIFICATION_SERVICE -> {
                val hint = context.getString(R.string.common_permission_notification)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            Permissions.PACKAGE_USAGE_STATS -> {
                val hint = context.getString(R.string.common_permission_task)
                if (!hints.contains(hint)) {
                    hints.add(hint)
                }
            }
            else -> {
            }
        }
    }
    if (hints.isNotEmpty()) {
        val builder = StringBuilder()
        for (text in hints) {
            if (builder.isEmpty()) {
                builder.append(text)
            } else {
                builder.append("、")
                    .append(text)
            }
        }
        builder.append(" ")
        return context.getString(R.string.common_permission_fail_3, builder.toString())
    }
    return context.getString(R.string.common_permission_fail_2)
}
