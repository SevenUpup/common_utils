package com.fido.common.common_utils.test.asm.apitest.replace_method_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
fun main(){
    replaceMethodInvokeTreeApi()
}

private const val clzName = "fido66/ReplaceMethodInvokeTreeClass"
private val TOAST = "com.fido.common.common_utils.test.asm.apitest.replace_method_invoke.Toast".replace(".","/")
private val SHADOW_TOAST = "com.fido.common.common_utils.test.asm.apitest.replace_method_invoke.ShadowToast".replace(".","/")
fun replaceMethodInvokeTreeApi() {

    val classReader = ClassReader(ReplaceMethodInvoke::class.java.canonicalName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode,ClassReader.SKIP_DEBUG)

    classNode.apply {
        name = clzName
        methods.forEach {methodNode ->
            methodNode.instructions.filterIsInstance<MethodInsnNode>().filter {
                it.opcode == Opcodes.INVOKEVIRTUAL && it.owner == TOAST && it.name == "show" && it.desc == "()V"
            }.forEach {
                methodNode.instructions.insertBefore(it,LdcInsnNode(classNode.name))
                it.opcode = Opcodes.INVOKESTATIC
                it.owner = SHADOW_TOAST
                it.name = it.name
                it.desc = "(L$TOAST;Ljava/lang/String;)V"
            }
        }
    }

    val classWriter = ClassWriter(classReader,ClassWriter.COMPUTE_FRAMES)
    classNode.accept(classWriter)
    classWriter.toByteArray()?.apply {
        toFile(ASMConstant.ASM_Class_DIR + "/ReplaceMethodInvokeTreeClass.class")
        loadClass(clzName.replace("/","."))?.apply {
            getMethod("main",Array<String>::class.java).invoke(getConstructor().newInstance(),null)
        }
    }
}
