package com.fido.common.common_base_util.util.toast

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.util.toast.interfaces.ToastFactory

@SuppressLint("StaticFieldLeak")
object ToastConfig {

    internal var context:Context = app

    internal var toast:Toast? = null

    /** 构建吐司 */
    @JvmField
    var toastFactory:ToastFactory = ToastFactory

    /**
     * 初始化
     * 如果应用存在多进程使用则必须使用本方法初始化, 否则是可选
     * @param toastFactory 构建吐司
     */
    @JvmOverloads
    @JvmStatic
    fun initialize(application: Application,toastFactory: ToastFactory?=null){
        this.context = application
        if (toastFactory != null) {
            this.toastFactory = toastFactory
        }
    }

    /** 取消吐司显示 */
    @JvmStatic
    fun cancle(){
        kotlin.runCatching {
            toast?.cancel()
        }
    }

}