package com.fido.common.common_utils.crash

import android.app.Application
import android.content.Context
import android.os.Build
import com.fido.common.common_utils.crash.pojo.CrashPortray
import java.io.File

/**
 * @author: FiDo
 * @date: 2024/3/5
 * @des:
 */
object CrashPortrayHelper {

    private var crashPortrayConfig: List<CrashPortray>? = null
    private lateinit var application: Application
    private lateinit var actionImpl: IApp

    /**
     * 供 APP 调用，提供：
     * 1. 全局上下文
     * 2. crash 保护的配置
     * 3. 相应操作的实现
     *
     * 外部需要实现：
     * 1. IApp 接口，提供相应操作的实现
     * 2. 在 Application#onCreate() 中调用此方法
     * 3. 自定义配置文件的下载、缓存、读取逻辑
     */
    fun init(application: Application, config: List<CrashPortray>?, actionImpl: IApp) {
        CrashPortrayHelper.application = application
        crashPortrayConfig = config
        CrashPortrayHelper.actionImpl = actionImpl
        CrashUncaughtExceptionHandler.init()
    }

    fun needBandage(throwable: Throwable): Boolean {
        if (crashPortrayConfig.isNullOrEmpty()) {
            return false
        }

        val config: List<CrashPortray>? = crashPortrayConfig
        if (config.isNullOrEmpty()) {
            return false
        }
        for (i in config.indices) {
            val crashPortray = config[i]
            if (!crashPortray.valid()) {
                continue
            }

            //1. app 版本号
            if (crashPortray.appVersion.isNotEmpty()
                && !crashPortray.appVersion.contains(actionImpl.getVersionName(application))
            ) {
                continue
            }

            //2. os_version
            if (crashPortray.osVersion.isNotEmpty()
                && !crashPortray.osVersion.contains(Build.VERSION.SDK_INT)
            ) {
                continue
            }

            //3. model
            if (crashPortray.model.isNotEmpty()
                && crashPortray.model.firstOrNull { Build.MODEL.equals(it, true) } == null
            ) {
                continue
            }

            val throwableName = throwable.javaClass.simpleName
            val message = throwable.message ?: ""
            //4. class_name
            if (crashPortray.className.isNotEmpty()
                && crashPortray.className != throwableName
            ) {
                continue
            }

            //5. message
            if (crashPortray.message.isNotEmpty() && !message.contains(crashPortray.message)
            ) {
                continue
            }

            //6. stack
            if (crashPortray.stack.isNotEmpty()) {
                var match = false
                throwable.stackTrace.forEach { element ->
                    val str = element.toString()
                    if (crashPortray.stack.find { str.contains(it) } != null) {
                        match = true
                        return@forEach
                    }
                }
                if (!match) {
                    continue
                }
            }

            //7. 相应操作
            if (crashPortray.clearCache == 1) {
                actionImpl.cleanCache(application)
            }
            if (crashPortray.finishPage == 1) {
                actionImpl.finishCurrentPage()
            }
            if (crashPortray.toast.isNotEmpty()) {
                actionImpl.showToast(application, crashPortray.toast)
            }
            return true
        }
        return false
    }


    fun getCrashPortrayConfig(): List<CrashPortray>? {
        TODO()
    }

    fun getAppImp(): IApp {
        return object :IApp{
            override fun showToast(context: Context, msg: String) {
                TODO("Not yet implemented")
            }

            override fun cleanCache(context: Context) {
                TODO("Not yet implemented")
            }

            override fun finishCurrentPage() {
                TODO("Not yet implemented")
            }

            override fun getVersionName(context: Context): String {
                TODO("Not yet implemented")
            }

            override fun donwloadFile(url: String): File? {
                TODO("Not yet implemented")
            }

            override fun readStringFromCache(key: String): String {
                TODO("Not yet implemented")
            }

            override fun writeStringToCache(file: File, content: String) {
                TODO("Not yet implemented")
            }

        }
    }



}