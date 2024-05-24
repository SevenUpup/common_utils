package com.fido.common.common_utils.motionlayout

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.ext.gradiendShapeDrawableBg
import com.fido.common.common_base_util.getColor
import com.fido.common.R
import com.fido.common.databinding.AcMotionCollapsibleBinding

/**
@author FiDo
@description:
@date :2023/6/7 15:24
 */
class MotionCollapsibleAc:AppCompatActivity() {

    val binding:AcMotionCollapsibleBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
//        val bgColor = intArrayOf(Color.parseColor("#FF03DAC5"),Color.parseColor("#FF6200EE"))
        val bgColor = intArrayOf(R.color.teal_200.getColor,R.color.teal_700.getColor,R.color.purple_500.getColor)
        binding.appbar.gradiendShapeDrawableBg(0f, bgColor ,
            orientation = GradientDrawable.Orientation.LEFT_RIGHT)

        val list  = mutableListOf<String>()
        for (i in (0 until 20)) {
            list.add("test${i}")
        }
        binding.mRv.vertical()
            .bindData(
                list,
                R.layout.item_rv_text
            ) { holder, position, item ->
                holder.setText(R.id.mTitle, item)
            }

    }

}