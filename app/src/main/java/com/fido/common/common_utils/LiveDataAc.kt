package com.fido.common.common_utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.toJson
import com.fido.common.common_utils.databinding.AcLivedataBinding

/**
@author FiDo
@description:
@date :2023/5/19 15:25
 */
class LiveDataAc: AppCompatActivity() {

    val binding:AcLivedataBinding by binding()

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
        }

    }

}