package com.fido.common.common_base_ui.ext.imageview

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


/**
@author FiDo
@description: 根据图片url 下载图片并保存本地
@date :2023/6/6 11:40
 */

fun savePic2Gallery(imagePath: Any, context: Context, fileName: String, quality: Int = 100){
    loadBitmapFormPath(context,imagePath){
        it?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveQUp(it, context, fileName, quality)
            } else {
                saveQNext(it, context, fileName, quality)
            }
        }
    }
}

// Android Q 以下
internal fun saveQNext(image: Bitmap, context: Context, fileName: String, quality: Int = 100) {
    val path: String = (Environment.getExternalStorageDirectory().absolutePath + File.separator).toString() + "image"
    // 创建文件夹
    createOrExistsDir(path)
    // 文件名称
    val file = File(path, fileName)
    try {
        val fos = FileOutputStream(file)
        // 通过io流的方式来压缩保存图片
        image.compress(Bitmap.CompressFormat.JPEG, quality, fos)
        fos.flush()
        fos.close()
        // 保存图片后发送广播通知更新数据库
        val uri: Uri = Uri.fromFile(file)
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

// Android Q 以上
@RequiresApi(api = Build.VERSION_CODES.Q)
internal fun saveQUp(image: Bitmap, context: Context, fileName: String, quality: Int = 80) {
    // 文件夹路径
    val imageSaveFilePath = Environment.DIRECTORY_DCIM + File.separator + "image"
    createOrExistsDir(imageSaveFilePath)
    // 文件名字
    val contentValues = ContentValues()
    contentValues.put(MediaStore.MediaColumns.TITLE, fileName)
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    contentValues.put(MediaStore.MediaColumns.DATE_TAKEN, fileName)
    //该媒体项在存储设备中的相对路径，该媒体项将在其中保留
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, imageSaveFilePath)
    var uri: Uri? = null
    var outputStream: OutputStream? = null
    val localContentResolver = context.contentResolver
    try {
        uri =
            localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        outputStream = localContentResolver.openOutputStream(uri!!)
        // Bitmap图片保存
        // 1、宽高比例压缩
        // 2、压缩参数
        image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream?.flush()
        outputStream?.close()
    } catch (e: Exception) {
        e.printStackTrace()
        if (uri != null) {
            localContentResolver.delete(uri, null, null)
        }
    } finally {
        image.recycle()
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

private fun createOrExistsDir(path:String){
    if (path.isNotBlank()) {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}