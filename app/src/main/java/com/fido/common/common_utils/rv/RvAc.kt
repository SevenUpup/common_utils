package com.fido.common.common_utils.rv

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.base.widget.bind
import com.fido.common.common_base_ui.ext.*
import com.fido.common.common_base_ui.ext.image_select.clearImageSelectCache
import com.fido.common.common_base_ui.util.ui.RVScrollUtils
import com.fido.common.common_base_ui.util.ui.SmoothLinearLayoutManager
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_base_util.util.timer.Interval
import com.fido.common.R
import com.fido.common.databinding.AcRvBinding
import com.fido.common.databinding.ItemRvImgBinding
import com.fido.common.databinding.ItemRvTextBinding
import com.fido.common.databinding.LayoutHeaderViewBinding
import java.util.concurrent.TimeUnit

class RvAc:AppCompatActivity() {

    private val mBinding:AcRvBinding by binding()
    private lateinit var mRv: RecyclerView
    private lateinit var mRv2: RecyclerView

    private lateinit var layoutManager:SmoothLinearLayoutManager
    private lateinit var layoutManager2:SmoothLinearLayoutManager
    companion object{
        fun test(context:Context){
            Log.d("FiDo", "test: ---")
            Toast.makeText(context, "调用了test", Toast.LENGTH_SHORT).show()
        }
    }

    fun goEmpty(v:View){
        startActivity<RvEmptyViewAc>()
    }

    fun goScroll(v: View){
        startActivity<RvScrollWithWindownAc>()
    }

    fun goDrag(v: View){
        startActivity<RvDragSwipeAc>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mBinding = DataBindingUtil.setContentView(this, R.layout.ac_rv)

        initView()

        Interval(10,2,TimeUnit.SECONDS,1,5).life(this)
            .subscribe {
                Log.d("FiDo", "s==>${it}")
            }
            .finish {
                Log.d("FiDo", "finish==>$it")
            }.start()

        initEvent()

    }

    private fun initEvent() {
        mBinding.mSRv.bind(
            mutableListOf("1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿"),
            R.layout.item_rv_text_img,
            holderBindFun = {
                holder, position, item ->

            },
            onRefreshListener = {
                mBinding.mSRv.mRecyclerView.submitList(mutableListOf("newData-0","newData-1","newData-2","newData-3"))
            },
            onLoadMoreListener = {
                mBinding.mSRv.mRecyclerView.addAll(mutableListOf("add-0","add-1","add-2","add-3"))
            }
        )
        mBinding.mSRv.mRecyclerView.vertical()
            .bindData(
                mutableListOf("1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿"),
                R.layout.item_rv_text_img
            ) { holder, position, item ->
                holder.setText(R.id.mTv, "$position-->${item}")
                holder.setImageResource(R.id.mIcon, R.mipmap.flower)
            }
        mBinding.mSRv.mSmartRefreshLayout.apply {
            setEnableAutoLoadMore(true)
            setOnRefreshListener {
                mBinding.mSRv.mRecyclerView.submitList(mutableListOf("newData-0","newData-1","newData-2","newData-3"))
            }
            setOnLoadMoreListener {
                mBinding.mSRv.mRecyclerView.addAll(mutableListOf("add-0","add-1","add-2","add-3"))
            }
        }

        mBinding.btFirst.setOnClickListener {
            mBinding.btFirst.text= if(mBinding.mSRv.isVisible) "show muilt Rv" else "show smartRefreshRv"
            mBinding.mSRv.isVisible = !mBinding.mSRv.isVisible

            mBinding.mRv.isVisible = !mBinding.mSRv.isVisible
            mBinding.mRv2.isVisible = !mBinding.mSRv.isVisible

        }

        mBinding.btScroll.click {
            RVScrollUtils.rvSmoothScrollToPosition(mRv, layoutManager, 5)
            RVScrollUtils.rvSmoothScrollToPosition(mRv2, layoutManager2, 5)

            RVScrollUtils.rvSmoothScrollToPosition(mBinding.mHorizRv,mBinding.mHorizRv.layoutManager as LinearLayoutManager,5)
        }
    }

    private fun initView() {
        mBinding.mHorizRv.horizontal()
            .bindData(
                mutableListOf("horizontal 1", "horizontal 2", "horizontal 3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿","1", "2", "3", "呵呵我嘿嘿"),
                R.layout.item_rv_text_img
            ) { holder, position, item ->
                holder.setText(R.id.mTv, "$position-->${item}")
            }

        mRv = mBinding.mRv
        mRv2 = mBinding.mRv2

        val mTestData = mutableListOf<MEntity>()
        for (i in 0 until 10) {
            val d = MEntity().apply {
                itemType = if (i % 2 == 0) 0 else 1
                itemLayoutRes = if(itemType == 0) R.layout.item_rv_text else R.layout.item_rv_img
                content = (i+5).toString()
            }
            mTestData.add(d)
        }


        mRv.vertical()
            .bindMuiltVBData(mutableListOf<MEntity>()){ holder, position, item ->
                Log.d("FiDo", "initView binding: ${holder.binding}")
                when(holder.binding){
                    is ItemRvTextBinding ->{
                        (holder.binding as ItemRvTextBinding).data = "xml data bind${item?.content}"
                    }
                    is ItemRvImgBinding -> {
                        (holder.binding as? ItemRvImgBinding)?.mIv?.setImageResource(R.mipmap.flower)
                    }
                    is LayoutHeaderViewBinding->{
                        (holder.binding as? LayoutHeaderViewBinding)?.tvHeaderTitle?.text = "LayoutHeaderViewBinding"
                    }
                }
            }
            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
                holder.setText(R.id.tv_header_title, "rv header1")
                holder.itemView.setOnClickListener {
                    mRv.addFooter(mutableListOf("222"), R.layout.layout_header_view)
                }
            }
            .divider(size = 5)
            .itemClick<MEntity> { adapter, view, position ->
                Toast.makeText(this, "pos:$position", Toast.LENGTH_SHORT).show()
            }
            .addItemChildClick<MEntity>(R.id.mIv) { adapter, view, position ->
                Toast.makeText(this, "click img:$position 变为横向", Toast.LENGTH_SHORT).show()
                mRv.horizontal()
                    .divider(size = 0, isReplace = true)
            }
            .addItemChildClick<MEntity>(R.id.tv_header_title) { adapter, view, position ->
                Toast.makeText(this, "click title:$position ", Toast.LENGTH_SHORT).show()
            }
            .addItemChildClick<MEntity>(R.id.bt_up){adapter, view, position ->
                toast("click up")

            }
            .addItemChildClick<MEntity>(R.id.bt_down){adapter, view, position ->
                toast("click down")
            }
            .submitList(
                mutableListOf<MEntity>().apply {
                    add(MEntity().apply {
                        itemType = 0
                        itemLayoutRes = R.layout.item_rv_text
                    })
                    add(MEntity().apply {
                        itemType = 1
                        itemLayoutRes = R.layout.item_rv_img
                    })
                    add(MEntity().apply {
                        itemType = 1
                        itemLayoutRes = R.layout.item_rv_img
                    })
                    add(MEntity().apply {
                        itemType = 1
                        itemLayoutRes = R.layout.item_rv_img
                    })
                    add(MEntity().apply {
                        itemType = 1
                        itemLayoutRes = R.layout.item_rv_img
                    })
                    add(MEntity().apply {
                        itemType = 1
                        itemLayoutRes = R.layout.item_rv_img
                    })
                }
            )
            .addData(MEntity().apply {
                itemType = 2
                itemLayoutRes = R.layout.item_rv_text_img
            })
            .addAll(mutableListOf<MEntity>().apply{
                add(MEntity().apply {
                    itemType = 3
                    itemLayoutRes = R.layout.layout_header_view
                })
            })

        val mEntity = MEntity().apply {
            content = "111111"
            itemType = 3
            itemLayoutRes = R.layout.layout_header_view
        }

        mRv2.vertical()
            .bindMuiltData(mTestData){holder, position, item ->
                Log.d("FiDo", "mRv2 initView: ${holder.itemViewType} ")
                when (holder.itemViewType) {
                    0-> holder.setText(R.id.mTitle,"itemType = 0 pos=${position} ${if (item?.content?.isNotEmpty() == true) item.content else ""}")
                    1-> holder.setImageResource(R.id.mIv,R.mipmap.flower)
                    3-> holder.setText(R.id.tv_header_title,item?.content)
                }
            }
            .itemClick<MEntity> { adapter, view, position ->
                Toast.makeText(this, "click rv2${position}", Toast.LENGTH_SHORT).show()
                mEntity.content = "666666"
                adapter.notifyDataSetChanged()
            }
            .addItemChildClick<MEntity>(R.id.bt_up){adapter, view, position ->
                toast("click up")
            }
            .addItemChildClick<MEntity>(R.id.bt_down){adapter, view, position ->
                toast("click down")
            }
            .addData(mEntity)
            .addData(mEntity)
            .addData(MEntity().apply {
                itemType = 0
                content = "i am new data"
            },3)

//        mRv2.vertical()
//            .divider(color = getColorCompat(R.color.purple_500), size = 1, isReplace = true)
//            .bindData(
//                mutableListOf("1", "2", "3", "呵呵我嘿嘿"),
//                R.layout.item_rv_text
//            ) { holder, position, item ->
//                holder.setText(R.id.mTitle, "$position-->${item}")
//            }
//            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
//                holder.setText(R.id.tv_header_title, "rv2 header1")
//                holder.itemView.setOnClickListener {
//                    mRv2.addHeader(mutableListOf("1"), R.layout.layout_header_view)
//                }
//            }
//            .addHeader(mutableListOf("1"), R.layout.layout_header_view) { holder, position, item ->
//                holder.setText(R.id.tv_header_title, "rv2 header2")
//                holder.itemView.setOnClickListener {
//                    mRv2.clearBeforeAdapters()
//                }
//            }
//            .addFooter(
//                mutableListOf("444", "555", "666"),
//                R.layout.layout_header_view
//            ) { holder, position, item ->
//                holder.setText(R.id.tv_header_title, "rv2 footer1")
//            }
//            .itemClick<String> { adapter, view, position ->
//                Toast.makeText(this, "pos${position}", Toast.LENGTH_SHORT).show()
//            }

        Handler().postDelayed({
            mRv.addAll(listOf(MEntity().apply {
                mType = 3
                itemLayoutRes = R.layout.layout_header_view
            }))
        }, 2000)


        layoutManager = SmoothLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRv.layoutManager = layoutManager
        layoutManager2 = SmoothLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRv2.layoutManager = layoutManager2
    }

    override fun onDestroy() {
        super.onDestroy()
        clearImageSelectCache()
    }

}

class MEntity : BaseMuiltEntity() {
    var content = ""
    var mType: Int = 0
        get() = field
        set(value) {
            itemType = value
            field = value
        }

}

