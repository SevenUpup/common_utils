package com.fido.common.base.popup

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.fido.common.base.popup.enu.PopupAnimationType
import com.fido.common.base.popup.enu.PopupType
import com.fido.common.base.popup.interf.IPopupAnimationStrategy
import com.fido.common.base.popup.interf.IPopupViewCreator

/**
 * @author: FiDo
 * @date: 2024/6/11
 * @des:
 */
object PopupViewCreatorFactory {

//    fun <VB:ViewDataBinding> getCreator(
//            popType:PopupType,
//            context: Context,
//            width: Int = 0,
//            height: Int = 0,
//            maxWidth: Int = 0,
//            maxHeight: Int = 0,
//            onCreateListener:((VB, IPopupController) -> Unit)? = null,
//            onDismissListener:(()->Unit)?=null
//    ) :IPopupViewCreator<VB>{
//        return when(popType){
//            PopupType.ATTACH -> AttachPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//            PopupType.BOTTOM -> CenterPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//            PopupType.CENTER -> CenterPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//            PopupType.FULL_SCREEN -> CenterPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//            PopupType.KEYBOARD -> CenterPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//            PopupType.PART_SCREEN -> CenterPopViewCreator(context,width,height,maxWidth,maxHeight,onCreateListener,onDismissListener)
//
//        }
//    }


   fun <VB:ViewDataBinding>getCreator(
       popupType: PopupType,
       context: Context,
   ):IPopupViewCreator<VB>{
       return when(popupType){
           PopupType.CENTER -> CenterPopCreator<VB>(context)
       }
   }


}

object PopupAnimationStrategyFactory {

    fun getStrategy(animationType: PopupAnimationType?): IPopupAnimationStrategy? {
        return when (animationType) {
            PopupAnimationType.ScaleAlphaFromCenter -> ScaleAlphaFromCenterStrategy()
            PopupAnimationType.ScaleAlphaFromLeftTop -> ScaleAlphaFromLeftTopStrategy()
            PopupAnimationType.ScaleAlphaFromRightTop -> ScaleAlphaFromRightTopStrategy()
            PopupAnimationType.ScaleAlphaFromLeftBottom -> ScaleAlphaFromLeftBottomStrategy()
            PopupAnimationType.ScaleAlphaFromRightBottom -> ScaleAlphaFromRightBottomStrategy()
            PopupAnimationType.TranslateAlphaFromLeft -> TranslateAlphaFromLeftStrategy()
            PopupAnimationType.TranslateAlphaFromRight -> TranslateAlphaFromRightStrategy()
            PopupAnimationType.TranslateAlphaFromTop -> TranslateAlphaFromTopStrategy()
            PopupAnimationType.TranslateAlphaFromBottom -> TranslateAlphaFromBottomStrategy()
            PopupAnimationType.TranslateFromLeft -> TranslateFromLeftStrategy()
            PopupAnimationType.TranslateFromRight -> TranslateFromRightStrategy()
            PopupAnimationType.TranslateFromTop -> TranslateFromTopStrategy()
            PopupAnimationType.TranslateFromBottom -> TranslateFromBottomStrategy()
            PopupAnimationType.NoAnimation -> NoAnimationStrategy()
            else -> null
        }
    }


}