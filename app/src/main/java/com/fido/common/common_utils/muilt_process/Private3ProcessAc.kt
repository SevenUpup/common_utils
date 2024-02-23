package com.fido.common.common_utils.muilt_process

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.isGone
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.startActivity
import com.fido.common.common_utils.databinding.AcPrivateProcessBinding
import com.fido.common.common_utils.muilt_process.Private2ProcessAc.Companion.PRIVATE2TAG

/**
 * @author: FiDo
 * @date: 2024/2/22
 * @des:  xml 设置私有（:） processname
 */
class Private3ProcessAc:AppCompatActivity() {

    val binding:AcPrivateProcessBinding by binding()

    private val TAG = "FiDo"
    companion object{
        var PRIVATE3TAG = "private33"

        fun private3StaticFun() = logd("private3 private3StaticFun${PRIVATE3TAG}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bt.isGone = true
        binding.bt2.isGone = true

        binding.tvIntent.text = "接收到同进程Intent值为：${intent.getStringExtra(Constant.INTENT_CONTENT)?:""}"

        binding.tvTag.text = PRIVATE3TAG
        binding.bt3.text = "修改Private3Process的静态变量值为 private44"
        binding.bt3.throttleClick {
            PRIVATE3TAG = "private44"
            binding.tvTag.text = PRIVATE3TAG
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Private2ProcessAc onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Private2ProcessAc onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Private2ProcessAc onDestroy: ")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "Private2ProcessAc onNewIntent: ")
    }

}