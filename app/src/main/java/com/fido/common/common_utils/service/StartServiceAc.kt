package com.fido.common.common_utils.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.currentTimeString
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_utils.test.Job
import com.fido.common.databinding.AcStartServiceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.concurrent.timer

/**
 * @author: HuTao
 * @date: 2026/7/28
 * @des:
 */
class StartServiceAc : AppCompatActivity() {
    private val TAG = "StartServiceAc"

    private val binding: AcStartServiceBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btStartService.throttleClick {
                testStartService()
            }
            btBindService.throttleClick {
                testBindService()
            }
            btStartBind.throttleClick {
                testMixedService()
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: Service 绑定成功")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // ⚠️ 只有远程 Service 进程死亡时才会调用
            Log.e(TAG, "onServiceDisconnected: Service 意外断开（进程死亡）")
        }
    }

    fun testStartService() {
        Log.d(TAG, "testStartService: ")
        runCatching {
            startService(Intent(this, MyService::class.java))
        }.onFailure {
            Log.e(TAG, "startService failed: ${it.message}")
        }
        // 杀死 App 进程 → Service 仍在运行
    }

    fun testBindService() {
        Log.d(TAG, "testBindService: 使用 bindService，不会调用 onStartCommand，也不会启动前台服务")
        runCatching {
            val intent = Intent(this, MyService::class.java)
            val result = bindService(intent, connection, BIND_AUTO_CREATE)
            Log.d(TAG, "bindService 结果: $result")
        }.onFailure {
            Log.e(TAG, "testBindService failed: ${it.message}", it)
        }
        // 杀死 App 进程 → Service 被销毁（因为没有 startService 保活）
    }

    fun testMixedService() {
        Log.d(TAG, "testMixedService: ")
        runCatching {
            startService(Intent(this, MyService::class.java))
            bindService(Intent(this, MyService::class.java), connection, BIND_AUTO_CREATE)
        }.onFailure {
            Log.e(TAG, "testMixedService failed: ${it.message}", )
        }
        // 杀死 App 进程 → Service 仍在运行（startService 保活）
    }
}


class MyService : Service() {
    private val CHANNEL_ID = "service_channel"
    private val CHANNEL_NAME = "My Service Channel"
    private val TAG = "StartServiceAc-MyService"
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var timerJob: kotlinx.coroutines.Job? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate called ")
        
        // 创建通知渠道（Android 8.0+ 必需）
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Channel for MyService"
                }
                val notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }
            startTimer()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate failed: ", e)
        }
    }

    private fun startTimer() {
        timerJob = serviceScope.launch {
            while (serviceScope.isActive) {
                Log.d(TAG, "timer called ${currentTimeString()}")
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand intent:$intent flags:$flags startId:$startId")

        try {// 构建正确的通知
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service 运行中")
                .setContentText("Service is running in foreground")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // 必须设置 smallIcon
                .setTicker("Service 运行中")
                .setOngoing(true)
                .build()

            startForegroundService(notification)// 提升为前台 Service
        } catch (e: Exception) {
            Log.e(TAG, "onStartCommand failed: ", e)
        }
        return START_STICKY  // 被杀后自动重启
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind called")
        // bindService 必须返回 IBinder，返回 null 会导致绑定失败
        // 如果不需要通信，返回一个空的 Binder
        return android.os.Binder()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        serviceScope.cancel()
    }

    private fun startForegroundService(notification: Notification) {
        Log.d(TAG, "startForegroundService called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        } else {
            startForeground(1, notification);
        }
    }

}