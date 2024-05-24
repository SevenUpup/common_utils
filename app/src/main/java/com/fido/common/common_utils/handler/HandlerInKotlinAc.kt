package com.fido.common.common_utils.handler

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.databinding.AcHandlerInKotlinBinding
import java.lang.ref.WeakReference

/**
@author FiDo
@description:
@date :2023/8/8 15:51
 */
class HandlerInKotlinAc:AppCompatActivity() {

    val binding:AcHandlerInKotlinBinding by binding()

    val testTxt = "我是Ac的测试文本"

    private val mWeakReferenceHandler by lazy {
        WeakReferenceHandler(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mWeakReferenceHandler.sendEmptyMessageDelayed(0,1000)
    }

    //默认静态内部类
    class WeakReferenceHandler(ac:HandlerInKotlinAc) : Handler(Looper.getMainLooper()){
        private val weakReference = WeakReference(ac)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            weakReference.get()?.run {
                println("${testTxt} msg what=${msg.what}")
            }
        }

    }

    override fun onDestroy() {
        mWeakReferenceHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

}