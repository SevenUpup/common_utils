package com.fido.common.common_base_ui.base.dialog.base

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView

@PublishedApi
internal class BaseVBCommonPop<VB : ViewDataBinding>(
    context: Context,
    private val layoutResId: Int,
    private val init: (VB.() -> Unit)? = null,
) : BottomPopupView(context) {

    private lateinit var binding: VB

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)!!

        init?.invoke(binding)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}

@PublishedApi
internal class BaseVBCommonAttachPop<VB : ViewDataBinding>(
    context: Context,
    private val layoutResId: Int,
    private val init: (VB.() -> Unit)? = null,
) : AttachPopupView(context) {

    private lateinit var binding: VB

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)!!

        init?.invoke(binding)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}