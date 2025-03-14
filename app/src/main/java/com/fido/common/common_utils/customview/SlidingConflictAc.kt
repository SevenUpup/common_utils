package com.fido.common.common_utils.customview

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.horizontal
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_ui.util.dp
import com.fido.common.common_base_util.ext.children
import com.fido.common.common_base_util.ext.setDrawable
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.ext.widthAndHeight
import com.fido.common.common_utils.rv.bindData
import com.fido.common.common_utils.rv.setText
import com.fido.common.databinding.AcSlidingConflictBinding

/**
 * @author: FiDo
 * @date: 2025/3/12
 * @des:  滑动冲突Ac
 */
class SlidingConflictAc:AppCompatActivity() {

    private val binding:AcSlidingConflictBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private val data = Array(10){it.toString()}.toList()


    private fun initView() {
        binding.apply {
            dragContainer.setHintTextView {
                setDrawable(leftDrawable = R.mipmap.back_black)
            }.setOnShowMoreListener {
                toast("进入更多页面")
            }
            rvDragLookMore.horizontal()
                .bindData(data, R.layout.item_rv_img) { holder, position, item ->
                    holder.itemView.apply {
                        widthAndHeight(80.dp,100.dp)
                        findViewById<ImageView>(R.id.mIv).setImageResource(R.mipmap.flower)
                    }
                }

            rv1.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            rv2.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            mScrollView.setRecyclerView(rv3)
            rv3.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            recyclerView4.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }


            container.children.forEach {
                if (it is RecyclerView) {
                    (it as RecyclerView).vertical()
                        .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                            holder.setText(R.id.mTitle, item)
                        }
                }
            }
        }
    }

}