package com.fido.common.common_utils.rv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindAdapter
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.setEmptyView
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.R
import com.fido.common.databinding.AcRvEmptyViewBinding

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des:
 */
class RvEmptyViewAc:AppCompatActivity() {

    val binding:AcRvEmptyViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 当访问name时，就会从map这个散列表中获取key为"name"的value值并返回，不存在就直接抛异常
        val map = mutableMapOf<String, Int>()
        map["name"] = 20
        val name: Int by map

        val list = mutableListOf<String>()
        for (c in 'a'..'z') {
            list.add(c.toString())
        }
        binding.apply {
            btClear.throttleClick {
                mRv.bindAdapter.submitList(emptyList())
            }
//            TextView(mRv.context).apply {
//                text = "i am emptyview"
//                widthAndHeight(context.getScreenWidthPx(),context.getScreenHeightPx())
//                gravity = Gravity.CENTER
//                setTextColor(R.color.colorBgBtnText.getColor)
//                textSize = 18.sp.toFloat()
//            }
            mRv.setEmptyView(this@RvEmptyViewAc,binding.emptyView)

            mRv.vertical()
                .bindData(list, R.layout.item_rv_text){holder, position, item ->
                    holder.setText(R.id.mTitle,item)
                }
        }

    }


}