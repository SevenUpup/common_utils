package com.fido.common.utils.dex_classloader

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream

/**
 * @author: HuTao
 * @date: 2026/7/22
 * @des:
 */
class ApkLoader(private val context: Context) {

    private val TAG = "ApkLoader"
    private var dexClassLoader: DexClassLoader? = null

    private var pluginResources: Resources? = null
    private var pluginPackageName: String? = null
    private var pluginContext: PluginContext? = null

    private val optimizedDir: File = File(context.cacheDir, "optimized_dex")

    fun loadApkFromAssets(apkName: String,skipWhenExit: Boolean=true) {
        val apkPath = copyApkFromAssets(apkName,skipWhenExit)

        loadApk(apkPath, optimizedDir.absolutePath)
    }

    fun loadApk(apkPath: String, optimizedDirectory: String = optimizedDir.absolutePath) {

        if (!optimizedDir.exists()) {
            optimizedDir.mkdirs()
        }

        // 1. 创建DexClassLoader
        dexClassLoader = DexClassLoader(
            apkPath,
            optimizedDirectory,
            null,
            context.classLoader
        )

        try {
            //2. 获取插件的包名
            val packageInfo = context.packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
            pluginPackageName = packageInfo?.packageName

            //3.创建AssetsManager
            val assetsManager = AssetManager::class.java.newInstance()
            val addAssetPathMethod = AssetManager::class.java.getMethod("addAssetPath", String::class.java)
            addAssetPathMethod.invoke(assetsManager,apkPath)
            pluginResources = Resources(
                assetsManager,
                context.resources.displayMetrics,
                context.resources.configuration
            )

            val icResId = getResourceId("ic_launcher", "mipmap")
            logd("loadApk Test Get ic_launcher resId:$icResId")

            //4. 创建插件Context（使用自定义ContextWrapper，因为插件未安装）
            pluginContext = PluginContext(context, pluginResources!!, pluginPackageName ?: "", dexClassLoader!!)

        } catch (e: Exception) {
            loge("loadApk exception:${e.message}")
        }
    }

    fun <T> loadClass(name: String) = dexClassLoader?.loadClass(name) as Class<T>?

    /**
     * 获取apk自定资源的Id - 但是使用时需要使用插件的context,使用宿主的context会报错
     * @param name 资源名称，例如ic_back
     * @param type 资源类型，例如 mipmap
     */
    fun getResourceId(name: String, type: String) = pluginResources?.getIdentifier(name, type, pluginPackageName) ?: 0

    fun getRealResDrawable(name: String,type: String): Drawable {
        if (pluginResources == null) {
            throw IllegalStateException("Plugin resources not initialized")
        }
        val resId = getResourceId(name,type)
        return ResourcesCompat.getDrawable(pluginResources!!, resId, pluginContext?.theme) ?: throw IllegalArgumentException("Resource not found: $name")
    }

    /**
     * 从插件的布局文件中获取 View
     * @param xmlName 布局文件名，例如 activity_main
     * @param parent 父容器，可以为 null
     */
    fun getViewFromLayoutName(xmlName: String,parent: ViewGroup?=null): View? {
        return getPluginContext()?.let { context ->
            val layoutId = getResourceId(xmlName,"layout")
            val inflater = LayoutInflater.from(context)
            val xmlParser = context.resources.getLayout(layoutId)
            inflater.inflate(xmlParser, parent, false)
        }
    }

    /**
     * 获取插件的Context
     */
    fun getPluginContext(): PluginContext? {
        return pluginContext
    }

    /**
     * 自定义PluginContext，使用插件的Resources
     */
    class PluginContext(
        val base: Context,
        private val pluginRes: Resources,
        private val packageName: String,
        private val pluginClassLoader: ClassLoader
    ) : ContextWrapper(base) {

        override fun getResources(): Resources {
            return pluginRes
        }

        override fun getAssets(): AssetManager {
            return pluginRes.assets
        }

        override fun getPackageName(): String {
            return packageName
        }

        override fun getClassLoader(): ClassLoader {
            // 返回插件的ClassLoader
            return pluginClassLoader
        }

        override fun getTheme(): Resources.Theme {
            return pluginRes.newTheme()
        }

        override fun getSystemService(name: String): Any? {
            // 对于LayoutInflater，需要特殊处理
            if (LAYOUT_INFLATER_SERVICE == name) {
                // 使用插件Resources创建LayoutInflater
                val inflater = base.getSystemService(name) as LayoutInflater
                return inflater.cloneInContext(this)
            }
            return super.getSystemService(name)
        }
    }

    /**
     * 从assets目录复制APK到内部存储
     * @param apkAssetPath assets目录下的APK路径
     * @param skipWhenExit 当APK已经存在时是否跳过复制(文件不完整时，跳过可能会有问题) false 时每次都会copy一份
     */
    private fun copyApkFromAssets(apkAssetPath: String,skipWhenExit: Boolean=true): String {
        // 去掉assets/前缀，因为AssetManager.open()不需要这个前缀
        val actualPath = if (apkAssetPath.startsWith("assets/")) {
            apkAssetPath.substringAfter("assets/")
        } else if (apkAssetPath.startsWith("assets\\")) {
            apkAssetPath.substringAfter("assets\\")
        } else {
            apkAssetPath
        }

        val apkFileName = actualPath.substringAfterLast("/")
        val destFile = File(context.cacheDir, apkFileName)

        // 如果已经复制过，直接返回
        if (skipWhenExit && destFile.exists()) {
            Log.d(TAG, "APK already copied: ${destFile.absolutePath}")
            return destFile.absolutePath
        }

        // 从assets复制APK
        try {
            context.assets.open(actualPath).use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d(TAG, "APK copied to: ${destFile.absolutePath}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy APK from assets: $actualPath", e)
            throw RuntimeException("Failed to copy APK from assets: $actualPath", e)
        }

        return destFile.absolutePath
    }

}