package com.fido.common.base.popup

import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.fido.common.base.popup.enu.PopupType
import com.lxj.xpopup.XPopup

/**
 * @author: FiDo
 * @date: 2024/6/11
 * @des:
 */

fun <VB:ViewDataBinding>FragmentActivity.showPopup(
    popupType: PopupType,
    viewBinding:(LayoutInflater)->VB,
    width: Int = 0,
    height: Int = 0,
    maxWidth: Int = 0,
    maxHeight: Int = 0,
//    onCreate: (VB?.(IPopViewController) -> Unit)? = null,
//    onDismiss: (() -> Unit)? = null
){
    val creator = PopupViewCreatorFactory.getCreator<VB>(popupType,this)
    val pop = creator.create(viewBinding)
    val builder = XPopup.Builder(this)
        .asCustom(pop)
    builder.show()
}

//fun <VB:ViewDataBinding>FragmentActivity.showPopup(
//    popupType: PopupType,  //必须指定类型
//    viewBinding: (LayoutInflater) -> VB,
//    popupAnimationType: PopupAnimationType? = null,  //动画类型
//    targetView: View? = null, //如果是Attach则一定要指定在哪个View的基础上弹窗
//    width: Int = 0,
//    height: Int = 0,   //宽高一般不需要额外设置，自动适配布局的宽高
//    maxWidth: Int = 0,  //如果想要指定宽高，可以自定义宽高
//    maxHeight: Int = 0,
//    offsetX: Int = 0,   //弹框在 X,Y 方向的偏移值
//    offsetY: Int = 0,
//    isCenterHorizontal: Boolean = false,  //布局是否水平居中
//    onCreateListener: ((VB, IPopupController) -> Unit)? = null,   //创建的回调
//    onDismissListener: (() -> Unit)? = null,     //消失的回调
//){
//
//    val creator = PopupViewCreatorFactory.getCreator(popupType,this,width, height, maxWidth, maxHeight, onCreateListener, onDismissListener)
//    val popView = creator.creator(viewBinding)
//
//    val builder = XPopup.Builder(this)
//        .hasShadowBg(popupType == PopupType.CENTER || popupType == PopupType.BOTTOM)
//        .isDestroyOnDismiss(true)
//        .offsetX(offsetX)
//        .offsetY(offsetY)
//        .isCenterHorizontal(isCenterHorizontal)
//
//    if (targetView != null) {
//        builder.atView(targetView)
//    }
//    if (popupType == PopupType.KEYBOARD) {
//        builder.moveUpToKeyboard(true)
//        builder.autoOpenSoftInput(true)
//    }
//    //动画
//    if (transformAnimationType(popupAnimationType) != null) {
//        builder.popupAnimation(transformAnimationType(popupAnimationType))
//    }
//    builder.asCustom(popView)
//        .show()
//}
//
////获取动画类型
//internal fun transformAnimationType(animationType: PopupAnimationType?): PopupAnimation? {
//    val strategy = PopupAnimationStrategyFactory.getStrategy(animationType)
//    return strategy?.getAnimation()
//}
