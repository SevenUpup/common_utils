package com.fido.common.common_base_ui.ext.image_select

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fido.common.common_base_ui.ext.image_select.ImageFileCompressEngine.Companion.createImageCompressEngine
import com.fido.common.common_base_ui.ext.image_select.ImageFileCropEngine.Companion.createImageCropEngine
import com.fido.common.common_base_ui.ext.image_select.ImageSelectGlideEngine.Companion.createGlideEngine
import com.fido.common.common_base_ui.ext.image_select.MeSandboxFileEngine.Companion.createSandboxFileEngine
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.*
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.permissions.PermissionChecker
import com.luck.picture.lib.utils.*

/**
 * 返回选择图片的file path
 */
val ArrayList<LocalMedia>.selectImagesPath
    get() = this.mapNotNull { it.compressPath }.ifEmpty { this.map { it.realPath } }

/**
 * 图片选择的引擎类
 * 用于开启图片选择相关，压缩相关，裁剪相关
 *
 * openImageSelect -  开启图库的选择（选择数量，是否裁剪，是否压缩）
 * openCamera  -  开启相机的选择 (是否裁剪，是否压缩)
 */
fun FragmentActivity.openImageSelect(
    selected: List<LocalMedia>?,
    selectNum: Int = 1,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Float = 1f,  //裁剪比例
    ratioY: Float = 1f,
    clickSound: Boolean = false,
    call: OnResultCallbackListener<LocalMedia>,
) {
    PictureSelector.create(this).openGallery(SelectMimeType.ofImage()).apply {
        setImageEngine(createGlideEngine())
        setLanguage(LanguageConfig.CHINESE)
        setSelectedData(selected)
        setMaxSelectNum(selectNum)
        isPreviewImage(true) // 是否可预览图片
        isPreviewVideo(true) // 是否可预览视频
        isPreviewAudio(true) // 是否可播放音频
        isEmptyResultReturn(true) //支持未选择返回
        setSelectionMode(if (selectNum > 1) SelectModeConfig.MULTIPLE else SelectModeConfig.SINGLE)
        isOpenClickSound(clickSound)
        if (canCompress) setCompressEngine(createImageCompressEngine())
        if (canCrop) setCropEngine(createImageCropEngine(ratioX, ratioY))
//        setSelectLimitTipsListener(MeOnSelectLimitTipsListener())
//        setAddBitmapWatermarkListener(getAddBitmapWatermarkListener())
//        setVideoThumbnailListener(getVideoThumbnailEventListener())
//        setCustomLoadingListener(OnCustomLoadingListener {
//        })
        isOriginalControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
//        setPermissionDescriptionListener(getPermissionDescriptionListener())
        setSandboxFileEngine(createSandboxFileEngine())
        forResult(call)
    }
}

fun FragmentActivity.openCamera(
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Float = 1f,  //裁剪比例
    ratioY: Float = 1f,
    call: OnResultCallbackListener<LocalMedia>,
) {
    PictureSelector.create(this)
        .openCamera(SelectMimeType.ofImage()).apply {
//            setCameraInterceptListener(getCustomCameraEvent())
//            setRecordAudioInterceptListener(MeOnRecordAudioInterceptListener())
            if (canCompress) createImageCompressEngine()
            if (canCrop) createImageCropEngine(ratioX, ratioY)
            setLanguage(LanguageConfig.CHINESE)
            setSandboxFileEngine(createSandboxFileEngine())
            isOriginalControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
//            setPermissionDescriptionListener(getPermissionDescriptionListener())
//            setOutputAudioDir(getSandboxAudioOutputPath())
            forResult(call)
        }
}

fun Fragment.openImageSelect(
    selected: List<LocalMedia>?,
    selectNum: Int = 1,
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Float = 1f,  //裁剪比例
    ratioY: Float = 1f,
    clickSound: Boolean = false,
    call: OnResultCallbackListener<LocalMedia>,
) {
    activity?.openImageSelect(
        selected,
        selectNum,
        canCrop,
        canCompress,
        ratioX,
        ratioY,
        clickSound,
        call
    )
}

fun Fragment.openCamera(
    canCrop: Boolean = false,
    canCompress: Boolean = true,
    ratioX: Float = 1f,  //裁剪比例
    ratioY: Float = 1f,
    call: OnResultCallbackListener<LocalMedia>,
) {
    activity?.openCamera(canCrop, canCompress, ratioX, ratioY,call)
}

/**
 * 清理图片选择，裁剪，压缩相关的缓存文件
 */
fun Context.clearImageSelectCache() {
    if (PermissionChecker.checkSelfPermission(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        )
    ) {
        PictureFileUtils.deleteAllCacheDirFile(this)
        //清除 包名 - cache - 子目录 下 Luban 压缩的图片文件
        try {
            val cacheDir = externalCacheDir?.listFiles()?.find { it.name == ImageFileCompressEngine.EXTERNAL_CACHE_DIR_CHILD_DIR_NAME }
            if (cacheDir?.isDirectory == true) {
                cacheDir.listFiles()?.forEach { it.delete() }
            } else {
                cacheDir?.delete()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}

