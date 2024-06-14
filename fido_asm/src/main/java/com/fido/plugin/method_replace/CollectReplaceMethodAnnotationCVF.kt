package com.fido.plugin.method_replace

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.fido.utils.matches
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode


/**
 * @author: FiDo
 * @date: 2024/5/8
 * @des:
 */
abstract class CollectReplaceMethodAnnotationCVF :
    AsmClassVisitorFactory<ReplaceMethodConfigParameters> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return InternalClassVisitor(nextClassVisitor, config.replaceClassName)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        //含 AsmMethodReplace 注解的类
        val replaceClzList = parameters.get().config.get().replaceClassName
        //全局变量存储，方便Task获取
        replaceMethodConfigClassList.addAll(replaceClzList)

        return config.enable && classData.matches(rules = replaceClzList)
    }
}

internal const val REPLACE_METHOD_ANNO_DES = "Lcom/fido/annotation/asm/AsmMethodReplace;"
internal val asmConfigs = mutableListOf<ReplaceMethodAnnotationItem>()
internal val replaceMethodConfigClassList = mutableSetOf<String>()
private class InternalClassVisitor(
    val nextClassVisitor: ClassVisitor,
    val replaceClassName: List<String>
) : ClassNode(Opcodes.ASM9) {

    private val slashReplaceClass = replaceClassName.map { it.replace(".", "/") }

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

        if (slashReplaceClass.contains(name)) {
            collectHookAnnotation()
        }

        accept(nextClassVisitor)
    }

    private fun collectHookAnnotation() {
        methods.forEach { methodNode ->
            //收集需要修改的方法
            methodNode.invisibleAnnotations?.forEach { annotationNode ->
                if (annotationNode.desc == REPLACE_METHOD_ANNO_DES) {
                    val asmItem = ReplaceMethodAnnotationItem(name, methodNode, annotationNode)
                    if (!asmConfigs.contains(asmItem)) {
                        asmConfigs.add(asmItem)
                    }
                }
            }
        }
    }
}
