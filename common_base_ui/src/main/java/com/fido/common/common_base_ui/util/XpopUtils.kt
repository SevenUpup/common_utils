package com.fido.common.common_base_ui.util

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

fun Context.showCustomLoading(title:CharSequence=""): BasePopupView = XPopup.Builder(this).asLoading(title).show()