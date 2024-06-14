package com.fido.common.base.popup

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import com.fido.common.base.popup.interf.IPopupViewCreator
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.CenterPopupView

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:
 */
private class OpenCenterPopView<VB:ViewDataBinding>(
    context: Context,
    val viewBinding:((LayoutInflater)->VB?)?=null,
) :CenterPopupView(context) {

    private var binding:VB?=null

    override fun addInnerContent() {
        if (viewBinding != null) {
            binding = viewBinding.invoke(LayoutInflater.from(context))
            centerPopupContainer.addView(binding?.root)
        } else {
            super.addInnerContent()
        }
    }

}


class CenterPopCreator<VB:ViewDataBinding>(val context: Context):IPopupViewCreator<VB>{
    override fun create(viewBinding: (LayoutInflater) -> VB): BasePopupView {
        return OpenCenterPopView(context, viewBinding)
    }

}