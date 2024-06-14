package com.fido.common.common_utils.sp

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.util.sp.putSp
import com.fido.common.common_base_util.util.sp.spValue
import com.fido.common.R
import com.fido.common.common_base_util.data_store.EasyStore
import com.fido.common.databinding.AcSpTestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        val b = kotlin.run {
            11
            Unit
        }
        val wb = WebView(this).settings.run {
            javaScriptEnabled = true
        }
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

        binding.btAddData.click {
            GlobalScope.launch {
                EasyStore.put("FiDo",122)
                withContext(Dispatchers.Main){
                    binding.tvDataStore.text = EasyStore.get("FiDo",0).toString()
                }
            }
        }
    }

    private val commonPreferences = CommonPreferences()
    private var data = mutableListOf<String>()

    private fun initView() {

        GlobalScope.launch {
            val block = Block()
            block.invoke()
        }
        
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

    class Block : suspend ()->String{
        override suspend fun invoke():String {
            return withContext(Dispatchers.IO){
                //模拟耗时
                delay(2000)
                "result"
            }
        }
    }

}