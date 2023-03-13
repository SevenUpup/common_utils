package com.fido.common.common_base_ui.ext.image_select

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.luck.picture.lib.R
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.utils.StyleUtils
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import java.io.File

/**
 * 自定义裁剪
 */
internal class ImageFileCropEngine private constructor(
    private val ratioX: Float,
    private val ratioY: Float
) : CropFileEngine {

    companion object {
        private var instance: ImageFileCropEngine? = null
        fun createImageCropEngine(
            ratioX: Float,
            ratioY: Float
        ): ImageFileCropEngine? {
            if (null == instance) {
                synchronized(ImageFileCropEngine::class.java) {
                    if (null == instance) {
                        instance = ImageFileCropEngine(ratioX, ratioY)
                    }
                }
            }
            return instance
        }
    }

    override fun onStartCrop(
        fragment: Fragment,
        srcUri: Uri?,
        destinationUri: Uri?,
        dataSource: ArrayList<String?>?,
        requestCode: Int
    ) {
        val options: UCrop.Options =
            buildOptions(fragment.requireContext(), rationX = ratioX, rationY = ratioY)
        val uCrop = UCrop.of(srcUri!!, destinationUri!!, dataSource)
        uCrop.withOptions(options)
        uCrop.setImageEngine(object : UCropImageEngine {
            override fun loadImage(context: Context, url: String, imageView: ImageView) {
                if (!ImageLoaderUtils.assertValidRequest(context)) {
                    return
                }
                Glide.with(context).load(url).override(180, 180).into(imageView)
            }

            override fun loadImage(
                context: Context,
                url: Uri,
                maxWidth: Int,
                maxHeight: Int,
                call: UCropImageEngine.OnCallbackListener<Bitmap>?
            ) {
                Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            call?.onCall(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            call?.onCall(null)
                        }
                    })
            }
        })
        uCrop.start(fragment.requireActivity(), fragment, requestCode)
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private fun getSandboxPath(context: Context): String {
        val externalFilesDir: File? = context.getExternalFilesDir("")
        val customFile = File(externalFilesDir?.absolutePath, "Sandbox")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     */
    private fun buildOptions(
        context: Context,
        rationX: Float = 1f,
        rationY: Float = 1f,
        isCircleLayer: Boolean = false,
        isShowCropGrid: Boolean = false,
        skipCropMimeTypes: Array<String> = arrayOf(
            PictureMimeType.ofWEBP(),
            PictureMimeType.ofGIF()
        )
    ): UCrop.Options {
        val options = UCrop.Options()
        options.setHideBottomControls(true) // 是否显示uCrop工具栏，默认不显示
        options.setFreeStyleCropEnabled(false) // 裁剪框是否可拖拽
        options.setShowCropFrame(!isCircleLayer)  // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
        options.setShowCropGrid(isShowCropGrid)
        options.setCircleDimmedLayer(isCircleLayer)
        options.withAspectRatio(rationX, rationY)
        options.setCropOutputPathDir(getSandboxPath(context))
        options.isCropDragSmoothToCenter(false)
        options.setSkipCropMimeType(*skipCropMimeTypes)
        options.isForbidCropGifWebp(false)
        options.isForbidSkipMultipleCrop(true)
        options.setMaxScaleMultiplier(100f)
        if (PictureSelectionConfig.selectorStyle != null && PictureSelectionConfig.selectorStyle.selectMainStyle.statusBarColor != 0) {
            val mainStyle = PictureSelectionConfig.selectorStyle.selectMainStyle
            val isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack
            val statusBarColor = mainStyle.statusBarColor
            options.isDarkStatusBarBlack(isDarkStatusBarBlack)
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor)
                options.setToolbarColor(statusBarColor)
            } else {
                options.setStatusBarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
                options.setToolbarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            }
            val titleBarStyle = PictureSelectionConfig.selectorStyle.titleBarStyle
            if (StyleUtils.checkStyleValidity(titleBarStyle.titleTextColor)) {
                options.setToolbarWidgetColor(titleBarStyle.titleTextColor)
            } else {
                options.setToolbarWidgetColor(
                    ContextCompat.getColor(
                        context,
                        R.color.ps_color_white
                    )
                )
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            options.setToolbarColor(ContextCompat.getColor(context, R.color.ps_color_grey))
            options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.ps_color_white))
        }
        return options
    }

}