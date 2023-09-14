package com.fido.common.common_base_ui.base.dialog.base

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView

@PublishedApi
internal class BaseVBCommonPopStrategy<VB:ViewDataBinding>(
    private val context: Context,
    private val layoutResId: Int,
    private val init: (VB.() -> Unit)?=null,
    private val atView:View?=null,
    private val offSet:Int?=null,
){
    fun getBaseVBPop():BasePopupView{
        if (atView != null) {
            return BaseVBCommonAttachPop<VB>(context, layoutResId, init)
        }
        if (offSet != null) {
            HPositionPopupView.BASE_POSITION_POP_LAYOUTRES = layoutResId
            return BaseVBPositionPop<VB>(context,init)
        }
        return BaseVBBottomPop<VB>(context, layoutResId, init)
    }
}


internal class BaseVBBottomPop<VB : ViewDataBinding>(
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

internal class BaseVBPositionPop<VB:ViewDataBinding>(
    contex: Context,
    private val init: (VB.() -> Unit)?
):HPositionPopupView(contex){

    private lateinit var binding: VB
    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)!!

        init?.invoke(binding)
    }
}

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