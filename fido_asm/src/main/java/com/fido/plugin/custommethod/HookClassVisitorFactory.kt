package com.fido.plugin.custommethod

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.HookClassConfig
import com.fido.constant.PluginConstant
import com.fido.utils.LogPrint
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

/**
 * @author: FiDo
 * @date: 2024/4/19
 * @des:  修改指定类名的参数或方法
 */
internal interface HookClassInstrumentationParameters:InstrumentationParameters{
    @get:Input
    val config:Property<HookClassConfig>
}
abstract class HookClassVisitorFactory :AsmClassVisitorFactory<HookClassInstrumentationParameters>{

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return HookClassClassVisitor(config, nextClassVisitor,classContext)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        return classData.className == config.className
    }

}

private class HookClassClassVisitor(
    val config: HookClassConfig,
    val nextClassVisitor: ClassVisitor,
    val classContext: ClassContext,
):ClassNode(Opcodes.ASM9){

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        LogPrint.normal(PluginConstant.HOOK_CLASS_TAG){
            """
                ======visitField======
                className=${classContext.currentClassData.className}
                access=${access}
                name =${name}
                descriptor=${descriptor}
                signature=${signature}
                value=${value}
            """.trimIndent()
        }
        if (classContext.currentClassData.className == config.className && name == config.parameterName) {
            return super.visitField(access, name, descriptor, signature, config.parameterNewValue)
        } else {
            return super.visitField(access, name, descriptor, signature, value)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)
        LogPrint.normal(PluginConstant.HOOK_CLASS_TAG){
            """
                ======visitMethod======
                className=${classContext.currentClassData.className}
                access=${access}
                name =${name}
                descriptor=${descriptor}
                signature=${signature}
                exceptions=${exceptions}
            """.trimIndent()
        }
        return vm
    }

    override fun visitEnd() {
        super.visitEnd()
        accept(nextClassVisitor)
        LogPrint.normal(tag = PluginConstant.HOOK_CLASS_TAG) {
            "$name 参数:${config.parameterName} 已替换为新值:${config.parameterNewValue}"
        }
    }

}