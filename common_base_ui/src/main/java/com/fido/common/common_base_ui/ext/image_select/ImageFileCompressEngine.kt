package com.fido.common.common_base_ui.ext.image_select

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.utils.DateUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


/**
 * 自定义压缩
 */
internal class ImageFileCompressEngine private constructor() : CompressFileEngine {

    companion object {
        /**
         * 包名cache 目录下 子目录名称 不需要传入绝度路径(默认app名称)
         */
        var EXTERNAL_CACHE_DIR_CHILD_DIR_NAME = ""
        private var instance: ImageFileCompressEngine? = null

        fun createImageCompressEngine(compressDirPath: String = ""): ImageFileCompressEngine? {
            if (null == instance) {
                synchronized(ImageFileCompressEngine::class.java) {
                    if (null == instance) {
                        EXTERNAL_CACHE_DIR_CHILD_DIR_NAME = compressDirPath
                        instance = ImageFileCompressEngine()
                    }
                }
            }
            return instance
        }
    }

    override fun onStartCompress(
        context: Context?,
        source: ArrayList<Uri?>?,
        call: OnKeyValueResultCallbackListener?
    ) {
        Luban.with(context).load(source).ignoreBy(100)
            .setTargetDir(getExternalCacheChildName(context, EXTERNAL_CACHE_DIR_CHILD_DIR_NAME))
            .setRenameListener { filePath ->
                val indexOf = filePath.lastIndexOf(".")
                val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"
                DateUtils.getCreateFileName("CMP_") + postfix
            }.filter { path ->
                if (PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path)) {
                    true
                } else !PictureMimeType.isUrlHasGif(path)
            }.setCompressListener(object : OnNewCompressListener {
                override fun onStart() {}
                override fun onSuccess(source: String?, compressFile: File) {
                    call?.onCallback(source, compressFile.absolutePath)
                }

                override fun onError(source: String?, e: Throwable?) {
                    call?.onCallback(source, null)
                }
            }).launch()
    }

    private fun getExternalCacheChildName(
        context: Context?,
        externalCacheDirChildDirName: String
    ): String {
        try {
            EXTERNAL_CACHE_DIR_CHILD_DIR_NAME = externalCacheDirChildDirName.ifEmpty { getAppName(context!!)?:"" }
            val result = File(context?.externalCacheDir, EXTERNAL_CACHE_DIR_CHILD_DIR_NAME)
            if (!result.exists()) {
                result.mkdir()
            }
            return result.absolutePath
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getAppName(context: Context): String? {
        var name: String? = ""
        val packageManager = context.packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo =
                packageManager.getApplicationInfo(context.applicationInfo.packageName, 0)
            name =
                (packageManager.getApplicationLabel(applicationInfo)) as String?
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return name
    }

}