package com.fido.common.common_utils.rv

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindMuiltData
import com.fido.common.common_base_ui.ext.imageview.ScrollWindowImageView
import com.fido.common.common_base_ui.ext.imageview.loadDrawable
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.R
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
            .bindMuiltData(mTestData){holder, position, item ->
                when (holder.itemViewType) {
                    0 -> {

                    }
                    1->{
                        holder.getView<ScrollWindowImageView>(R.id.mIv).apply {
//                            scrollWindow(binding.mRv)
//                            setImageDrawable(R.drawable.bg.getDrawable)
                            if (drawable == null && !list.isNullOrEmpty()) {
                                setImageDrawable(list.first())
                                scrollWindow(binding.mRv)
                            } else {

                            }
                        }
                    }
                }
            }

    }

}