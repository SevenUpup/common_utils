package com.fido.common.common_base_ui.util

import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.ImageViewerPopupView
import com.lxj.xpopup.util.SmartGlideImageLoader

object ImagePreviewUtil {

    /**
     * 单图片的预览
     */
    fun singleImagePreview(
        context: Context,
        img: ImageView,
        imageUrl: String,
        placeholderRes: Int = 0,
        placeholderColor: Int = -1,
        isInfinite: Boolean = false,
        placeholderStroke: Int = -1,
        roundRadius: Int = 0,
        isShowSaveBtn: Boolean = false,
        bgColor: Int = Color.BLACK,
        longPressBlock: (popupView: BasePopupView, position: Int) -> Unit,
    ) {
        XPopup.Builder(context)
            .asImageViewer(
                img,
                imageUrl,
                isInfinite,
                placeholderColor,
                placeholderStroke,
                roundRadius,
                isShowSaveBtn,
                bgColor,
                SmartGlideImageLoader(placeholderRes),
                longPressBlock
            )
            .show()
    }

    /**
     * 多图的选择
     */
    fun multipleImagePreview(
        context: Context,
        img: ImageView,
        list: List<Any>,
        position: Int = 0,
        placeholderRes: Int = 0,
        placeholderColor: Int = 0,
        isInfinite: Boolean = false,
        isShowSaveBtn: Boolean = false,
        isShowPlaceholder: Boolean = true,
        placeholderStroke: Int = -1,
        placeholderRadius: Int = -1,
        bgColor: Int = Color.BLACK,
        srcViewUpdateBlock: (popupView: ImageViewerPopupView, position: Int) -> Unit,
        longPressBlock: (popupView: BasePopupView, position: Int) -> Unit,
    ) {
        XPopup.Builder(context)
            .asImageViewer(
                img,
                position,
                list,
                isInfinite,
                isShowPlaceholder,
                placeholderColor,
                placeholderStroke,
                placeholderRadius,
                isShowSaveBtn,
                bgColor,
                srcViewUpdateBlock,
                SmartGlideImageLoader(placeholderRes),
                longPressBlock
            )
            .show()
    }

}
