package com.fido.common.common_utils.window_manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcWindowManagerBinding

/**
 * @author: HuTao
 * @date: 2026/4/1
 * @des:
 */
class WindowManagerAc: AppCompatActivity() {

    private val binding:AcWindowManagerBinding by binding()

    private var windowManager:WindowManager?=null
    private var floatingView: View?=null

    companion object {
        private const val REQUEST_CODE_OVERLAY_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btOpenWindowManager.throttleClick {
            if (checkOverlayPermission()) {
                showFloatingWindow()
            }else{
                requestOverlayPermission()
            }
        }

        binding.btCloseWindowManager.throttleClick {
            hideFloatingWindow()
        }
    }

    private fun showFloatingWindow() {
        if (floatingView!=null) return
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        floatingView = LayoutInflater.from(this).inflate(R.layout.dialog_test, null,false)

        floatingView?.throttleClick {
            toast("悬浮窗被点击")
        }
        // 配置 LayoutParams（关键！Android 14 推荐使用 TYPE_APPLICATION_OVERLAY）
        val params = WindowManager.LayoutParams(
            300,
            300,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        try {
            windowManager?.addView(floatingView, params)
        } catch (e: Exception) {
            Log.e("WindowManagerAc", "Failed to add floating window", e)
            toast("无法显示悬浮窗，请检查权限")
        }

    }

    private fun hideFloatingWindow() {
        try {
            floatingView?.let { view ->
                windowManager?.removeView(view)
                floatingView = null
            }
        } catch (e: IllegalArgumentException) {
            // View 已被移除
            floatingView = null
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        // 注意：部分 OEM（如小米、华为）可能不支持直接跳转，需引导用户手动进入
        try {
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
        } catch (e: ActivityNotFoundException) {
            // 降级：打开通用设置
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
            })
        }
    }

    private fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (checkOverlayPermission()) {
                showFloatingWindow()
            } else {
                toast("未授予悬浮窗权限，无法显示")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideFloatingWindow() // 避免内存泄漏
    }

}