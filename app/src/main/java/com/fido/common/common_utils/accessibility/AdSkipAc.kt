package com.fido.common.common_utils.accessibility

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.context
import com.fido.common.common_base_util.util.accessbility.jumpAccessibilityServiceSettings
import com.fido.common.databinding.AcAdSkipBinding

/**
@author FiDo
@description:
@date :2023/9/7 16:36
 */
class AdSkipAc:AppCompatActivity() {

    val binding:AcAdSkipBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btOpenService.setOnClickListener {
            jumpAccessibilityServiceSettings(AdGunAccessibilityService::class.java,context)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshServiceStatusUI()
    }

    /**
     * 刷新无障碍服务状态的UI
     * */
    private fun refreshServiceStatusUI() {
        if (AdGunAccessibilityService.isServiceEnable) {
            binding.tvServiceStatus.text = "跳过广告服务状态：已开启"
            binding.btOpenService.visibility = View.GONE
        } else {
            binding.tvServiceStatus.text = "跳过广告服务状态：未开启"
            binding.btOpenService.visibility = View.VISIBLE
        }
    }

}