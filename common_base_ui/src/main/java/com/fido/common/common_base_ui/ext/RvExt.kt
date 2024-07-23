package com.fido.common.common_base_ui.ext

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/**
@author FiDo
@description:
@date :2023/6/13 8:58
 */

/**
 * Rv是否滚动到底部
 */
fun RecyclerView.isSlideToBottom(): Boolean =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset() >= this.computeVerticalScrollRange())

/**
 * (1)的值表示是否能向上滚动，false表示已经滚动到底部
 */
val RecyclerView.canScrollUp
    get() = this.canScrollVertically(1)

/**
 * (-1)的值表示是否能向下滚动，false表示已经滚动到顶部
 */
val RecyclerView.canScrollDown
    get() = this.canScrollVertically(-1)

val RecyclerView.canScrollLeft get() = canScrollHorizontally(1)

val RecyclerView.canScrollRight get() = canScrollHorizontally(-1)

//获取Rv横向滑动百分比
val RecyclerView.scrollHorPercent:Float
    get() {
        val scrollX = computeHorizontalScrollOffset()
        val maxScrollX = computeHorizontalScrollRange() - width
        return scrollX.toFloat() / maxScrollX.toFloat()
    }

fun RecyclerView.setEmptyView(owner: LifecycleOwner,emptyView:View) =
    observeDataEmpty(owner) { emptyView.isVisible = it }

fun RecyclerView.observeDataEmpty(owner:LifecycleOwner,checkEmpty: (Boolean) -> Unit) =
    owner.lifecycle.addObserver(object :DefaultLifecycleObserver{

        private var rvObserver:RecyclerView.AdapterDataObserver?=null

        override fun onCreate(owner: LifecycleOwner) {
            if (rvObserver == null) {
                val adapter = checkNotNull(adapter){
                    "RecyclerView needs to set up the adapter before setting up an empty view."
                }
                rvObserver = AdapterDataEmptyObserver(adapter,checkEmpty)
                adapter.registerAdapterDataObserver(rvObserver as AdapterDataEmptyObserver)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            rvObserver?.let {
                adapter?.unregisterAdapterDataObserver(it)
                rvObserver = null
            }
        }

    })


class AdapterDataEmptyObserver(
    private val adapter:RecyclerView.Adapter<*>,
    private val checkEmpty:(Boolean)->Unit
):RecyclerView.AdapterDataObserver(){

    override fun onChanged() = checkEmpty(isDataEmpty)

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkEmpty(isDataEmpty)

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkEmpty(isDataEmpty)

    private val isDataEmpty get() = adapter.itemCount == 0

}