package com.fido.plugin.custommethod

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.plugin.config.HookCustomMethodConfig
import com.fido.plugin.constant.PluginConstant
import com.fido.plugin.constant.PluginConstant.CUSTOM_METHOD_TAG
import com.fido.plugin.constant.hookMethodMode
import com.fido.plugin.utils.LogPrint
import com.fido.plugin.utils.replaceDotBySlash
import com.fido.plugin.utils.simpleClassName
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
}

internal abstract class HookCustomMethodCVF : AsmClassVisitorFactory<HookCustomClzParameter> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return HookCustomMethodClassVisitor(
            classContext = classContext,
            nextClassVisitor = nextClassVisitor,
            config = config
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
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
        //删除
        if (config.handleMode.hookMethodMode() == PluginConstant.DELETE && config.originalMethodName == name) {
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
        val config: HookCustomMethodConfig,
    ) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

        override fun onMethodEnter() {
            super.onMethodEnter()
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
            if (config.handleMode.hookMethodMode() == PluginConstant.REPLACE && owner?.contains("ASMTestAc") == true) {
                LogPrint.normal("FiDo"){
                    """
                        opcodeAndSource =${opcodeAndSource}
                        owner =${owner}
                        name =${name}
                        descriptor =${descriptor}
                        config.originalClassName=${config.originalClassName}
                        config.originalMethodName=${config.originalMethodName}
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