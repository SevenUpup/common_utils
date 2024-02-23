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

/**
 * @author: FiDo
 * @date: 2024/2/22
 * @des:  xml 设置私有（:） processname
 */
class Private2ProcessAc:AppCompatActivity() {

    private val TAG = "FiDo"
    val binding:AcPrivateProcessBinding by binding()

    companion object{
        var PRIVATE2TAG = "tag2"

        fun private2StaticFun() = logd("private2 StaticFun${PRIVATE2TAG}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Private2ProcessAc onCreate: GLOABLE_PROCESS_TAG=${GloableProcessAc.GLOABLE_PROCESS_TAG} ${GloableProcessAc.gloableToast()}")

        binding.tvIntent.text = "接收到跨进程Intent值为：${intent.getStringExtra(Constant.INTENT_CONTENT)?:""}"
        binding.bt.isGone = true
        binding.bt2.apply {
            text = "launch PrivateProcess3Ac"
            throttleClick {
                startActivity<Private3ProcessAc>(Pair(Constant.INTENT_CONTENT,"private2ToPrivate3 intent value"))
            }
        }

        binding.tvTag.text = PRIVATE2TAG
        binding.bt3.throttleClick {
            PRIVATE2TAG = "tag3"
            binding.tvTag.text = PRIVATE2TAG
            binding.tvTag2.text = tag2Text
        }

        binding.bt4.apply {
            text = "Private2调用Private3的静态代码测试"
            throttleClick {
                Private3ProcessAc.private3StaticFun()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Private2ProcessAc onPause: ")
    }

    private val tag2Text
        get() = "Private2与Private3为同一个私有进程(fido2) \nprivate2Tag=${PRIVATE2TAG} \nprivate3Tag=${Private3ProcessAc.PRIVATE3TAG}" +
                "\n如果更改Private3ProcessAc的TAG值，当前Private2的值也会同步修改，说明同一进程的静态变量是可见的"

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Private2ProcessAc onResume: ")
        binding.tvTag2.text = tag2Text
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