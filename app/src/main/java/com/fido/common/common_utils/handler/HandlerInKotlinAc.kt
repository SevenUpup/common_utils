package com.fido.common.common_utils.handler

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.showLoading
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.loge
import com.fido.common.databinding.AcHandlerInKotlinBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

/**
@author FiDo
@description:
@date :2023/8/8 15:51
 */
class HandlerInKotlinAc:AppCompatActivity() {

    val binding:AcHandlerInKotlinBinding by binding()

    val testTxt = "我是Ac的测试文本"

    private var mHandleMessage : ((Message)->Unit)?=null

    private val sb = StringBuilder()

    private val mWeakReferenceHandler by lazy {
        WeakReferenceHandler(this,mHandleMessage)
    }

    //子线程中的handler
    private var mThreadHandler:Handler?=null

    @SuppressLint("SoonBlockedPrivateApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showLoading()
        mHandleMessage = {
            sb.append(it.obj.toString()+"\n")
            binding.tv.text = sb.toString()
        }
        mWeakReferenceHandler.sendEmptyMessageDelayed(0,1000)

        mWeakReferenceHandler.sendMessageDelayed("111".getHandlerMsg,1000)
        mWeakReferenceHandler.sendMessageDelayed("222".getHandlerMsg,2000)
        thread {
            printCurrentThreadInfo("thread")
            mWeakReferenceHandler.sendMessageDelayed("333".getHandlerMsg,3000)
            mWeakReferenceHandler.sendMessageDelayed("444".getHandlerMsg,4000)
        }
        GlobalScope.launch {
            printCurrentThreadInfo("GlobalScope")
            mWeakReferenceHandler.sendMessageDelayed("555".getHandlerMsg,5000)
            withContext(Dispatchers.Default) {
                printCurrentThreadInfo("Dispatchers.Default")
                mWeakReferenceHandler.sendMessageDelayed("Dispatchers.Default".getHandlerMsg,6000)
            }
            withContext(Dispatchers.Unconfined){
                printCurrentThreadInfo("Dispatchers.Unconfined")
                mWeakReferenceHandler.sendMessageDelayed("Dispatchers.Unconfined".getHandlerMsg,7000)
            }
            withContext(Dispatchers.IO){
                printCurrentThreadInfo("Dispatchers.IO")
                mWeakReferenceHandler.sendMessageDelayed("Dispatchers.IO".getHandlerMsg,8000)
            }
            withContext(Dispatchers.Main){
                printCurrentThreadInfo("Dispatchers.Main")
                mWeakReferenceHandler.sendMessageDelayed("Dispatchers.Main".getHandlerMsg,9000)
            }
        }


        thread {
            Looper.prepare()
            mThreadHandler = Handler {
                resetHandleText = it.obj.toString()
                false
            }
            Looper.loop()
        }

        binding.apply {
            btMain2main.throttleClick {
                _thread2MainHandler.sendMessage("当前线程:${Thread.currentThread().name}-发送消息更新UI线程\nHandler中线程:${_thread2MainHandler.looper.thread.name}".getHandlerMsg)
                val msg = Message.obtain()
                msg.obj = BitmapFactory.decodeResource(resources, R.drawable.bg)
                _thread2MainHandler.sendMessage(msg)
            }
            btThread2main.throttleClick {
                thread {
                    _thread2MainHandler.sendMessage("${Thread.currentThread().name}-发送消息更新UI线程\nHandler中线程:${_thread2MainHandler.looper.thread.name}".getHandlerMsg)
                }
            }
            btMain2thread.throttleClick {
                mThreadHandler?.sendMessage("${Thread.currentThread().name}-发送消息更新子线程\nHandler中线程:${mThreadHandler?.looper?.thread?.name}".getHandlerMsg)
            }
            btThread2thread.throttleClick {
                thread {
                    kotlin.runCatching {
                        mThreadHandler?.sendMessage("当前线程:${Thread.currentThread().name}-发送消息更新子线程,\nHandler中线程:${mThreadHandler?.looper?.thread?.name}".getHandlerMsg)
                    }
                }
            }

            // ============================= 两个Handler在不同子线程中相互通信 ==============================
            var index = 0
            binding.tvNumber.text =  "当前线程:"+Thread.currentThread().name + " \nindex=${index}"
            var thread1Handler:Handler? = null
            var thread2Handler:Handler?=null
            thread {
                Looper.prepare()
                thread1Handler = Handler {
                    threadHandleText = "当前线程:"+Thread.currentThread().name + " 当前Handler:${thread1Handler} 收到消息后index++${index++}"
                    if (thread2Handler != null) {
                        thread2Handler?.sendMessageDelayed("".getHandlerMsg,1000L)
                    }
                    false
                }
                Looper.loop()
            }
            thread {
                Looper.prepare()
                thread2Handler = Handler {
                    threadHandleText = "当前线程:"+Thread.currentThread().name + " 当前Handler:${thread2Handler} 收到消息后index++${index++}"
                    if (thread1Handler != null) {
                        thread1Handler?.sendMessageDelayed("".getHandlerMsg,1000L)
                    }
                    false
                }
                Looper.loop()
            }
            btThread2thread2.throttleClick {
                thread1Handler?.sendMessage("".getHandlerMsg)
            }
        }

    }

    private val _thread2MainHandler = Handler(Looper.getMainLooper()
    ) {
        if (it.obj is Bitmap){
            binding.ivBg.setImageBitmap(it.obj as Bitmap)
        }else{
            resetHandleText = it.obj.toString()
        }
        false
    }

    private var threadHandleText = ""
        set(value) {
            binding.tvNumber.text = value
            field = value
        }

    private var resetHandleText = ""
        set(value) {
            binding.tv2.text = value
            field = value
        }

    private fun printCurrentThreadInfo(prefix:String = ""):String{
        val result = "${prefix}-${Thread.currentThread().id}-${Thread.currentThread().name}"
        loge(result)
        return result
    }
    private val String.getHandlerMsg
        get() = run {
            val msg = Message.obtain()
            msg.obj = this
            msg.what = 0
            msg
        }

    //默认静态内部类
    class WeakReferenceHandler(ac:HandlerInKotlinAc, private val handleMessage:((Message)->Unit)?=null) : Handler(Looper.getMainLooper()){
        private val weakReference = WeakReference(ac)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            handleMessage?.invoke(msg)

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