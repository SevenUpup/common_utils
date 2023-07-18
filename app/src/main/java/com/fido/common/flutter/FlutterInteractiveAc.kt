package com.fido.common.flutter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_utils.databinding.AcFlutterInteractiveBinding

/**
@author FiDo
@description: 跳转时 需去 AndroidManifest 注册 FlutterActivity
@date :2023/7/18 16:56
 */
class FlutterInteractiveAc:AppCompatActivity() {

    val binding:AcFlutterInteractiveBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bt.throttleClick {
            toFlutterActivity()
        }

    }

    //跳转到 FlutterActivity , 跳转时 需去 AndroidManifest 注册 FlutterActivity
    fun toFlutterActivity() {
//        val intent = FlutterActivity.createDefaultIntent(this)
//        startActivity(intent)
    }

}