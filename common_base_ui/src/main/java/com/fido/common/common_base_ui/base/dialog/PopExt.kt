package com.fido.common.common_base_ui.base.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.fido.common.common_base_ui.base.dialog.base.*
import com.fido.common.common_base_ui.base.dialog.base.BaseCommonPopStrategy
import com.fido.common.common_base_ui.util.creatXPopCustomView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.interfaces.XPopupCallback
import kotlin.math.max


/**
 * @param layoutResId : your pop xml
 * @param popGravity  : 弹窗位置：默认居中弹出
 * @param popPosition : 需要绑定参数:[atView] 弹窗位置相对于(atView)的Position
 * @param init        : do some init 可以通过作用域直接拿到view
 */
inline fun <reified VB : ViewDataBinding> Context.createVBPop(
    layoutResId: Int,
    popGravity:Int = Gravity.CENTER,
    hasShadowBg: Boolean = false,
    isClickThrough: Boolean = false,
    isDestroyOnDismiss: Boolean = true, //对于只使用一次的弹窗，推荐设置这个
    dismissOnBackPressed: Boolean = true,
    dismissOnTouchOutside: Boolean = true,
    autoOpenSoftInput: Boolean = false,
    moveUpToKeyboard: Boolean = false,
    isDarkTheme: Boolean = false,
    hasStatusBarShadow: Boolean = false, //启用状态栏阴影
    isLightStatusBar: Boolean = false,   //设置状态栏是否是亮色
    popHeight: Int = -1,
    popWidth: Int = -1,
    offsetX: Int? = null,
    offsetY: Int? = null,
    maxHeight: Int = -1,
    maxWidth: Int = -1,
    popAnima: PopupAnimation ?= null,
    popPosition: PopupPosition? = null,
    atView: View? = null,
    isCenterHorizontal: Boolean = false,
    popCallBack: XPopupCallback? = null,
    noinline init: (VB.() -> Unit)? = null,
): BasePopupView {
    val basePop = BaseVBCommonPopStrategy<VB>(
        this,
        layoutResId,
        init,
        atView,
        if (offsetX != null || offsetY != null) max(offsetX ?: 0, offsetY ?: 0) else null,
        popGravity

    ).getBaseVBPop()
    creatXPopCustomView(basePop).apply {
        isDarkTheme(isDarkTheme)
        hasShadowBg(hasShadowBg)
        isClickThrough(isClickThrough)
        isDestroyOnDismiss(isDestroyOnDismiss)
        if (popHeight > 0) popupHeight(popHeight)
        if (popWidth > 0) popupWidth(popWidth)
        if (maxHeight > 0) maxHeight(maxHeight)
        if (maxWidth > 0) maxWidth(maxWidth)
        if (atView != null) atView(atView)
        popupAnimation(popAnima)
        if (popPosition != null) popupPosition(popPosition)
        dismissOnBackPressed(dismissOnBackPressed)
        dismissOnTouchOutside(dismissOnTouchOutside)
        if (offsetX != null) offsetX(offsetX)
        if (offsetY != null) offsetY(offsetY)
        autoOpenSoftInput(autoOpenSoftInput)
        moveUpToKeyboard(moveUpToKeyboard)
        hasStatusBarShadow(hasStatusBarShadow)
        isLightStatusBar(isLightStatusBar)
        isCenterHorizontal(isCenterHorizontal)
        if (popCallBack != null) setPopupCallback(popCallBack)
    }
    return basePop
}

/**
 * 建议使用createVBPop 可以做更多
 * @param layoutResId : your pop xml
 * @param popPosition : 弹窗位置相对于atView（需要 atView）
 * @param init        : 做一些初始化操作 findViewById等都在这
 */
fun Context.createPop(
    layoutResId: Int,
    popGravity:Int = Gravity.CENTER,
    hasShadowBg: Boolean = false,
    isClickThrough: Boolean = false,
    isDestroyOnDismiss: Boolean = true, //对于只使用一次的弹窗，推荐设置这个
    dismissOnBackPressed: Boolean = true,
    dismissOnTouchOutside: Boolean = true,
    autoOpenSoftInput: Boolean = false,
    moveUpToKeyboard: Boolean = false,
    isDarkTheme: Boolean = false,
    hasStatusBarShadow: Boolean = false, //启用状态栏阴影
    isLightStatusBar: Boolean = false,   //状态栏是否是亮色
    popHeight: Int = -1,
    popWidth: Int = -1,
    offsetX: Int? = null,
    offsetY: Int? = null,
    popAnima: PopupAnimation = PopupAnimation.NoAnimation,
    popPosition: PopupPosition? = null,
    atView: View? = null,
    isCenterHorizontal: Boolean = false,
    popCallBack: XPopupCallback? = null,
    init: (BasePopupView.() -> Unit)? = null,
): BasePopupView {
    val basePop = BaseCommonPopStrategy(
        this,
        layoutResId,
        init,
        if (offsetX != null || offsetY != null) max(offsetX ?: 0, offsetY ?: 0) else null,
        atView,
        popGravity,
    ).getPop()
    creatXPopCustomView(basePop).apply {
        isDarkTheme(isDarkTheme)
        hasShadowBg(hasShadowBg)
        isClickThrough(isClickThrough)
        isDestroyOnDismiss(isDestroyOnDismiss)
        if (popHeight > 0) popupHeight(popHeight)
        if (popWidth > 0) popupWidth(popWidth)
        if (atView != null) atView(atView)
        popupAnimation(popAnima)
        if (popPosition != null) popupPosition(popPosition)
        dismissOnBackPressed(dismissOnBackPressed)
        dismissOnTouchOutside(dismissOnTouchOutside)
        if (offsetX != null) offsetX(offsetX)
        if (offsetY != null) offsetY(offsetY)
        autoOpenSoftInput(autoOpenSoftInput)
        moveUpToKeyboard(moveUpToKeyboard)
        hasStatusBarShadow(hasStatusBarShadow)
        isLightStatusBar(isLightStatusBar)
        isCenterHorizontal(isCenterHorizontal)
        if (popCallBack != null) setPopupCallback(popCallBack)
    }
    return basePop
}
