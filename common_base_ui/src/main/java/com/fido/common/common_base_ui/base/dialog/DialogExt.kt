package com.fido.common.common_base_ui.base.dialog

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.fido.common.common_base_ui.base.dialog.base.BaseCommonAttachPop
import com.fido.common.common_base_ui.base.dialog.base.BaseCommonPop
import com.fido.common.common_base_ui.base.dialog.base.BaseVBCommonAttachPop
import com.fido.common.common_base_ui.base.dialog.base.BaseVBCommonPop
import com.fido.common.common_base_ui.util.creatXPopCustomView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.XPopupCallback


/**
 * @param layoutResId : your pop xml
 * @param init        : do some init findViewById等都在这
 */
inline fun <reified VB : ViewDataBinding> Context.createVBPop(
    layoutResId: Int,
    hasShadowBg:Boolean = false,
    isClickThrough:Boolean = false,
    isDestroyOnDismiss:Boolean = true, //对于只使用一次的弹窗，推荐设置这个
    dismissOnBackPressed:Boolean = true,
    dismissOnTouchOutside:Boolean = true,
    autoOpenSoftInput:Boolean = false,
    moveUpToKeyboard:Boolean = false,
    isDarkTheme:Boolean = false,
    hasStatusBarShadow:Boolean = false, //启用状态栏阴影
    isLightStatusBar:Boolean = false,   //置状态栏是否是亮色
    popHeight:Int = -1,
    popWidth:Int =-1,
    offsetX:Int = -1,
    offsetY:Int = -1,
    maxHeight:Int = -1,
    maxWidth:Int = -1,
    popAnima:PopupAnimation = PopupAnimation.TranslateFromBottom,
    popPosition:PopupPosition = PopupPosition.Bottom,
    atView:View?=null,
    popCallBack:XPopupCallback?=null,
    noinline init:(VB.()->Unit)?=null,
):BasePopupView {
    val basePop = if (atView == null) BaseVBCommonPop<VB>(this, layoutResId,init) else BaseVBCommonAttachPop(this, layoutResId,init)
    creatXPopCustomView(basePop).apply {
        isDarkTheme(isDarkTheme)
        hasShadowBg(hasShadowBg)
        isClickThrough(isClickThrough)
        isDestroyOnDismiss(isDestroyOnDismiss)
        if (popHeight>0) popupHeight(popHeight)
        if (popWidth>0) popupWidth(popWidth)
        if (maxHeight>0) maxHeight(maxHeight)
        if (maxWidth>0) maxWidth(maxWidth)
        if(atView!=null) atView(atView)
        popupAnimation(popAnima)
        popupPosition(popPosition)
        dismissOnBackPressed(dismissOnBackPressed)
        dismissOnTouchOutside(dismissOnTouchOutside)
        if(offsetX>0) offsetX(offsetX)
        if(offsetY>0) offsetY(offsetY)
        autoOpenSoftInput(autoOpenSoftInput)
        moveUpToKeyboard(moveUpToKeyboard)
        hasStatusBarShadow(hasStatusBarShadow)
        isLightStatusBar(isLightStatusBar)
        if(popCallBack!=null) setPopupCallback(popCallBack)
    }
    return basePop
}

/**
 * 建议使用createVBPop 可以做更多
 * @param layoutResId : your pop xml
 * @param init        : 做一些初始化操作 findViewById等都在这
 */
fun Context.createPop(
    layoutResId: Int,
    hasShadowBg:Boolean = false,
    isClickThrough:Boolean = false,
    isDestroyOnDismiss:Boolean = true, //对于只使用一次的弹窗，推荐设置这个
    dismissOnBackPressed:Boolean = true,
    dismissOnTouchOutside:Boolean = true,
    autoOpenSoftInput:Boolean = false,
    moveUpToKeyboard:Boolean = false,
    isDarkTheme:Boolean = false,
    hasStatusBarShadow:Boolean = false, //启用状态栏阴影
    isLightStatusBar:Boolean = false,   //置状态栏是否是亮色
    popHeight:Int = -1,
    popWidth:Int =-1,
    offsetX:Int = -1,
    offsetY:Int = -1,
    popAnima:PopupAnimation = PopupAnimation.NoAnimation,
    popPosition:PopupPosition = PopupPosition.Bottom,
    atView:View?=null,
    popCallBack:XPopupCallback?=null,
    init:(BasePopupView.()->Unit)?=null,
):BasePopupView {
    val basePop = if (atView == null) BaseCommonPop(this, layoutResId,init) else BaseCommonAttachPop(this, layoutResId,init)
    creatXPopCustomView(basePop).apply {
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
        moveUpToKeyboard(moveUpToKeyboard)
        hasStatusBarShadow(hasStatusBarShadow)
        isLightStatusBar(isLightStatusBar)
        if(popCallBack!=null) setPopupCallback(popCallBack)
    }
    return basePop
}