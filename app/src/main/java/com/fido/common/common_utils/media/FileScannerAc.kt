package com.fido.common.common_utils.media

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.media.FileScanner
import com.fido.common.databinding.AcFileScannerBinding

/**
 * @author: HuTao
 * @date: 2026/7/28
 * @des:  FileScanner 的设计目的是触发系统媒体库扫描，而不是返回扫描到的文件数据。它调用 MediaScannerConnection.scanFile() 让系统去更新媒体数据库。
 */
class FileScannerAc: AppCompatActivity() {
    private val TAG = "FileScannerAc"

    private val binding: AcFileScannerBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FileScanner.registerMediaObserver(this)

        binding.btStart.throttleClick {
            val checked = checkMediaPermission()
            Log.d(TAG, "onCreate checked: $checked")
            if (checked) {
                val inputText = binding.input.text.toString()
                FileScanner.scanFiles(this@FileScannerAc, paths = if (!inputText.isEmpty()) arrayOf(inputText) else null, deepScan = true)
            }
        }

    }

    private fun checkMediaPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
        }else{
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FileScanner.cleanup(this)
    }

}