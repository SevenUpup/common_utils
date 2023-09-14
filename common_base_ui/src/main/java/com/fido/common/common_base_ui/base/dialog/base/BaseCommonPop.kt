package com.fido.common.common_base_ui.base.dialog.base

import android.content.Context
import android.view.View
import com.lxj.xpopup.core.AttachPopupView
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.BottomPopupView


@PublishedApi
internal class BaseCommonPopStrategy(
    val context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
    private val offSet: Int? = null,
    private val atView: View? = null
) {
    fun getPop(): BasePopupView {
        if (atView != null) {
            return BaseCommonAttachPop(context, layoutResId, init)
        }
        if (offSet != null) {
            HPositionPopupView.BASE_POSITION_POP_LAYOUTRES = layoutResId
            return BaseCommonPositionPop(context, layoutResId, init)
        }
        return BaseCommonBottomPop(context, layoutResId, init)
    }
}

internal class BaseCommonBottomPop(
    context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
) : BottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()

        init?.invoke(this)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}


internal class BaseCommonPositionPop(
    context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
) : HPositionPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        init?.invoke(this)
    }

   /* override fun getImplLayoutId(): Int {
        return layoutResId
    }*/

}

internal class BaseCommonAttachPop(
    context: Context,
    private val layoutResId: Int,
    private val init: (BasePopupView.() -> Unit)? = null,
) : AttachPopupView(context) {

    override fun onCreate() {
        super.onCreate()

        init?.invoke(this)
    }

    override fun getImplLayoutId(): Int {
        return layoutResId
    }
}
