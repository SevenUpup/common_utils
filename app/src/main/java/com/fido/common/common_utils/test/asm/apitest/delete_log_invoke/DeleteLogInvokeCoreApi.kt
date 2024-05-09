package com.fido.common.common_utils.test.asm.apitest.delete_log_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
fun main() {
    deleteLogInvokeCoreApi()
}

private const val clzName = "fido66/DeleteLogCoreClass"
private fun deleteLogInvokeCoreApi() {
    val classReader = ClassReader(DeleteLogInvoke::class.java.canonicalName)

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
            return DeleteMethodVisitor(methodVisitor)
        }

    }, ClassReader.SKIP_DEBUG)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/DeleteLogCoreClass.class")
        loadClass(clzName.replaceSlash2Dot)?.apply {
            getMethod(
                "print",
                String::class.java,
                Int::class.java
            ).invoke(getConstructor().newInstance(), "FiDo", 66)
        }
    }

}

private class DeleteMethodVisitor(methodVisitor: MethodVisitor) : MethodVisitor(Opcodes.ASM9, methodVisitor) {
    /** 删除期间 */
    var deleteDuration = false

    /** 删除 pop 指令 */
    var deletePopInsn = false
    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        if (opcode == Opcodes.GETSTATIC && owner == "java/lang/System" && name == "out" && descriptor == "Ljava/io/PrintStream;") {
            deleteDuration = true
            return
        }
        deleteDuration = false
        super.visitFieldInsn(opcode, owner, name, descriptor)
    }

    override fun visitLdcInsn(value: Any?) {
        if (deleteDuration) {
            return
        }
        super.visitLdcInsn(value)
    }

    override fun visitInsn(opcode: Int) {
        if (deleteDuration) {
            return
        }
        if (deletePopInsn) {
            deletePopInsn = false
            return
        }
        super.visitInsn(opcode)
    }

    override fun visitVarInsn(opcode: Int, varIndex: Int) {
        if (deleteDuration) {
            return
        }
        super.visitVarInsn(opcode, varIndex)
    }

    override fun visitTypeInsn(opcode: Int, type: String?) {
        if (deleteDuration) {
            return
        }
        super.visitTypeInsn(opcode, type)
    }

    override fun visitInvokeDynamicInsn(
        name: String?,
        descriptor: String?,
        bootstrapMethodHandle: Handle?,
        vararg bootstrapMethodArguments: Any?
    ) {
        if (deleteDuration) {
            return
        }
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String,
        descriptor: String?,
        isInterface: Boolean
    ) {
        if (opcode == Opcodes.INVOKEVIRTUAL
            && owner == "java/io/PrintStream"
            && name.startsWith("print")
        ) {
            deletePopInsn = Type.getMethodType(descriptor).returnType != Type.VOID_TYPE
            deleteDuration = false
            return
        }
        if (deleteDuration) {
            return
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

}


val String.replaceSlash2Dot
    get() = replace("/", ".")

val String.replaceDot2Slash
    get() = replace(".", "/")