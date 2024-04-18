package com.fido.plugin.replace

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.ReplaceClassConfig
import com.fido.constant.PluginConstant
import com.fido.utils.LogPrint
import com.fido.utils.matches
import com.fido.utils.replaceDotBySlash
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:  将目标类替换为指定类
 */

internal interface ReplaceClassConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<ReplaceClassConfig>
}

abstract class ReplaceClassClassVisitorFactory :
    AsmClassVisitorFactory<ReplaceClassConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return ReplaceClassClassVisitor(config, nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        if (classData.className == config.targetClass || classData.superClasses.first() != config.originClass) {
            return false
        }
        val include = config.include
        val exclude = config.exclude
        if (include.isEmpty()) {
            return !classData.matches(rules = exclude)
        }
        return classData.matches(rules = include) && !classData.matches(rules = exclude)
    }
}

private class ReplaceClassClassVisitor(
    val config: ReplaceClassConfig,
    val nextClassVisitor: ClassVisitor
) : ClassNode(Opcodes.ASM9) {

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, replaceDotBySlash(className = config.targetClass), interfaces)
        LogPrint.normal(tag = PluginConstant.REPLACE_TAG) {
            "$name 的父类符合 ReplaceClass 规则，完成处理... targetClass = ${config.targetClass}"
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        accept(nextClassVisitor)
    }

}