package com.fido.common.common_utils.customview.fake_taobao

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.canScrollLeft
import com.fido.common.common_base_ui.ext.horizontal
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.ext.height
import com.fido.common.common_base_util.ext.widthAndHeight
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.databinding.AcFakeTaobaoBinding

/**
 * @author: FiDo
 * @date: 2024/7/19
 * @des:
 */
class FakeTaobaoKingkongViewAc : AppCompatActivity() {

    private val binding: AcFakeTaobaoBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list  = mutableListOf<String>()
        for (i in (0 until 20)) {
            list.add("test${i}")
        }
        binding?.apply {
            binding?.mRv?.horizontal()
                ?.bindData(
                    list,
                    R.layout.item_rv_text
                ) { holder, position, item ->
                    holder.itemView.widthAndHeight(getScreenWidthPx()/5-20,50.dp)
                    holder.setText(R.id.mTitle, item)
                    holder.setGone(R.id.bt_up,true)
                    holder.setGone(R.id.bt_down,true)
                }

             var totalScrollDistance = 0
            binding?.mRv?.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // 累加水平滑动距离
                    totalScrollDistance += dx
                    // 根据滑动距离动态调整列数
                    adjustSpanCount()
                Log.e("FiDo", "onScrolled dx: $dx  binding.mRv.canScrollLeft=${binding?.mRv?.canScrollLeft}")
//                    if( dx > 0 ){
//                        if( binding?.motion?.currentState == binding?.motion?.startState){
//                            binding?.motion?.transitionToEnd()
//                        }
//                    }else{
//                        if (binding?.mRv?.canScrollLeft == true) return
//                        if( binding?.motion?.currentState == binding?.motion?.endState) {
//                            binding?.motion?.transitionToStart()
//                        }
//                    }
                }
            })
        }
    }

    private fun adjustSpanCount() {
        binding?.mRv?.height(50)
    }

}