package com.fido.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.fido.Log
import com.fido.plugin.config.CustomMethodPluginParameter
import com.fido.plugin.config.HookCustomMethodConfig
import com.fido.plugin.custommethod.HookCustomMethodCVF
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author: FiDo
 * @date: 2024/4/15
 * @des:
 */
class FiDoAsmPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // 判断是否有 com.android.application 插件来判断该 module 是否是一个 Android App 的 moudle，我们只处理 Android APP
        val appPlugin = try {
            target.plugins.getPlugin("com.android.application")
        }catch (e:Throwable){
            null
        }
        if (appPlugin != null) {
            Log.d(msg = "find android app plugin")
            target.extensions.create(
                "hookCustomMethod",
                CustomMethodPluginParameter::class.java
            )

            val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant: Variant ->
                handleCustomMethod(project = target, variant = variant)
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
            }
        }
    }

    private fun handleCustomMethod(project: Project, variant: Variant) {
        val pluginHookCustomMethod =
            project.extensions.findByType(CustomMethodPluginParameter::class.java)
        val hookCustomMethodConfig = if (pluginHookCustomMethod == null) {
            null
        } else {
            HookCustomMethodConfig(parameter = pluginHookCustomMethod)
        }
        if (hookCustomMethodConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(
                    HookCustomMethodCVF::class.java,
                    InstrumentationScope.ALL
                ) { hookCustomClzParameter ->
                    hookCustomClzParameter.config.set(hookCustomMethodConfig)
                }
            }
        }
    }


}