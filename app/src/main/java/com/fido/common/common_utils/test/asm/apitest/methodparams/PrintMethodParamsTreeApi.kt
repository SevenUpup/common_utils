package com.fido.common.common_utils.test.asm.apitest.methodparams

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.isMethodReturn
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode
import java.lang.reflect.Modifier

/**
 * @author: FiDo
 * @date: 2024/4/30
 * @des:
 */

fun main(){
    printMethodParamsByTreeApi()
}

private const val clzName = "fido66/PrintMethodParamsTreeClass"

private fun printMethodParamsByTreeApi() {
    val classReader = ClassReader(PrintMethodParams::class.java.canonicalName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode,ClassReader.SKIP_DEBUG)
    classNode.apply {
        name = clzName
        methods.filter {
            it.name != "<init>" && !Modifier.isAbstract(it.access) && !Modifier.isNative(it.access)
        }.forEach {methodNode ->
            val methodType = Type.getMethodType(methodNode.desc)
            var slotIndex = if (Modifier.isStatic(methodNode.access)) 0 else 1
            //输出方法入参
            methodType.argumentTypes.forEach { type->
                when (type.sort) {
                    Type.INT->{
                        printInt(methodNode,slotIndex)
                    }
                    Type.getType(String::class.java).sort->{
                        printSting(methodNode,slotIndex)
                    }
                }
                slotIndex += type.size
            }
            // 给方法出参添加输出
            methodNode.instructions.filter {
                it.opcode.isMethodReturn()
            }.forEach {abstractInsnNode ->
                when (abstractInsnNode.opcode) {
                    Opcodes.IRETURN -> {
                        println("======= 出参为Int类型 =======")
                        printInt2(abstractInsnNode,methodNode)
                    }
                    Opcodes.ARETURN ->{
                        println("======= 出参为 Object类型 =======")
                        printObject2(abstractInsnNode,methodNode)
                    }
                }
            }
        }
    }

    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    classNode.accept(classWriter)
    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/PrintMethodParamsTreeClass.class")
        //反射验证
        loadClass(clzName.replace("/","."))?.apply {
            getMethod("print",String::class.java,Int::class.java).invoke(
                getConstructor().newInstance(),"LiaoLiao",12
            )
        }
    }
}

private fun printInt2(nextInsnNode: AbstractInsnNode?, methodNode: MethodNode?) {
    methodNode?.instructions?.insertBefore(nextInsnNode,InsnList().apply {
        add(InsnNode(Opcodes.DUP))
        add(FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"))
        add(InsnNode(Opcodes.SWAP))
        add(MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(I)V",false))
    })
}

private fun printObject2(nextInsnNode: AbstractInsnNode?,methodNode: MethodNode?){
    methodNode?.instructions?.insertBefore(nextInsnNode,InsnList().apply {
        add(InsnNode(Opcodes.DUP))
        add(FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"))
        add(InsnNode(Opcodes.SWAP))
        add(MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/Object;)V",false))
    })
}

private fun printSting(methodNode: MethodNode?, slotIndex: Int) {
    methodNode?.instructions?.insert(
        InsnList().apply {
            add(VarInsnNode(Opcodes.ALOAD,slotIndex))
            add(FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"))
            add(InsnNode(Opcodes.SWAP))
            add(MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V",false))
        }
    )
}

private fun printInt(methodNode: MethodNode?, slotIndex: Int) {
    methodNode?.instructions?.insert(
        InsnList().apply {
//            add(InsnNode(Opcodes.ILOAD))
            add(VarInsnNode(Opcodes.ILOAD,slotIndex))
            add(FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"))
            add(InsnNode(Opcodes.SWAP))
            add(MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(I)V",false))
        }
    )
}
