package com.fido.common.common_utils.rv

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindMuiltData
import com.fido.common.common_base_ui.ext.imageview.ScrollWindowImageView
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.getDrawable
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcRvScrollWithWindownBinding

/**
@author FiDo
@description:
@date :2023/9/28 15:27
 */
class RvScrollWithWindownAc:AppCompatActivity() {

    val binding:AcRvScrollWithWindownBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mTestData = mutableListOf<MEntity>()
        for (i in 0 until 10) {
            val d = MEntity().apply {
//                itemType = if (i % 2 == 0) 0 else 1
                itemType = if (i !=2 ) 0 else 1
                itemLayoutRes = if(itemType == 0) R.layout.item_view else R.layout.item_rv_scroll_with_windown_img
                content = (i+5).toString()
            }
            mTestData.add(d)
        }

        binding.mRv.vertical()
            .bindMuiltData(mTestData){holder, position, item ->
                when (holder.itemViewType) {
                    0 -> {

                    }
                    1->{
                        holder.getView<ScrollWindowImageView>(R.id.mIv).apply {
//                            setImageDrawable(if (position%2 == 0) R.drawable.avocado.getDrawable else R.drawable.ic_zelda.getDrawable)
                            setImageDrawable(R.drawable.bg.getDrawable)
                        }
                    }
                }
            }

        binding.mRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val childCount: Int = layoutManager.childCount
                for (i in 0 until childCount) {
                    val view: View? = layoutManager.getChildAt(i)
                    val tag = view?.tag
//                    if (view is ScrollWindowImageView) {
                        if (view is ViewGroup) {
                            val img = view.getChildAt(0)
                            if (img is ScrollWindowImageView) {
                                val parentBottom = recyclerView.bottom
                                val itemBottom = view.bottom
                                if (itemBottom < parentBottom) {
                                    val scrollY  = parentBottom - itemBottom
                                    img.scrollWindow(scrollY.toFloat())
                                }
                            }
                        }
                }
            }
        })

    }

}