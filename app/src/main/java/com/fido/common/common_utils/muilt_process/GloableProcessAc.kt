package com.fido.common.common_utils.muilt_process

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.startActivity
import com.fido.common.databinding.AcGloableProcessBinding

/**
 * @author: FiDo
 * @date: 2024/2/22
 * @des:  xml 设置全局 processname
 */
open class GloableProcessAc:AppCompatActivity() {

    val binding:AcGloableProcessBinding by binding()
    private val TAG = "FiDo"

    companion object{
        var GLOABLE_PROCESS_TAG = "FiDo66"

        fun gloableToast() = logd("gloable toast")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvIntent.text = "接受到跨进程intent传值：${intent.getStringExtra(Constant.INTENT_CONTENT)?:""}"

        val str = intent.getStringExtra(Constant.INTENT_CONTENT)

        val pName= Application.getProcessName()
        logd("GloableProcess pName=${pName} myPid =${Process.myPid()} GLOABLE_PROCESS_TAG =$GLOABLE_PROCESS_TAG")
        Toast.makeText(this,"接收到数据$str", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "GloableProcessAc onCreate: ")

        binding.bt.throttleClick {
            startActivity<PrivateProcessAc>(Pair(Constant.INTENT_CONTENT,"Gloable2Private"))
        }
        binding.bt2.throttleClick {
            startActivity<GloableProcessAc>()
        }

        binding.bt3.throttleClick {
            GLOABLE_PROCESS_TAG = "FiDo77"
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "GloableProcessAc onNewIntent: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "GloableProcessAc onPause: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "GloableProcessAc onResume: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "GloableProcessAc onDestroy: ")
    }

}

object Constant{
    const val INTENT_CONTENT = "INTENT_CONTENT"
}