package com.fido.common.common_utils.sp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.util.sp.putSp
import com.fido.common.common_base_util.util.sp.spValue
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcSpTestBinding
import kotlin.properties.Delegates

/**
@author FiDo
@description:
@date :2023/8/9 11:10
 */
class SPAc : AppCompatActivity() {

    val binding: AcSpTestBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        initEvent()
    }

    private var lastClickTime by Delegates.observable(0L) { property, oldValue, newValue ->
        if (newValue - oldValue > 1000) {
            plusSp()
        } else {
            toast("没到时间，别点")
        }
    }

    private fun plusSp() {
        commonPreferences.addValue("plusValue", "plus00")
        bindRvData()
    }

    private fun initEvent() {
        binding.plus2.click {
            "我是通过扩展函数添加的".putSp("hahaha")
            123.putSp("hehehe")
            bindRvData()
        }

        binding.plus.setOnClickListener {
            lastClickTime = System.currentTimeMillis()
        }

        binding.delete.click {
            commonPreferences.apply {
                isFirst = false
                userAge = 0
                userName = ""
                userHeight = 0.0f
            }
            bindRvData()
        }

        binding.edit.click {
            commonPreferences.apply {
                userAge = 99
                userHeight = 188.0f
                userName = "edit"
            }
            bindRvData()
        }
    }

    private val commonPreferences = CommonPreferences()
    private var data = mutableListOf<String>()

    private fun initView() {

        binding.mRv.vertical()
        bindRvData()
    }

    private fun bindRvData() {
        data = with(commonPreferences) {
            mutableListOf(
                isFirst.toString(), userName, userAge.toString(), userHeight.toString(),
                "plusValue".spValue("",CommonPreferences.DEULT_FILE_NAME),
                "hahaha".spValue(""),
                "hehehe".spValue(0).toString(),
            )
        }
        binding.mRv.bindData(data, R.layout.item_rv_text) { holder, position, item ->
            holder.setText(R.id.mTitle, item)
        }
    }

}