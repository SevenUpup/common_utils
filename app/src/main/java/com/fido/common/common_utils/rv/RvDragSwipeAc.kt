package com.fido.common.common_utils.rv

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.ext.addItemChildClick
import com.fido.common.common_base_ui.ext.bindAdapter
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.channel.receiveEvent
import com.fido.common.common_base_util.channel.receiveEventLive
import com.fido.common.common_base_util.channel.sendEvent
import com.fido.common.common_base_util.ext.logGson
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.toJson
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcRvDragSwipeBinding
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

class RvDragSwipeAc : BaseVBActivity<AcRvDragSwipeBinding>() {

    override fun initEvent() {

        runBlocking {
            delay(200)

            receiveEventLive<String>{
                toast(it)
            }

            receiveEvent<String>().collect{
                loge("flow $it","Channel")

            }
            
            sendEvent("123")
        }

    }

    override fun initData() {
        immersionBar {
            statusBarDarkFont(true)
        }
    }

    override fun initView() {
        binding.mRv.vertical()
            .bindData(
                mutableListOf(
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                ),
                R.layout.item_rv_text
            )
            { holder, position, item ->
                holder.setText(R.id.mTitle, "item pos=${position}")
            }
            .addItemChildClick<String>(R.id.bt_up) { adapter, view, position ->
                toast("click up")
                if (position > 0) {
                    adapter.swap(position, if (position > 0) position - 1 else position)
                }
                logGson(binding.mRv.bindAdapter.items,this@RvDragSwipeAc::class.java.simpleName)
            }
            .addItemChildClick<String>(R.id.bt_down) { adapter, view, position ->
                toast("click down")
                adapter.swap(position, if (position >= adapter.itemCount-1) position else position + 1)
                logGson(binding.mRv.bindAdapter.items,this@RvDragSwipeAc::class.java.simpleName)
            }

        ItemTouchHelper(MyItemTouchHelperCallBack(binding.mRv.bindAdapter).apply {
            setCanDrag(true)
        }).attachToRecyclerView(
            binding.mRv
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.ac_rv_drag_swipe
    }

}

class MyItemTouchHelperCallBack(val adapter: BaseQuickAdapter<*, *>) : ItemTouchHelper.Callback() {
    private var isCanDrag = false //默认关闭拖拽功能

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val movePos = viewHolder.bindingAdapterPosition
        val targetPos = target.bindingAdapterPosition
//        adapter.notifyItemMoved(movePos, targetPos)
        recyclerView.bindAdapter.swap(movePos,targetPos)
        loge("onMove ${recyclerView.bindAdapter.items.toJson()} movePos=${movePos} targetPos=${targetPos}","RvDragSwipeAc")
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        loge("onSwiped direction=${direction}","RvDragSwipeAc")
    }

    override fun isLongPressDragEnabled(): Boolean {
        return isCanDrag
    }

    fun setCanDrag(isCandrag: Boolean) {
        this.isCanDrag = isCandrag
    }
}