package com.fido.common.common_utils

import android.app.Application
import android.util.Log
import com.drake.debugkit.DevTool
import com.fido.common.common_base_util.log.LogUtils
import com.fido.common.common_base_util.util.time.countDown
import com.fido.common.common_base_util.util.toast.ToastConfig
import com.fido.common.common_base_util.util.toast.interfaces.ToastGravityFactory
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class App: Application() {

    companion object{
        val instance by lazy {
            App()
        }

    }

    override fun onCreate() {
        super.onCreate()
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.purple_500, android.R.color.white)
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(
                context
            ).setDrawableSize(20F)
        }

        ToastConfig.toastFactory = ToastGravityFactory()

        DevTool.debug = BuildConfig.DEBUG

        LogUtils.logEnabled = BuildConfig.DEBUG
        LogUtils.logGlobalTag = "FiDo"
    }

}