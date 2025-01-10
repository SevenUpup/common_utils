package com.fido.common.common_utils.rv

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.imageview.ScrollWindowImageView
import com.fido.common.common_base_ui.ext.imageview.loadDrawable
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.databinding.AcRvScrollWithWindownBinding

/**
@author FiDo
@description: ScrollWindowImageView 在Rv中使用 需要提前缓存Drawable 否则bind时会重绘
@date :2023/9/28 15:27
 */
class RvScrollWithWindownAc:AppCompatActivity() {

    val binding:AcRvScrollWithWindownBinding by binding()
    val mTestData = mutableListOf<MEntity>()
    val list = mutableSetOf<Drawable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (i in 0 until 50) {
            val d = MEntity().apply {
//                itemType = if (i % 2 == 0) 0 else 1
                itemType = if (i ==6 || i == 3) 1 else 0
                itemLayoutRes = if(itemType == 0) R.layout.item_view else R.layout.item_rv_scroll_with_windown_img
                content = (i+5).toString()
            }
            mTestData.add(d)
        }


        loadDrawable(this,"https://gd-hbimg.huaban.com/03d9e7fbe014cea4ea0b25c718f733db74eae9bc2096be-qByP1C_fw658webp"){
            it?.let { drawable ->
                list.add(drawable)
                bindRv()
            }

        }

//        bindRv()

        /*binding.mRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var firstScrollY = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val childCount: Int = layoutManager.childCount
                for (i in 0 until childCount) {
                    val view: View? = layoutManager.getChildAt(i)
                    val tag = view?.tag
//                    if (view is ScrollWindowImageView) {
//                        if (view is ViewGroup) {
//                            val img = view.getChildAt(0)
                            if (view is ScrollWindowImageView) {
                                val parentBottom = recyclerView.bottom
                                val itemBottom = view.bottom
                                loge("parentBottom=$parentBottom itemBottom=$itemBottom scroolY=${parentBottom - itemBottom - firstScrollY}")
                                if (itemBottom < parentBottom) {
                                    val scrollY  = parentBottom - itemBottom
                                    if (firstScrollY == 0) {
                                        firstScrollY = scrollY
                                        loge("firstScrollY=$firstScrollY ")
                                    } else {
                                        view.scrollWindow(scrollY.toFloat() - firstScrollY)
                                    }
                                }
                            }
//                        }
                }
            }
        })*/

    }

    private fun bindRv() {
        binding.mRv.vertical()
        val mAdapter = RvScrollWindowAdapter(mTestData)
        binding.mRv.adapter = mAdapter
//        binding.mRv.vertical()
//            .bindMuiltData(mTestData){holder, position, item ->
//                when (holder.itemViewType) {
//                    0 -> {
//
//                    }
//                    1->{
//                        holder.getView<ScrollWindowImageView>(R.id.mIv).apply {
////                            scrollWindow(binding.mRv)
////                            setImageDrawable(R.drawable.bg.getDrawable)
//                            if (drawable == null && !list.isNullOrEmpty()) {
//                                setImageDrawable(list.first())
//                                scrollWindow(binding.mRv)
//                            } else {
//
//                            }
//                        }
//                    }
//                }
//            }

    }


    inner class RvScrollWindowAdapter(val data: MutableList<MEntity>): RecyclerView.Adapter<RvScrollWindowAdapter.VH>(){

        override fun getItemViewType(position: Int): Int {
            return data.get(position).itemType
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): VH {
            if (viewType == 0) {
                return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false))
            }else{
                return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_scroll_with_windown_img,parent,false))
            }
        }

        override fun onBindViewHolder(
            holder: VH,
            position: Int
        ) {
            if (data.get(position).itemType == 1) {
                holder.itemView.findViewById<ScrollWindowImageView>(R.id.mIv)?.apply {
                    if (drawable == null && !list.isNullOrEmpty()) {
                        setImageDrawable(list.first())
                        scrollWindow(binding.mRv)
                    } else {

                    }
                }
            }
        }

        override fun getItemCount(): Int = data?.size?:0

        inner class VH(itemView: View): RecyclerView.ViewHolder(itemView) {

        }

    }

}