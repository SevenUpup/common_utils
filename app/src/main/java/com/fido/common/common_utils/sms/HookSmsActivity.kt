package com.fido.common.common_utils.sms

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.currentTimeString
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcHookSmsBinding


/**
 * @author: FiDo
 * @date: 2025/5/14
 * @des:
 */
class HookSmsActivity :AppCompatActivity(){

    private val binding:AcHookSmsBinding by binding()

    private val REQUEST_CODE_SMS_ROLE = 666

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
            startActivityForResult(intent, REQUEST_CODE_SMS_ROLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SMS_ROLE) {
            if (resultCode == Activity.RESULT_OK) {
                // 应用已设置为默认短信应用
                Log.d("SMS", "App is now the default SMS app");
            } else {
                // 用户拒绝 - 可能要提供策略来处理拒绝情况
                Log.e("SMS", "Failed to set app as default SMS app");
            }
        }
    }

    private fun initView() {
        binding.apply {
            btSmsPermission.click {
                requestSmsPermission()
            }

            btInsert.click {
                addSmsToInbox(this@HookSmsActivity,"111","测试的")
            }

            btSmsManagerSend.click {
                kotlin.runCatching {
                    SmsUtils.sendSms(this@HookSmsActivity,"15056025040","你好${currentTimeString()}")
                }.onFailure {
                    loge(it.message?:"")
                }
            }

        }
        SmsUtils.registerSmsStatusReceiver(this)
    }
    private val PERMISSIONS_REQUEST_SEND_SMS: Int = 1

    // 请求发送短信权限
    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(Manifest.permission.SEND_SMS),
                PERMISSIONS_REQUEST_SEND_SMS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 已授予发送短信权限，此处可以添加插入短信的代码
                toast("已授予发送短信权限")
            } else {
                // 没有授权发送短信权限，可以给出相应提示或者处理
                toast("没有授权发送短信权限")
            }
        }
    }

    /**
     * address: 发件人号码。
     * body: 短信内容。
     */
    private fun addSmsToInbox(context: Context, address: String, body: String) {
        val values = ContentValues()
        values.put("address", address)
        values.put("body", body)
        values.put("read", 0) // 0:未读, 1:已读

        try {
            context.contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values)
            Log.d("SMS", "Message added to inbox.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("SMS", "Failed to add message to inbox: " + e.message)
        }
    }

}

class MySmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            // 处理接收到的短信
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                if (pdus != null) {
                    for (pdu in pdus) {
                        val message: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        val sender: String = message.getDisplayOriginatingAddress()
                        val body: String = message.getMessageBody()


                        // 在这里处理收到的短信
                        Log.d("MySmsReceiver", "SMS received from $sender: $body")
                    }
                }
            }
        }
    }
}