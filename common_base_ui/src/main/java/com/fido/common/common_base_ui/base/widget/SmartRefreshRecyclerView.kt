package com.fido.common.common_base_ui.base.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.DataBindingHolder
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.bindMuiltData
import com.fido.common.common_base_ui.ext.bindMuiltVBData
import com.fido.common.common_base_ui.ext.vertical
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class SmartRefreshRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val mSmartRefreshLayout:SmartRefreshLayout
        get() = (this[0] as SmartRefreshLayout)

    val mRecyclerView:RecyclerView
        get() = mSmartRefreshLayout.children.find { it is RecyclerView } as RecyclerView

    init {
        addView(
            SmartRefreshLayout(context).apply {
                addView(
                    RecyclerView(context),
                    LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                    )
                )
            },
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

    }

}

fun <T> SmartRefreshRecyclerView.bind(
    data:List<T>,
    layoutRes:Int,
    holderBindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null,
    onRefreshListener: OnRefreshListener?=null,
    onLoadMoreListener: OnLoadMoreListener?=null,
):SmartRefreshRecyclerView{
    mRecyclerView.vertical().bindData(data,layoutRes,holderBindFun)
    onRefreshListener?.let { mSmartRefreshLayout.setOnRefreshListener(it) }
    onLoadMoreListener?.let { mSmartRefreshLayout.setOnLoadMoreListener(it) }
    return this
}

fun <T:BaseMuiltEntity> SmartRefreshRecyclerView.bindMuiltVB(
    data:List<T>,
    holderBindFun: ((holder: DataBindingHolder<ViewDataBinding>, position: Int, item: T?) -> Unit)? = null,
    onRefreshListener: OnRefreshListener?=null,
    onLoadMoreListener: OnLoadMoreListener?=null,
):SmartRefreshRecyclerView{
    mRecyclerView.vertical().bindMuiltVBData(data,holderBindFun)
    onRefreshListener?.let { mSmartRefreshLayout.setOnRefreshListener(it) }
    onLoadMoreListener?.let { mSmartRefreshLayout.setOnLoadMoreListener(it) }
    return this
}

fun <T:BaseMuiltEntity> SmartRefreshRecyclerView.bindMuilt(
    data:List<T>,
    holderBindFun: ((holder: QuickViewHolder, position: Int, item: T?) -> Unit)? = null,
    onRefreshListener: OnRefreshListener?=null,
    onLoadMoreListener: OnLoadMoreListener?=null,
):SmartRefreshRecyclerView{
    mRecyclerView.vertical().bindMuiltData(data,holderBindFun)
    onRefreshListener?.let { mSmartRefreshLayout.setOnRefreshListener(it) }
    onLoadMoreListener?.let { mSmartRefreshLayout.setOnLoadMoreListener(it) }
    return this
}


