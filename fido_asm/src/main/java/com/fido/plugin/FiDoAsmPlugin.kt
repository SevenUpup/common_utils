package com.fido.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.fido.Log
import com.fido.plugin.click.ClickViewClassVisitorFactory
import com.fido.plugin.config.ViewClickPluginParameter
import com.fido.plugin.config.CustomMethodPluginParameter
import com.fido.plugin.config.HookCustomMethodConfig
import com.fido.plugin.config.ViewClickConfig
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
            target.extensions.create(
                ViewClickPluginParameter::class.java.simpleName,
                ViewClickPluginParameter::class.java
            )
            val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant: Variant ->
                handleCustomMethod(project = target, variant = variant)
                handleViewClickPlugin(target,variant)
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
            }
        }
    }

    private fun handleViewClickPlugin(target: Project, variant: Variant) {
        val viewClickParameter = target.extensions.findByType(ViewClickPluginParameter::class.java)
        val viewClickConfig = if (viewClickParameter == null) null else ViewClickConfig(parameter = viewClickParameter)
        if (viewClickConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(ClickViewClassVisitorFactory::class.java,InstrumentationScope.ALL){clickViewConfigParameter ->
                    clickViewConfigParameter.config.set(viewClickConfig)
                }
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