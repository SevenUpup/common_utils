package com.fido.common.common_base_util.util.hook

import android.content.Context
import android.view.View

class ProxyClickListener(
    var context: Context,
    private var clickListener: View.OnClickListener,
    private val doBefore: ((View) -> Unit)?
) : View.OnClickListener {

    override fun onClick(v: View) {
        doBefore?.invoke(v)
        clickListener.onClick(v)
    }
}