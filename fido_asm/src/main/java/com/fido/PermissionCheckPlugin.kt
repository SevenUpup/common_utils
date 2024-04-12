package com.fido

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.fido.permission.PermissionCheckClassVisitorFactory
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:
 */
class PermissionCheckPlugin:Plugin<Project> {
    override fun apply(target: Project) {
        // 判断是否有 com.android.application 插件来判断该 module 是否是一个 Android App 的 moudle，我们只处理 Android APP
        val appPlugin = try {
            target.plugins.getPlugin("com.android.application")
        }catch (e:Throwable){
            null
        }
        if (appPlugin != null) {
            val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                Log.d(msg = "PermissionCheckPlugin ===> ${variant.name}")
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
                variant.instrumentation.transformClassesWith(PermissionCheckClassVisitorFactory::class.java,InstrumentationScope.ALL){

                }
            }
        }
    }
}