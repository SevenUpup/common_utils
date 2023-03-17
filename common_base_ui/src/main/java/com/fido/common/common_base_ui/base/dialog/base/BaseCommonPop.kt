package com.fido.common.common_base_ui.base.dialog.base

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView

class BaseCommonPop(
    context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()

        init?.invoke(this)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}

class BaseCommonAttachPop(
    context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
) : AttachPopupView(context) {

    override fun onCreate() {
        super.onCreate()

        init?.invoke(this)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}
