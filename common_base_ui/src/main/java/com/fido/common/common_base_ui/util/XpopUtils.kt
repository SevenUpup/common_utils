package com.fido.common.common_base_ui.util

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView

fun Context.showLoading(title:CharSequence=""): BasePopupView = XPopup.Builder(this).asLoading(title).show()

fun Context.creatCustomPop(popupView: BasePopupView) = XPopup.Builder(this).apply {
    asCustom(popupView)
}
