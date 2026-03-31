package com.fido.common.common_utils.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.IMyAidlInterface
import com.fido.common.IMyCallback
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.formatDateString
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcAidlBinding

/**
 * @author: HuTao
 * @date: 2026/3/27
 * @des:
 */
class AidlAc:AppCompatActivity() {

    private val TAG = "AidlAc"
    private val binding:AcAidlBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btBind.throttleClick {
            bindService()
        }

        binding.btUnbind.throttleClick {
            unbindService()
        }

        binding.btRegister.throttleClick {
            registerCallback()
        }
        binding.btUnregister.throttleClick {
            myAidlService?.unregisterCallback(callback)
            setTips("Unregister Callback",R.color.colorBgBtnNormal)
        }
    }

    private val callback = object :IMyCallback.Stub(){
        override fun onSuccess(msg: String?) {
            Log.d(TAG, "onSuccess: $msg")
            setTips(msg?.toLong()?.formatDateString(),R.color.black)

        }
    }

    private fun setTips(msg:String?,color:Int) {
        runOnUiThread {
            binding.tvTips.text = msg
            binding.tvTips.setTextColor(getColor(color))
        }
    }

    private fun registerCallback() {
        myAidlService?.registerCallback(callback)
    }

    private var myAidlService:IMyAidlInterface?=null
    private val serverConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myAidlService = IMyAidlInterface.Stub.asInterface(service)
            Log.d(TAG, "onServiceConnected: ")
            setTips("Bind Success ServiceConnected",R.color.colorBgBtnNormal)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myAidlService = null
            Log.d(TAG, "onServiceDisconnected: ")
            setTips("onServiceDisconnected",android.R.color.holo_red_light)
        }
    }

    private fun bindService() {
        kotlin.runCatching {
            val intent = Intent("com.fido.common.aidlservice.MyAidlService")
            //这里package name 要设置com.fido.common.common_utils 不是 com.fido.common
            //因为 gradle 里面定义的是 applicationId "com.fido.common.common_utils"
            intent.setPackage("com.fido.common.common_utils")
            val result = bindService(intent,serverConnection, BIND_AUTO_CREATE)
            toast("bind server result=$result")
        }.onFailure {
            Log.e(TAG, "bindService error: ",it )
        }
    }

    private fun unbindService() {
        kotlin.runCatching {
            unbindService(serverConnection)
            toast("unbind server")
        }.onFailure {
            Log.e(TAG, "unbindService error: ",it )
        }
    }

}