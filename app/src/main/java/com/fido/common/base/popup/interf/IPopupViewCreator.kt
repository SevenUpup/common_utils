package com.fido.common.base.popup.interf

import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.BasePopupView

/**
 * @author: FiDo
 * @date: 2024/6/12
 * @des:
 */
interface IPopupViewCreator<VB:ViewDataBinding> {

    fun create(viewBinding:(LayoutInflater)->VB):BasePopupView

}