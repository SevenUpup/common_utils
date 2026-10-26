package com.fido.common.common_utils.dexclassloader

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.widthAndHeight
import com.fido.common.databinding.AcDexClassloaderBinding
import com.fido.common.utils.dex_classloader.ApkLoader

/**
 * @author: HuTao
 * @date: 2026/7/22
 * @des:
 */
class DexClassLoaderAc: AppCompatActivity() {

    private val TAG = "DexClassLoaderAc"
    private val binding: AcDexClassloaderBinding by binding()

    private val apkLoader: ApkLoader by lazy {
        ApkLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runCatching {
            apkLoader.loadApkFromAssets("debugkit.apk",false)

            binding.btLoadMethod.throttleClick {
                val clazz = apkLoader.loadClass<Any>("com.drake.statusbar.StatusBarKt") as Class<Any>
                val getNavigationBarHeightMethod = clazz.getMethod("getNavigationBarHeight", Context::class.java)
                val value = getNavigationBarHeightMethod.invoke(null, apkLoader.getPluginContext())
                binding.tvInfo.text = "${binding.tvDexClassloader.text}\nNavigationBarHeight:$value"
            }

            val statusBarClazz = apkLoader.loadClass<Any>("com.drake.statusbar.StatusBarKt") as Class<Any>
            val getStatusBarHeight = statusBarClazz.getMethod("getStatusBarHeight", Context::class.java).invoke(null, apkLoader.getPluginContext())
            binding.tvInfo.text = "statusBarHeight:$getStatusBarHeight"

            val resId = apkLoader.getResourceId("ic_menu", "drawable")
            val drawable = apkLoader.getPluginContext()?.resources?.getDrawable(resId)
            Log.d(TAG, "resId:$resId drawable:$drawable")
            binding.ivDexClassloader2.setImageDrawable(drawable)
            //下面这种直接用会报错 使用宿主的resource无法找到资源，Resources$NotFoundException
            runCatching {
                binding.ivDexClassloader.setImageResource(resId)
            }.onFailure {
                Log.e(TAG, "onFailure: ${it.message}", it)
                binding.tvDexClassloader.text = "DexClassLoader 加载apk资源失败\n${it.message}"
            }

            val view = apkLoader.getViewFromLayoutName("custom_dialog")
            view?.let {
                binding.llApkViewContainer.addView(it)
                it.widthAndHeight(900,600)
            }
            val drawableSettings = apkLoader.getRealResDrawable("ic_launcher_round", "mipmap")
            val ivSettings = ImageView(this@DexClassLoaderAc)
            Log.d(TAG, "getViewFromLayoutName view: $view getRealResDrawable ivSettings=$ivSettings")
            ivSettings.setImageDrawable(drawableSettings)
            binding.llApkViewContainer.addView(ivSettings)
            ivSettings.widthAndHeight(300,300)

        }.onFailure {
            Log.e(TAG, "onFailure: ${it.message}", it)
        }

    }

}