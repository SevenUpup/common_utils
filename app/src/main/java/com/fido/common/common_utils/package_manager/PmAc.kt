package com.fido.common.common_utils.package_manager

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.databinding.AcPackageManagerBinding
import java.util.function.Function

/**
 * @author: HuTao
 * @date: 2026/7/14
 * @des:
 */
class PmAc:AppCompatActivity() {

    private val binding by binding<AcPackageManagerBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEvent()
    }

    private fun initEvent() {
        binding.btGetAllApps.throttleClick {
            binding.tvAllApps.text = installedApps().map { "name: ${it.name}, packageName: ${it.packageName}" }.joinToString("\n")
        }
    }

    //读取手机上所有可启动应用：
    fun installedApps(): List<AppInfo> {
        val pm: PackageManager = packageManager
        val result: MutableList<AppInfo> = ArrayList()
        for (app in pm.getInstalledApplications(PackageManager.GET_META_DATA)) {
            if (pm.getLaunchIntentForPackage(app.packageName) == null) {
                continue
            }
            val label = try {
                pm.getApplicationLabel(app).toString()
            } catch (ignored: Throwable) {
                app.packageName
            }
            result.add(AppInfo(label, app.packageName))
        }
        result.sortWith(
            Comparator.comparing(
                Function<AppInfo, String> { a: AppInfo -> a.name.lowercase() })
        )
        return result
    }

    data class AppInfo(
        val name: String,
        val packageName: String
    )

}