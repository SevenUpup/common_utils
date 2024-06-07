package com.fido.plugin

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.api.variant.Variant
import com.fido.plugin.click.ClickViewClassVisitorFactory
import com.fido.config.ViewClickPluginParameter
import com.fido.config.CustomMethodPluginParameter
import com.fido.config.HookClassConfig
import com.fido.config.HookClassParameter
import com.fido.config.HookCustomMethodConfig
import com.fido.config.ReplaceClassConfig
import com.fido.config.ReplaceClassPluginParameter
import com.fido.config.ToastConfig
import com.fido.config.ToastPluginParameter
import com.fido.config.ViewClickConfig
import com.fido.config.replace_method.ReplaceMethodConfig
import com.fido.config.replace_method.ReplaceMethodPluginParameters
import com.fido.constant.PluginConstant
import com.fido.plugin.custommethod.HookClassVisitorFactory
import com.fido.plugin.custommethod.HookCustomMethodCVF
import com.fido.plugin.method_replace.CollectReplaceMethodAnnotationCVF
import com.fido.plugin.method_replace.ReplaceMethodClassVisitorFactory
import com.fido.plugin.replace.ReplaceClassClassVisitorFactory
import com.fido.plugin.task.ModifyClassesTask
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
            target.extensions.create(
                HookClassParameter::class.java.simpleName,
                HookClassParameter::class.java
            )
            target.extensions.create(
                ReplaceMethodPluginParameters::class.java.simpleName,
                ReplaceMethodPluginParameters::class.java
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
                //替换类中方法或属性
                handleHookClassPlugin(target,variant)
                //收集需要通过注解替换方法的一些配置信息,需要与ReplaceMethodClassVisitorFactory一起使用，因为要保证先收集，再修改方法(因为我不知道怎么便收集便修改，不能保证每次字节码修改的顺序，可能没收集到就先到了修改的字节码文件了)
                handleCollectReplaceMethodPlugin(target,variant)
                //替换方法，主要用于同意隐私协议前调用系统方法，目前改为ModifyClassesTask实现
//                handleReplaceMethodPlugin(target,variant)
                variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)

//                val asmReplaceMethodClassList = target.properties[PluginConstant.FIDO_ASM_REPLACE_METHOD_CLASS]
                val replaceMethodTaskEnable = target.properties[PluginConstant.FIDO_ASM_REPLACE_METHOD_ENABLE] as? Boolean ?: false
                if (replaceMethodTaskEnable) {
                    val taskProvider = target.tasks.register("${variant.name}${ModifyClassesTask::class.java.simpleName}",ModifyClassesTask::class.java)
                    variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                        .use(taskProvider)
                        .toTransform(
                            ScopedArtifact.CLASSES,
                            ModifyClassesTask::allJars,
                            ModifyClassesTask::allDirectories,
                            ModifyClassesTask::output,
                        )
                }
            }
        }

        /*with(target) {
            plugins.withType(AppPlugin::class.java){
                val androidComponents = extensions.findByType(AndroidComponentsExtension::class.java)
                androidComponents?.onVariants { variant ->
                    val taskProvider = tasks.register("${variant.name}RouterClassesTask",RouterClassesTask::class.java)
                    variant.artifacts.forScope(ScopedArtifacts.Scope.ALL)
                        .use(taskProvider)
                        .toTransform(
                            ScopedArtifact.CLASSES,
                            RouterClassesTask::jars,
                            RouterClassesTask::dirs,
                            RouterClassesTask::output,
                        )
                }
            }
        }*/

    }

    private fun handleCollectReplaceMethodPlugin(target: Project, variant: Variant) {
        val parameters = target.extensions.findByType(ReplaceMethodPluginParameters::class.java)
        val collectReplaceMethodAnnotationConfig = if (parameters == null){
            null
        }else{
            ReplaceMethodConfig(parameters)
        }
        if (collectReplaceMethodAnnotationConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(CollectReplaceMethodAnnotationCVF::class.java,InstrumentationScope.ALL){replaceMethodConfigParameters ->
                    replaceMethodConfigParameters.config.set(collectReplaceMethodAnnotationConfig)
                }
            }
        }
    }

    private fun handleReplaceMethodPlugin(target: Project, variant: Variant) {
        val parameters = target.extensions.findByType(ReplaceMethodPluginParameters::class.java)
        val replaceMethodConfig = if (parameters == null) {
            null
        }else{
            ReplaceMethodConfig(parameters)
        }
        if (replaceMethodConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(ReplaceMethodClassVisitorFactory::class.java,InstrumentationScope.ALL){replaceMethodConfigParameters ->
                    replaceMethodConfigParameters.config.set(replaceMethodConfig)
                }
            }
        }
    }

    private fun handleHookClassPlugin(target: Project, variant: Variant) {
        val hookClassParameter = target.extensions.findByType(HookClassParameter::class.java)
        val hookClassConfig = if (hookClassParameter == null){
            null
        }else{
            HookClassConfig(hookClassParameter)
        }
        if (hookClassConfig != null) {
            variant.instrumentation.apply {
                transformClassesWith(HookClassVisitorFactory::class.java,InstrumentationScope.ALL){ hookClassInstrumentationParameters ->
                    hookClassInstrumentationParameters.config.set(hookClassConfig)
                }
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