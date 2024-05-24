package com.fido.plugin.method_replace

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.replace_method.ReplaceMethodConfig
import com.fido.constant.PluginConstant
import com.fido.utils.LogPrint
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:
 */
internal interface ReplaceMethodConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<ReplaceMethodConfig>
}

abstract class ReplaceMethodClassVisitorFactory :
    AsmClassVisitorFactory<ReplaceMethodConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ReplaceMethodClassVisitor(nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return parameters.get().config.get().enable
    }

}

public class ReplaceMethodClassVisitor(val nextClassVisitor: ClassVisitor) :
    ClassNode(Opcodes.ASM9) {

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

    override fun visitEnd() {
        super.visitEnd()

        hook()

        accept(nextClassVisitor)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        if (mOwner?.contains("DeviceInfoAc") == true) {

            LogPrint.normal("DeviceInfo visitMethod") {
                """
                    =======visitMethod=============
                    desc=${descriptor}
                    name=${name}
                    opcode=${access}
                    asmConfigs=${asmConfigs}
                """.trimIndent()
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    private fun hook() {

        if (asmConfigs.isNotEmpty()) {
            methods.forEach { methodNode ->
                //修改方法
                hookMethodNode(methodNode)
            }
        } else {
            // 未收集到替换方法的注解信息,将methods先缓存下来，等全部收集完成后，遍历一遍
            // PS：如果注解类是最后访问的呢？会不会收集不到？
//            if (!laterReplaceMethods.contains(methods)) {
//                laterReplaceMethods.add(methods)
//            }
//            LogPrint.normal("Empty"){
//                "asmConfigs.empty ===> mOwner=${mOwner} replaceMethodAnnotationIsCollected=${replaceMethodAnnotationIsCollected} laterReplaceMethods.size=${laterReplaceMethods.size}"
//            }
            /*if (replaceMethodAnnotationIsCollected) {
                if (laterReplaceMethods.isNotEmpty()) {
                    //开始hook 是否有需要修改的方法
                    laterReplaceMethods.forEachIndexed { index, methodNodes ->
                        methodNodes.forEach {methodNode ->
                            hookMethodNode(methodNode){node->
                                runCatching {
                                    if (node == methodNode) {
                                        println("find remove node remove前 原size = ${laterReplaceMethods.size}")
                                        laterReplaceMethods.removeAt(index)
                                        println("find remove node remove后 size = ${laterReplaceMethods.size}")
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
        }
    }

    private fun hookMethodNode(methodNode: MethodNode?) {
        methodNode?.instructions?.forEach { insnNode ->
            if (insnNode is MethodInsnNode) {
                asmConfigs.forEach { asmItem ->
                    //INVOKEVIRTUAL android/app/ActivityManager.getRunningAppProcesses ()Ljava/util/List; ->
                    //INVOKESTATIC  com/lanshifu/asm_plugin_library/privacy/PrivacyUtil.getRunningAppProcesses (Landroid/app/ActivityManager;)Ljava/util/List;

                    if (asmItem.oriDesc == insnNode.desc && asmItem.oriMethod == insnNode.name
                        && insnNode.opcode == asmItem.oriAccess &&
                        (insnNode.owner == asmItem.oriClass || asmItem.oriClass == "java/lang/Object")
                    ) {
                        LogPrint.normal(PluginConstant.REPLACE_METHOD_TAG) {
                            "\nhook:\n" +
                                    "asmConfigs=${asmConfigs}"
                            "opcode=${insnNode.opcode},owner=${insnNode.owner},desc=${insnNode.desc},name=${mOwner}#${insnNode.name} ->\n" +
                                    "opcode=${asmItem.targetAccess},owner=${asmItem.targetClass},desc=${asmItem.targetDesc},name=${asmItem.targetMethod}\n"
                        }

                        insnNode.opcode = asmItem.targetAccess
                        insnNode.desc = asmItem.targetDesc
                        insnNode.owner = asmItem.targetClass
                        insnNode.name = asmItem.targetMethod
                    }
                }
            }
        }
    }
}
