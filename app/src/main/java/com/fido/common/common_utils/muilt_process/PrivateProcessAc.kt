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
import com.fido.common.common_utils.MainActivity
import com.fido.common.databinding.AcPrivateProcessBinding
import com.fido.common.common_utils.test.FunTest.Companion.also

/**
 * @author: FiDo
 * @date: 2024/2/22
 * @des:  xml 设置私有（:） processname
 */
class PrivateProcessAc:AppCompatActivity() {
    private val TAG = "FiDo"
    val binding:AcPrivateProcessBinding by binding()

    var times = 1
    fun Int.multipy() = this * times

    fun runAsOuter(block:()->Unit){
        block()  //等价于 this.block() ,而this的类型是 PrivateProcessAc
    }

//    override fun reportFullyDrawn() {
//        super.reportFullyDrawn()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.times.multipy()


        runAsOuter {
            3.multipy()
        }

        binding.bt.also {
            
        }

        val bt = binding.bt.run {
            123
        }

        Log.d("FiDo", "由于跨进程所以 Private MAIN_MUILT_PROCESS_TAG = ${MainActivity.MAIN_MUILT_PROCESS_TAG} 还是初始值")
        kotlin.runCatching {
            Log.d(TAG, "PrivateProcessAc onCreate: GLOABLE_PROCESS_TAG=${GloableProcessAc.GLOABLE_PROCESS_TAG} PRIVATE_PROCESS_TAG =${Private2ProcessAc.PRIVATE2TAG}")
        }.onFailure {
            Log.d(TAG, "onCreate Error: ${it.message}")
        }

        binding.bt.post {

        }

        binding.tvIntent.text = "接收到跨进程Intent值为：${intent.getStringExtra(Constant.INTENT_CONTENT)?:""}"
        binding.bt3.isGone = true
        binding.bt.throttleClick {
            startActivity<GloableProcessAc>(Pair(Constant.INTENT_CONTENT,"private2Gloable"))
        }

        binding.bt2.throttleClick {
            Private2ProcessAc.private2StaticFun()
            startActivity<Private2ProcessAc>(Pair(Constant.INTENT_CONTENT,"private2Private2"))
        }

       kotlin.runCatching {
           val str = intent.getStringExtra(Constant.INTENT_CONTENT)
           val pName= Application.getProcessName()
           logd("PrivateProcess pName=${pName}")
           Toast.makeText(this,"当前进程${pName} 接收到数据$str",Toast.LENGTH_SHORT).show()
       }.onFailure {
           logd(it.message.toString())
       }

        binding.bt4.isGone = false
        binding.bt4.throttleClick {
            GloableProcessAc.gloableToast()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "PrivateProcessAc onPause: ")
    }

    override fun onResume() {
        super.onResume()
        binding.tvTag.text = "Private2ProcessAc的TAG值为：${Private2ProcessAc.PRIVATE2TAG}"

        Log.d(TAG, "PrivateProcessAc onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "PrivateProcessAc onDestroy: ")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "PrivateProcessAc onNewIntent: ")
    }

}