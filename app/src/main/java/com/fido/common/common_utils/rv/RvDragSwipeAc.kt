package com.fido.common.common_utils.rv

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.drake.debugkit.DevTool
import com.drake.debugkit.dev
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.ext.addItemChildClick
import com.fido.common.common_base_ui.ext.bindAdapter
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.channel.*
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.toJson
import com.fido.common.common_base_util.util.AssetUtils
import com.fido.common.common_utils.P
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcRvDragSwipeBinding
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.*
import kotlin.concurrent.thread


class RvDragSwipeAc : BaseVBActivity<AcRvDragSwipeBinding>() {

    private val C_TAG = "Channel"
    override fun initEvent() {
        receiveStickTag("tag_FiDo"){
            loge("receiveStickTag-->${this}")
        }

        receiveStickEvent<P> {
            loge("2${it.toJson()} ${Thread.currentThread().name}",C_TAG)
        }

        lifecycleScope.launch {
            receiveTag("tag_2").collect{
                loge(it,C_TAG)
//                binding.btRecevie.text = withContext(Dispatchers.IO){
//                    "${binding.btRecevie.text} \n$it"
//                }
                binding.btRecevie.text = "${binding.btRecevie.text} \n$it"
            }
        }

        binding.btRecevie.click {

            receiveEventLive<String> {
                loge(it,C_TAG)
                receiveTag("tag_1").collect{
                    loge(it,C_TAG)
                    binding.btRecevie.text = withContext(Dispatchers.IO){
                        "$it"
                    }
                }
            }

            sendEvent("123")

            receiveTag("tag_2") {
                toast("receive tag2 value=$it")
//                binding.btRecevie.text = withContext(Dispatchers.IO){
//                    "receive tag value=$it"
//                }
            }

            sendTag("tag_1")
            sendTag("tag_2")


            thread {
                loge("thread= ${Thread.currentThread().name}",C_TAG)
                receiveStickEvent<P> {
                    loge("1${it.toJson()} ${Thread.currentThread().name}",C_TAG)
                    binding.mIv.setImageBitmap(AssetUtils.loadBitmapAsset(this@RvDragSwipeAc,"dog.jpg"))
                }
            }

            val devTool = DevTool(this)
            lifecycle.addObserver(devTool)
            dev(devTool) {
                function("round_image") {
                    Glide.with(this@RvDragSwipeAc)
                        .applyDefaultRequestOptions(RequestOptions.centerCropTransform())
                        .load("https://gd-hbimg.huaban.com/6cf0cd75148fa2ea73991c20a1046d2761803c7c4ab87-Om3w94_fw240webp")
                        .into(binding.mIv)
                }
                function("send Tag2") {
                    sendTag("tag_2")
                }
                function("关闭") {
                    // do something ...
                    close() // 关闭调试窗口
                }
                function("send Tag1") {
                    sendTag("tag_1")
                }
            }

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
                logGson(binding.mRv.bindAdapter.items, this@RvDragSwipeAc::class.java.simpleName)
            }
            .addItemChildClick<String>(R.id.bt_down) { adapter, view, position ->
                toast("click down")
                adapter.swap(
                    position,
                    if (position >= adapter.itemCount - 1) position else position + 1
                )
                logGson(binding.mRv.bindAdapter.items, this@RvDragSwipeAc::class.java.simpleName)
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
        recyclerView.bindAdapter.swap(movePos, targetPos)
        loge(
            "onMove ${recyclerView.bindAdapter.items.toJson()} movePos=${movePos} targetPos=${targetPos}",
            "RvDragSwipeAc"
        )
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        loge("onSwiped direction=${direction}", "RvDragSwipeAc")
    }

    override fun isLongPressDragEnabled(): Boolean {
        return isCanDrag
    }

    fun setCanDrag(isCandrag: Boolean) {
        this.isCanDrag = isCandrag
    }
}