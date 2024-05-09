package com.fido.common.common_utils.test.asm.apitest.catch_method_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.apitest.delete_log_invoke.replaceSlash2Dot
import com.fido.common.common_utils.test.asm.common.const0Opcode
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import java.lang.reflect.Modifier

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:
 */

fun main() {
    catchMethodInvokeCoreApi()
}

private const val clzName = "fido66/CatchMethodCoreApiClass"

private fun catchMethodInvokeCoreApi() {
    val classReader = ClassReader(CatchMethodInvoke::class.java.canonicalName)
    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)

    classReader.accept(object : ClassVisitor(Opcodes.ASM9, classWriter) {
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            super.visit(version, access, clzName, signature, superName, interfaces)
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
            val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (name == "<init>" || Modifier.isNative(access) || Modifier.isAbstract(access)) {
                return methodVisitor
            }
            return InternalMethodVosotor(methodVisitor, access, name, descriptor)
        }
    }, ClassReader.SKIP_DEBUG)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/CatchMethodCoreApiClass.class")
        loadClass(clzName.replaceSlash2Dot)?.apply {
            val returnValue = getMethod("calc").invoke(getConstructor().newInstance())
            println(returnValue)
        }
    }

}

class InternalMethodVosotor(
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?
) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

    private val fromLabel = Label()
    private val toLabel = Label()
    private val targetLabel = Label()

    override fun onMethodEnter() {
        super.onMethodEnter()
        mv.visitTryCatchBlock(fromLabel, toLabel, targetLabel, "java/lang/Exception")
        mv.visitLabel(fromLabel)
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        mv.visitLabel(toLabel)
        mv.visitLabel(targetLabel)
        val varIndex = newLocal(Type.getType("Ljava/lang/Exception"))
        mv.visitVarInsn(Opcodes.ASTORE, varIndex)
        mv.visitVarInsn(Opcodes.ALOAD, varIndex)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/Exception",
            "printStackTrace",
            "()V",
            false
        )

        Type.getMethodType(methodDesc).returnType.apply {
            if (sort == Type.VOID) {
                mv.visitInsn(Opcodes.RETURN)
            } else {
                // 异常时返回默认值
                mv.visitInsn(this.const0Opcode())
                mv.visitInsn(this.getOpcode(IRETURN))
            }
        }
        super.visitMaxs(maxStack, maxLocals)
    }

}
