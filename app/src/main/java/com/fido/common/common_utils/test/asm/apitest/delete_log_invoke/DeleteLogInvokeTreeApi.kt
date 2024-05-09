package com.fido.common.common_utils.test.asm.apitest.delete_log_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

fun main() {
    deleteLogInvokeByTreeApi()
}

/**
 * 使用 Tree Api 删除 sout 日志语句
 *
 * 思路: 删除 GETSTATIC out 和 INVOKEVIRTUAL print 之间的指令
 */
private const val clzName = "fido66/DeleteLogInvokeTreeClass"
private fun deleteLogInvokeByTreeApi() {
    val classReader = ClassReader(DeleteLogInvoke::class.qualifiedName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)

    classNode.name = clzName
    classNode.methods.forEach { methodNode ->
        val deleteNodes = mutableListOf<AbstractInsnNode>()
        methodNode.instructions.forEachIndexed { index, node ->
            if (node is MethodInsnNode && node.opcode == Opcodes.INVOKEVIRTUAL
                && node.name.startsWith("print")
                && node.owner == "java/io/PrintStream"
            ) {
                // 往前找到 getStatic
                var startIndex = index - 1
                while (!methodNode.instructions[startIndex].let {
                        it is FieldInsnNode && it.opcode == Opcodes.GETSTATIC
                                && it.name == "out" && it.desc == "Ljava/io/PrintStream;"
                                && it.owner == "java/lang/System"
                    }) {
                    startIndex--
                }
                var endIndex = index
                if (Type.getMethodType(node.desc).returnType != Type.VOID_TYPE
                    && node.next != null && node.next.opcode == Opcodes.POP
                ) {
                    // 如何方法的返回值不是 Void，还要删除后面的 POP 指令
                    endIndex++
                }
                (startIndex..endIndex).forEach {
                    deleteNodes.add(methodNode.instructions[it])
                }
            }
        }

        deleteNodes.forEach {
            methodNode.instructions.remove(it)
        }

    }

    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    classNode.accept(classWriter)
    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR+"/DeleteLogInvokeTreeClass.class")
        loadClass(clzName.replaceSlash2Dot)?.apply {
            val printMethod = getMethod("print", String::class.java, Int::class.java)
            printMethod.invoke(getConstructor().newInstance(), "Omooo", 25)
        }
    }
}
