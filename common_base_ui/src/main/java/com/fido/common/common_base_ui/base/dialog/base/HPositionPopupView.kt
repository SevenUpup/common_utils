package com.fido.common.common_base_ui.base.dialog.base

import android.content.Context
import com.lxj.xpopup.core.PositionPopupView

/**
@author FiDo
@description: 使用静态变量 BASE_POSITION_POP_LAYOUTRES直接给 ImplLayoutId赋值
@date :2023/9/14 15:59
 */
internal open class HPositionPopupView(context: Context) : PositionPopupView(context) {

    companion object{
        internal var BASE_POSITION_POP_LAYOUTRES:Int = -1
    }

    override fun getImplLayoutId(): Int = BASE_POSITION_POP_LAYOUTRES

}