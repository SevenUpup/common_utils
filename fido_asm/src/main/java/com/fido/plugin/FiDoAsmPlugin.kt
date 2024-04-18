package com.fido.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.fido.utils.Log
import com.fido.plugin.click.ClickViewClassVisitorFactory
import com.fido.config.ViewClickPluginParameter
import com.fido.config.CustomMethodPluginParameter
import com.fido.config.HookCustomMethodConfig
import com.fido.config.ReplaceClassConfig
import com.fido.config.ReplaceClassPluginParameter
import com.fido.config.ToastConfig
import com.fido.config.ToastPluginParameter
import com.fido.config.ViewClickConfig
import com.fido.plugin.custommethod.HookCustomMethodCVF
import com.fido.plugin.replace.ReplaceClassClassVisitorFactory
import com.fido.plugin.toast.ToastClassVisitorFactory
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
            target.extensions.create(
                ReplaceClassPluginParameter::class.java.simpleName,
                ReplaceClassPluginParameter::class.java
            )
            target.extensions.create(
                ToastPluginParameter::class.java.simpleName,
                ToastPluginParameter::class.java
            )

            val androidComponents = target.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant: Variant ->
                //hook指定类名&方法 为 目标方法
                handleCustomMethod(project = target, variant = variant)
                //处理全局点击
                handleViewClickPlugin(target,variant)
                //替换类名
                handleReplaceClassPlugin(target,variant)
                //替换系统toast,可以解决7.0系统toast
                handleToastPlugin(target,variant)
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)
            }
        }
    }

    private fun handleToastPlugin(target: Project, variant: Variant) {
        val parameter = target.extensions.findByType(ToastPluginParameter::class.java)
        val toastPluginConfig = if (parameter == null){
            null
        }else{
            ToastConfig(parameter)
        }
        if (toastPluginConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(ToastClassVisitorFactory::class.java,InstrumentationScope.ALL){toastConfigParameter ->
                    toastConfigParameter.config.set(toastPluginConfig)
                }
            }
        }
    }

    private fun handleReplaceClassPlugin(target: Project, variant: Variant) {
        val pluginParameter = target.extensions.findByType(ReplaceClassPluginParameter::class.java)
        val replaceClassPluginConfig = if (pluginParameter == null){
            null
        }else{
            ReplaceClassConfig(parameter = pluginParameter)
        }
        if (replaceClassPluginConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(ReplaceClassClassVisitorFactory::class.java,InstrumentationScope.ALL){replaceClassConfigParameters ->
                    replaceClassConfigParameters.config.set(replaceClassPluginConfig)
                }
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