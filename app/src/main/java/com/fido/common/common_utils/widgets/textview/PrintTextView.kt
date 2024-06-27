package com.fido.common.common_utils.widgets.textview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.DefaultLifecycleObserver

/**
 * @author: FiDo
 * @date: 2024/6/27
 * @des:
 */
class PrintTextView: AppCompatTextView,DefaultLifecycleObserver {

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val text = "Hello World"

    private val handler = object :Handler(Looper.getMainLooper()){

        override fun handleMessage(msg: Message) {
            if (msg.obj == 0) {
                printText()
            } else {
                printTextBack()
            }
        }
    }

    fun start(){
        sendMsg(0)
    }

    private fun sendMsg(what:Int){
        val msg = Message.obtain()
        msg.obj = what
        handler.sendMessageDelayed(msg,18)
    }

    private var currentIndex = 0
    fun printText(){
        currentIndex += 1
        val m = text.substring(0,currentIndex)
        setText(m)
        if (currentIndex >= text.length) {
            currentIndex = text.length
            sendMsg(1)
        } else {
            sendMsg(0)
        }
    }

    fun printTextBack(){
        currentIndex-=1
        setText(text.substring(0,currentIndex))
        if (currentIndex <= 0) {
            currentIndex = 0
            sendMsg(0)
        } else {
            sendMsg(1)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (handler != null) {
            handler.removeCallbacksAndMessages(null)
        }
    }

}