package com.fido.common.common_utils

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.dp
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.getColor
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.common_base_util.toJson
import com.fido.common.common_utils.databinding.AcLivedataBinding

/**
@author FiDo
@description:
@date :2023/5/19 15:25
 */
class LiveDataAc: AppCompatActivity() {

    val binding: AcLivedataBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wifi = WifiListLiveData()
        wifi.observe(this){
            it?.forEach {
                loge("Wifi scanResult = ${it.toJson()}")
            }
        }

        NetworkAvailableLiveData().observe(this){
            loge("net available is $it")
        }

        binding.root.click {
            toast("click root")
            wifi.startScan()
            addView()
        }

        binding.container.throttleClick {
            addView()
        }

    }

    fun addView(){
        binding.container.addView(View(this@LiveDataAc).apply {
            widthAndHeight(getScreenWidthPx(),100.dp)
            setBackgroundColor(R.color.black.getColor)
        })
        binding.container.translationY = 200f
    }

}