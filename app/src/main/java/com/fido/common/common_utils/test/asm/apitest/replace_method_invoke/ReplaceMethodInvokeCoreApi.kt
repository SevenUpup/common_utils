package com.fido.common.common_utils.test.asm.apitest.replace_method_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
fun main(){
    replaceMethodInvokeCoreApi()
}
private val clzName = "fido66/ReplaceMethodInvokeCoreClass"
private fun replaceMethodInvokeCoreApi() {
    val classReader = ClassReader(ReplaceMethodInvoke::class.java.canonicalName)
    val classWriter = ClassWriter(classReader,ClassWriter.COMPUTE_FRAMES)

    classReader.accept(object :ClassVisitor(Opcodes.ASM9,classWriter){


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
            val vm = super.visitMethod(access, name, descriptor, signature, exceptions)
            return InterMethodVisitor(clzName,vm)
        }

    },ClassReader.SKIP_DEBUG)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/ReplaceMethodInvokeCoreClass.class")
        loadClass(clzName.replace("/","."))?.apply {
            getMethod("main",Array<String>::class.java).invoke(
                getConstructor().newInstance(),null
            )
        }
    }

}

private class InterMethodVisitor(val clzName: String, methodVisitor: MethodVisitor?) : MethodVisitor(Opcodes.ASM9,methodVisitor) {

    private val TOAST = "com.fido.common.common_utils.test.asm.apitest.replace_method_invoke.Toast".replace(".","/")
    private val SHADOW_TOAST = "com.fido.common.common_utils.test.asm.apitest.replace_method_invoke.ShadowToast".replace(".","/")
    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        if (opcode == Opcodes.INVOKEVIRTUAL && owner == TOAST && name == "show" && descriptor == "()V") {
            mv.visitLdcInsn(clzName)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                SHADOW_TOAST,
                name,
                "(L$TOAST;Ljava/lang/String;)V",
                isInterface
            )
            return
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

}
