package com.fido.common.common_utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.text.TextUtils
import com.drake.debugkit.DevTool
import com.facebook.stetho.Stetho
import com.fido.common.BuildConfig
import com.fido.common.R
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.log.LogUtils
import com.fido.common.common_base_util.util.global_gray.GlobalGray
import com.fido.common.common_base_util.util.sp.spValue
import com.fido.common.common_base_util.util.toast.ToastConfig
import com.fido.common.common_base_util.util.toast.interfaces.ToastGravityFactory
import com.fido.common.common_utils.privacy.WhaleHook
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        logd("App onCreate $this processName=${getProcessName(this)}")
        val processName = getProcessName(this, Process.myPid()) //根据进程id获取进程名
        if (!TextUtils.isEmpty(processName) && processName.equals(this.packageName)) {
            //隐私合规调试工具
            WhaleHook.init()

            //初始化逻辑
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
//                CrashProtectUtil().init()
                //新 兜底
//                CrashPortrayHelper.init(this,getCrashPortrayConfig(),getAppImp())
            }
            CrashWoodpecker.instance()
                .withKeys("widget", "me.drakeet")
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPatchDialogUrlToOpen("https://drakeet.me")
                .setPassToOriginalDefaultHandler(true)
                .flyTo(this)

            Stetho.initializeWithDefaults(this)
        }

        if (MainActivity.globalGraySpKey.spValue("").isNotBlank()) {
            GlobalGray.hook()
        }
    }

    fun getProcessName(context: Context, pid: Int): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (procInfo in runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName
            }
        }
        return null
    }

    fun getProcessName(app:Application):String{
        val myPid = Process.myPid()
        val am = app.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = am.runningAppProcesses
        runningAppProcesses.forEach {
            if (it.pid == myPid) {
                return it.processName
            }
        }
        return "null";
    }

}
