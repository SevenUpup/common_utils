package com.fido.common.common_utils.sms

/**
 * @author: FiDo
 * @date: 2025/5/15
 * @des:
 */
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import android.widget.Toast
import com.fido.common.common_base_util.ext.toast

object SmsUtils {

    fun sendSms(context: Context, phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        val messageParts = smsManager.divideMessage(message)
        val sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
        val deliveredPendingIntent =
            PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), 0)
        for (part in messageParts) {
            smsManager.sendTextMessage(
                phoneNumber,
                null,
                part,
                sentPendingIntent,
                deliveredPendingIntent
            )
        }
    }

    // 监听短信发送状态
    fun registerSmsStatusReceiver(context: Context) {
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT)
                        .show()

                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                        context,
                        "Generic failure",
                        Toast.LENGTH_SHORT
                    ).show()

                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                        context,
                        "No service",
                        Toast.LENGTH_SHORT
                    ).show()
                    // 其他发送状态处理
                    else -> toast("error")
                }
            }
        }, IntentFilter("SMS_SENT"))
    }
}
