package com.fido.plugin.custommethod

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.HookClassConfig
import com.fido.constant.PluginConstant
import com.fido.utils.ClInitMethodName
import com.fido.utils.InitMethodName
import com.fido.utils.LogPrint
import com.fido.utils.accessIsStaticField
import com.fido.utils.matches
import com.fido.utils.replaceDotBySlash
import com.fido.utils.replaceListWithSymbol
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
internal interface HookClassInstrumentationParameters : InstrumentationParameters {
    @get:Input
    val config: Property<HookClassConfig>
}

/**
 * 插件配置的 className数组
 */
internal val List<String>.configClzNames
    get() = replaceListWithSymbol(this)

abstract class HookClassVisitorFactory :
    AsmClassVisitorFactory<HookClassInstrumentationParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return HookClassClassVisitor(config, nextClassVisitor, classContext)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        val clzList = config.className.configClzNames
        return classData.matches(rules = clzList)
    }

}

private class HookClassClassVisitor(
    val config: HookClassConfig,
    val nextClassVisitor: ClassVisitor,
    val classContext: ClassContext,
) : ClassNode(Opcodes.ASM9) {

    internal data class FieldInfoData(
        val access: Int,
        val name: String?,
        val descriptor: String?,
        val signature: String?,
        val value: Any?,
        val owner: String
    )

    private val shouldHookFields = mutableListOf<FieldInfoData>()

    private var mOwner: String? = ""

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        mOwner = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        val currentClz = config.className.configClzNames.find { replaceDotBySlash(it) == mOwner }
        if (!currentClz.isNullOrEmpty()) {
            val parameterNames = config.parameterName
            val targetFieldName = parameterNames.find { it == name }
            if (!targetFieldName.isNullOrEmpty()) {
                shouldHookFields.add(
                    FieldInfoData(
                        access,
                        targetFieldName,
                        descriptor,
                        signature,
                        value,
                        replaceDotBySlash(currentClz)
                    )
                )
            }
        }

//        LogPrint.normal(PluginConstant.HOOK_CLASS_TAG){
//            """
//                ============ visitField ===============
//                mOwner=${mOwner}
//                name = $name
//                shouldHookFields = $shouldHookFields
//            """.trimIndent()
//        }

        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)

        val clzList = config.className.configClzNames
        val currentClz = clzList.find { replaceDotBySlash(it) == mOwner }

        if (clzList.isNotEmpty() && !currentClz.isNullOrEmpty()) {
            val parameterNames = config.parameterName
            val parameterNewValues = config.parameterNewValue

            return when (name) {
                InitMethodName -> {
                    InitMethodVisitor(
                        vm,
                        parameterNames,
                        parameterNewValues,
                        currentClz,
                        shouldHookFields.filterNot { it.name.isNullOrEmpty() })
                }

                ClInitMethodName -> {
                    ClInitMethodVisitor(
                        vm,
                        parameterNames,
                        parameterNewValues,
                        mOwner,
                        shouldHookFields.filterNot { it.name.isNullOrEmpty() }.toMutableList()
                    )
                }

                else -> vm
            }
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

    private class InitMethodVisitor(
        methodVisitor: MethodVisitor?,
        val parameterNames: List<String>,
        val parameterNewValues: List<Any>,
        val mOwner: String,
        val shouldHookFields: List<FieldInfoData>?
    ) :
        MethodVisitor(Opcodes.ASM9, methodVisitor) {

        override fun visitFieldInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?
        ) {
            super.visitFieldInsn(opcode, owner, name, descriptor)

//            LogPrint.normal(PluginConstant.HOOK_CLASS_TAG) {
//                """ ============== InitMethodVisitor visitFieldInsn ==================
//                                    opcode=${opcode}
//                                    owner=${owner}
//                                    name=${name}
//                                    shouldHookFields=${shouldHookFields}
//                                """.trimIndent()
//            }

            if (!shouldHookFields.isNullOrEmpty()) {
                shouldHookFields.forEach { fieldInfoData ->
                    if (fieldInfoData.owner == owner &&
                        name == fieldInfoData.name &&
                        parameterNames.contains(fieldInfoData.name))
                    {
                        //这里获取 需要修改字段数组中对应字段的下标，所以parameterName 与 parameterNewValue 的长度需要对应(默认用,分割)
                        val newValueIndex = parameterNames.indexOf(fieldInfoData.name)
                        //赋值成员字段
                        mv.visitVarInsn(Opcodes.ALOAD, 0)
                        mv.visitLdcInsn(parameterNewValues[newValueIndex]) //通过LDC指令将常量 压入栈顶
                        mv.visitFieldInsn(
                            Opcodes.PUTFIELD,
                            owner,
                            fieldInfoData.name,
                            fieldInfoData.descriptor
                        )
                        LogPrint.normal(PluginConstant.HOOK_CLASS_TAG) {
                            """
                               ============visitFieldInsn==============
                               ${owner} 已修改常量${fieldInfoData.name}
                               newValue = ${parameterNewValues[newValueIndex]}
                               mOwner = ${mOwner}
                            """.trimIndent()
                        }
                    }
                }
            }
        }
    }

    private class ClInitMethodVisitor(
        methodVisitor: MethodVisitor?,
        val parameterNames: List<String>,
        val parameterNewValues: List<Any>,
        val mOwner: String?,
        val shouldHookFields: MutableList<FieldInfoData>?
    ) :
        MethodVisitor(Opcodes.ASM9, methodVisitor) {

        override fun visitInsn(opcode: Int) {

            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN || opcode == Opcodes.ATHROW) {

                val staticFields = shouldHookFields?.filter {
                    it.access.accessIsStaticField && parameterNames.contains(it.name)
                } ?: emptyList()

//                LogPrint.normal(PluginConstant.HOOK_CLASS_TAG) {
//                    """ ============== ClInitMethodVisitor Return之前 ==================
//                                    opcode=${opcode}
//                                    shouldHookFields=${shouldHookFields}
//                                    staticFields=${staticFields}
//                                """.trimIndent()
//                }

                if (staticFields.isNotEmpty()) {
                    staticFields.forEach { fieldInfoData ->
                        if (!parameterNames.find { it == fieldInfoData.name }.isNullOrEmpty()) {
                            val newValueIndex = parameterNames.indexOf(fieldInfoData.name)
                            //赋值静态字段
                            val realValue = parameterNewValues[newValueIndex].toRealTypeValue(fieldInfoData.descriptor?:"")
                            mv.visitLdcInsn(realValue) //通过LDC指令将常量值 压入栈顶
                            mv.visitFieldInsn(
                                Opcodes.PUTSTATIC,
                                mOwner,
                                fieldInfoData.name,
                                fieldInfoData.descriptor
                            )

                            shouldHookFields?.removeAt(shouldHookFields.indexOf(fieldInfoData))

                            LogPrint.normal(PluginConstant.HOOK_CLASS_TAG) {
                                """ ============== ClInitMethodVisitor ==================
                                    shouldHookFields=${shouldHookFields}
                                    staticFields=${staticFields}
                                    已修改静态变量 name=${fieldInfoData.name} descriptor=${fieldInfoData.descriptor} 
                                    newValue = ${parameterNewValues[newValueIndex]}
                                    mOwner =${mOwner}
                                """.trimIndent()
                            }
                        }
                    }
                }
            }
            mv.visitInsn(opcode)
        }
    }

}

internal fun Any.toRealTypeValue(descriptor:String):Any?{
    return when (descriptor) {
        "D" -> this.toString().toDouble()
        "F" -> this.toString().toFloat()
        "J" -> this.toString().toLong()
        else -> this
    }
}

