package com.fido.common.common_base_ui.base.dialog

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.fido.common.common_base_ui.util.creatCustomPop
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.XPopupCallback


inline fun <reified VB : ViewDataBinding> Context.showCustomPop(
    layoutResId: Int,
    noinline init:((VB)->Unit)?=null,
    hasShadowBg:Boolean = false,
    isClickThrough:Boolean = false,
    isDestroyOnDismiss:Boolean = true, //对于只使用一次的弹窗，推荐设置这个
    dismissOnBackPressed:Boolean = true,
    dismissOnTouchOutside:Boolean = true,
    autoOpenSoftInput:Boolean = true,
    isDarkTheme:Boolean = false,
    hasStatusBarShadow:Boolean = false, //启用状态栏阴影
    isLightStatusBar:Boolean = false,   //置状态栏是否是亮色
    popHeight:Int = -1,
    popWidth:Int =-1,
    offsetX:Int = -1,
    offsetY:Int = -1,
    popAnima:PopupAnimation = PopupAnimation.TranslateFromBottom,
    popPosition:PopupPosition = PopupPosition.Bottom,
    atView:View?=null,
    popCallBack:XPopupCallback?=null
):BasePopupView {
    val basePop = BaseCommonPop<VB>(this, layoutResId,init)
    creatCustomPop(basePop).apply {
        isDarkTheme(isDarkTheme)
        hasShadowBg(hasShadowBg)
        isClickThrough(isClickThrough)
        isDestroyOnDismiss(isDestroyOnDismiss)
        if (popHeight>0) popupHeight(popHeight)
        if (popWidth>0) popupWidth(popWidth)
        if(atView!=null) atView(atView)
        popupAnimation(popAnima)
        popupPosition(popPosition)
        dismissOnBackPressed(dismissOnBackPressed)
        dismissOnTouchOutside(dismissOnTouchOutside)
        if(offsetX>0) offsetX(offsetX)
        if(offsetY>0) offsetY(offsetY)
        autoOpenSoftInput(autoOpenSoftInput)
        hasStatusBarShadow(hasStatusBarShadow)
        isLightStatusBar(isLightStatusBar)
        if(popCallBack!=null) setPopupCallback(popCallBack)
    }
    basePop.show()
    return basePop
}

class BaseCommonPop<VB : ViewDataBinding>(
    context: Context,
    private val layoutResId: Int,
    private val init: ((VB) -> Unit)? = null,
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