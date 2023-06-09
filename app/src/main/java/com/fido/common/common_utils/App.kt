package com.fido.common.common_utils

import android.app.Application
import com.drake.debugkit.DevTool
import com.facebook.stetho.Stetho
import com.fido.common.CrashProtectUtil
import com.fido.common.common_base_util.log.LogUtils
import com.fido.common.common_base_util.util.toast.ToastConfig
import com.fido.common.common_base_util.util.toast.interfaces.ToastGravityFactory
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.drakeet.library.CrashWoodpecker
import me.drakeet.library.PatchMode

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

        if (BuildConfig.DEBUG) {
            //兜底策略
            CrashProtectUtil().init()
        }
        CrashWoodpecker.instance()
            .withKeys("widget", "me.drakeet")
            .setPatchMode(PatchMode.SHOW_LOG_PAGE)
            .setPatchDialogUrlToOpen("https://drakeet.me")
            .setPassToOriginalDefaultHandler(true)
            .flyTo(this)

        Stetho.initializeWithDefaults(this)
    }

}