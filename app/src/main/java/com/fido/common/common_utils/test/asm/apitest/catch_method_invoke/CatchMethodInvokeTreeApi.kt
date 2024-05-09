package com.fido.common.common_utils.test.asm.apitest.catch_method_invoke

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.apitest.delete_log_invoke.replaceSlash2Dot
import com.fido.common.common_utils.test.asm.common.const0Opcode
import com.fido.common.common_utils.test.asm.common.isEmpty
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

fun main() {
    catchMethodInvokeByTreeApi()
}

// 参考自: https://github.com/s1rius/pudge/blob/d6c110f023/willfix-core/src/main/java/wtf/s1/willfix/core/visitors/HasReturnMethodTransformer.kt
/**
 * 使用 Tree Api try-catch 特定方法
 */

private const val clzName = "fido66/CatchMethodInvokeTreeClass"
fun catchMethodInvokeByTreeApi() {
    val classReader = ClassReader(CatchMethodInvoke::class.java.canonicalName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode, ClassReader.SKIP_DEBUG)
    classNode.name = clzName

    classNode.methods.filter {
        it.name != "<init>" && !it.isEmpty()
    }.forEach { methodNode ->

        val fromLabel = LabelNode()
        val toLabel = LabelNode()
        val targetLabel = LabelNode()

        val returnType = Type.getReturnType(methodNode.desc)
        val nextLocalIndex = methodNode.maxLocals

        methodNode.tryCatchBlocks.add(TryCatchBlockNode(fromLabel, toLabel, targetLabel, "java/lang/Exception"))
        // 插入 try 块
        methodNode.instructions.insertBefore(methodNode.instructions.first, InsnList().apply {
            add(InsnNode(returnType.const0Opcode()))
            add(VarInsnNode(returnType.getOpcode(Opcodes.ISTORE), nextLocalIndex))
            add(fromLabel)
        })

        // 插入 catch 块
        methodNode.instructions.add(InsnList().apply {
            add(toLabel)
            add(targetLabel)
            add(VarInsnNode(Opcodes.ASTORE, nextLocalIndex + 1))
            add(VarInsnNode(Opcodes.ALOAD, nextLocalIndex + 1))
            add(MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V"))
            add(VarInsnNode(returnType.getOpcode(Opcodes.ILOAD), nextLocalIndex))
            add(InsnNode(returnType.getOpcode(Opcodes.IRETURN)))
        })

        methodNode.instructions.filter {
            it.opcode in Opcodes.IRETURN until Opcodes.ARETURN
        }.forEach {
            methodNode.instructions.insertBefore(it, InsnList().apply {
                add(VarInsnNode(returnType.getOpcode(Opcodes.ISTORE), nextLocalIndex))
                add(VarInsnNode(returnType.getOpcode(Opcodes.ILOAD), nextLocalIndex))
            })
        }
    }

    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES)
    classNode.accept(classWriter)
    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR+"/CatchMethodInvokeTreeClass.class")
        loadClass(clzName.replaceSlash2Dot)?.apply {
            val returnValue = getMethod("calc").invoke(getConstructor().newInstance())
            println(returnValue)
        }
    }
}
