package com.fido.plugin.custommethod

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.HookCustomMethodConfig
import com.fido.constant.PluginConstant
import com.fido.constant.PluginConstant.CUSTOM_METHOD_TAG
import com.fido.constant.hookMethodMode
import com.fido.utils.LogPrint
import com.fido.utils.replaceDotBySlash
import com.fido.utils.simpleClassName
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author: FiDo
 * @date: 2024/4/15
 * @des:
 */

internal interface HookCustomClzParameter : InstrumentationParameters {

    @get:Input
    val config: Property<HookCustomMethodConfig>

//    val config:ListProperty<HookCustomMethodConfig>
}

internal abstract class HookCustomMethodCVF : AsmClassVisitorFactory<HookCustomClzParameter> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
//        val config = parameters.get().config.get().configs
        val config = parameters.get().config.get()
        return HookCustomMethodClassVisitor(
            classContext = classContext,
            nextClassVisitor = nextClassVisitor,
            config = config
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
//        val config = parameters.get().config.get().configs
//        if (config.isEmpty() || config.any { it.originalClassName.isEmpty() }) {
//            return false
//        }
//        val filterClassName = config.filter { it.originalClassName.isNotEmpty() }.map { it.originalClassName }
//        val matches = classData.matches(rules = filterClassName)
//        LogPrint.normal(CUSTOM_METHOD_TAG){
//            """
//                filterClassName =${filterClassName}
//                className${classData.className} matches = ${matches}
//            """.trimIndent()
//        }
//        return matches

        val config = parameters.get().config.get()
        if (config.originalClassName.isEmpty()) {
            return false
        }
        return classData.className.contains(config.originalClassName.simpleClassName())
    }

}

private class HookCustomMethodClassVisitor(
    val classContext: ClassContext,
    val nextClassVisitor: ClassVisitor,
//    val config:  Set<MethodsConfig>
    val config: HookCustomMethodConfig
) :
    ClassVisitor(Opcodes.ASM9, nextClassVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
//        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
//        //是否含有需要删除的方法
//        val deleteModeList = config.filter { it.handleMode.hookMethodMode() == PluginConstant.DELETE && replaceDotBySlash(classContext.currentClassData.className) == it.originalClassName}
//        return if (deleteModeList.isEmpty()) {
//            CustomMethodMethodVisitor(mv,access,name,descriptor,classContext, config)
//        }else{
//            val deleteMethod = deleteModeList.find { it.originalMethodName == name }
//            if (deleteMethod != null) {
//                getLogInfo(deleteMethod.handleMode.hookMethodMode(),classContext.currentClassData.className,name?:"",descriptor)
//                null
//            } else {
//                CustomMethodMethodVisitor(mv,access,name,descriptor,classContext, config)
//            }
//        }
        //删除
        if (config.handleMode.hookMethodMode() == PluginConstant.DELETE &&
            replaceDotBySlash(classContext.currentClassData.className) == config.originalClassName &&
            config.originalMethodName == name)
        {
            getLogInfo(config.handleMode.hookMethodMode(),classContext.currentClassData.className,name,descriptor)
            return null
        }
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return CustomMethodMethodVisitor(mv,access,name,descriptor,classContext, config)
    }

    private class CustomMethodMethodVisitor(
        val methodVisitor: MethodVisitor?,
        access: Int,
        name: String?,
        descriptor: String?,
        val classContext: ClassContext,
//        val config: Set<MethodsConfig>,
        val config: HookCustomMethodConfig,
    ) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

        override fun onMethodEnter() {
            super.onMethodEnter()
            //在方法开始时插入自定义方法
//            val methodConfigs = config.filter { it.handleMode.hookMethodMode() == PluginConstant.ADD && replaceDotBySlash(classContext.currentClassData.className) == it.originalClassName &&
//                    name == it.originalMethodName }
//            if (methodConfigs.isNotEmpty()) {
//                val config = methodConfigs.find { it.originalMethodName == name }
//                getLogInfo(config?.handleMode?.hookMethodMode()?:"",classContext.currentClassData.className,name,methodDesc)
//                methodVisitor?.visitMethodInsn(
//                    Opcodes.INVOKESTATIC,
//                    config?.targetClassName,
//                    config?.targetMethodName,
//                    "()V",
//                    false
//                )
//            }
            if (config.handleMode.hookMethodMode() == PluginConstant.ADD && replaceDotBySlash(classContext.currentClassData.className) == config.originalClassName &&
                name == config.originalMethodName
            ) {
                getLogInfo(config.handleMode.hookMethodMode(),classContext.currentClassData.className,name,methodDesc)
                methodVisitor?.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    config.targetClassName,
                    config.targetMethodName,
                    "()V",
                    false
                )
            }
        }

        override fun onMethodExit(opcode: Int) {
            super.onMethodExit(opcode)
        }

        override fun visitMethodInsn(
            opcodeAndSource: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            //是否需要替换方法
//            LogPrint.normal(CUSTOM_METHOD_TAG){
//                """
//                    =====visitMethodInsn====
//                    owner=${owner}
//                    configs=${config.forEach(::print)}
//                """.trimIndent()
//            }
//            val methodConfigs = config.filter { it.handleMode.hookMethodMode() == PluginConstant.REPLACE && owner == it.originalClassName && name == it.originalMethodName }
//            if (methodConfigs.isNotEmpty()) {
//                val config = methodConfigs.find { it.originalMethodName == name }
//                getLogInfo(config?.handleMode?.hookMethodMode()?:"",classContext.currentClassData.className,name?:"",methodDesc)
//                super.visitMethodInsn(
//                    opcodeAndSource,
//                    config?.targetClassName,
//                    config?.targetMethodName,
//                    descriptor,
//                    isInterface
//                )
//            } else {
//                super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
//            }

            //是否需要替换方法
            if (config.handleMode.hookMethodMode() == PluginConstant.REPLACE && owner?.contains("ASMTestAc") == true) {
                LogPrint.normal("FiDo"){
                    """
                        opcodeAndSource =${opcodeAndSource}
                        owner =${owner}
                        name =${name}
                        descriptor =${descriptor}
                        config.originalClassName=${config.originalClassName}
                        config.originalMethodName=${config.originalMethodName}
                        replaceDotBySlash=${replaceDotBySlash(classContext.currentClassData.className)}
                    """.trimIndent()
                }
            }
            if (config.handleMode.hookMethodMode() == PluginConstant.REPLACE && owner == config.originalClassName && name == config.originalMethodName) {
                getLogInfo(config.handleMode.hookMethodMode(),classContext.currentClassData.className,name,methodDesc)
                super.visitMethodInsn(
                    opcodeAndSource,
                    config.targetClassName,
                    config.targetMethodName,
                    descriptor,
                    isInterface
                )
            } else {
                super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
            }

        }
    }

}

fun getLogInfo(mode:String,className:String,methodName:String,descriptor: String?){
    LogPrint.normal(CUSTOM_METHOD_TAG){
        "find hookMode ===> $mode className = ${className} methodName=${methodName} des=${descriptor}"
    }
}