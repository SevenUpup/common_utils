package com.fido.common.common_base_ui.util

import android.content.Context
import android.view.Gravity
import androidx.annotation.GravityInt
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

fun Context.showLoading(title: CharSequence = ""): BasePopupView =
    XPopup.Builder(this).asLoading(title).show()

fun Context.creatXPopCustomView(popupView: BasePopupView) = XPopup.Builder(this).apply {
    asCustom(popupView)
}

fun Context.showNormalInputDialog(
    title: CharSequence? = null,
    content: CharSequence? = null,
    inputContent: CharSequence? = null,
    hint: CharSequence? = null,
    autoOpenSoftInput: Boolean = true,
    isDarkTheme: Boolean = false,
    confirmBlock: (String) -> Unit,
) = XPopup.Builder(this).run {
    isDestroyOnDismiss(true)
    autoOpenSoftInput(autoOpenSoftInput)
    isDarkTheme(isDarkTheme)
    asInputConfirm(title, content, inputContent, hint) {
        confirmBlock.invoke(it)
    }.show()
}

fun Context.showNormalListDialog(
    data: List<String>,
    title: CharSequence? = null,
    @GravityInt orientation: Int = Gravity.CENTER,
    autoOpenSoftInput: Boolean = false,
    isDarkTheme: Boolean = false,
    onSelectBlock: (position:Int, text:String) -> Unit,
) = XPopup.Builder(this).run {
    isDestroyOnDismiss(true)
    autoOpenSoftInput(autoOpenSoftInput)
    isDarkTheme(isDarkTheme)
    if (orientation == Gravity.CENTER) {
        asCenterList(title, data.toTypedArray(), onSelectBlock)
    } else {
        asBottomList(title, data.toTypedArray(), onSelectBlock)
    }.show()
}