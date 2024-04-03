package com.fido

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * @author: FiDo
 * @date: 2024/4/3
 * @des:
 */
class AndroidActivityClassVisitor (
    private val classContext: ClassContext,
    outputVisitor: ClassVisitor
) : ClassVisitor(Opcodes.ASM9, outputVisitor) {

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        Log.d(msg = "-----------------------------")
        Log.d(msg = "name=${name.moveAsmTypeClassNameToSourceCode()}, signature=${signature}, superName=${superName.moveAsmTypeClassNameToSourceCode()}, interfaces=${interfaces?.map { it.moveAsmTypeClassNameToSourceCode() }}")
        Log.d(msg = "Parents:")
        val parents = classContext.currentClassData.superClasses
        for (p in parents) {
            println("   $p")
        }
        Log.d(msg = "-----------------------------")
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)!!
        return AndroidActivityMethodVisitor(
            classContext = classContext,
            outputVisitor = mv,
            access = access,
            name = name!!,
            des = descriptor!!
        )
    }

    companion object {
        fun String?.moveAsmTypeClassNameToSourceCode(): String? {
            return this?.replace("/", ".")
        }
    }

}