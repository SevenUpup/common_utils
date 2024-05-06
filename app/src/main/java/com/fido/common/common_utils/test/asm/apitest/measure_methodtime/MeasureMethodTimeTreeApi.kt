package com.fido.common.common_utils.test.asm.apitest.measure_methodtime

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.isMethodReturn
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.VarInsnNode

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
fun main() {
    measureMethodTimeTreeApi()
}

private const val clzName = "fido66/MeasureMethodTimeTreeClass"
fun measureMethodTimeTreeApi() {
    val classReader = ClassReader(MeasureMethodTime::class.java.canonicalName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode, ClassReader.SKIP_FRAMES)
    classNode.apply {
        name = clzName
        methods.forEach { methodNode ->
            if (methodNode.invisibleAnnotations?.map { it.desc }
                    ?.contains(Type.getDescriptor(MeasureTime::class.java)) == true) {
                val localVariablesSize = methodNode.localVariables.size
                //在方法的第一个指令之前插入
                val firstInsnNode = methodNode.instructions.first
                methodNode.instructions.insertBefore(firstInsnNode, InsnList().apply {
                    add(
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "java/lang/System",
                            "currentTimeMillis",
                            "()J",
                            false
                        )
                    )
                    add(VarInsnNode(Opcodes.LSTORE, localVariablesSize + 1))
                })
                //在方法return 指令之前插入
                methodNode.instructions.filter { it.opcode.isMethodReturn() }
                    .forEach {
                        methodNode.instructions.insertBefore(it, InsnList().apply {
                            add(
                                MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    "java/lang/System",
                                    "currentTimeMillis",
                                    "()J"
                                )
                            )
                            // 注意，Long 是占两个局部变量槽位的，所以这里要较之前 +3，而不是 +2
                            add(VarInsnNode(Opcodes.LSTORE, localVariablesSize + 3))
                            // 获取PrintStream
                            add(FieldInsnNode(
                                Opcodes.GETSTATIC,
                                "java/lang/System",
                                "out",
                                "Ljava/io/PrintStream;"
                            ))
                            // new StringBuilder
                            add(TypeInsnNode(Opcodes.NEW, "java/lang/StringBuilder"))
                            //复制栈顶值并压入栈顶
                            add(InsnNode(Opcodes.DUP))
                            //构建一个StringBuilder
                            add(
                                MethodInsnNode(
                                    Opcodes.INVOKESPECIAL,
                                    "java/lang/StringBuilder",
                                    "<init>",
                                    "()V",
                                    false
                                )
                            )
                            //通过LDC指令将常量”used time:“压入栈顶
                            add(LdcInsnNode("used time:"))
                            //调用append方法
                            add(MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "java/lang/StringBuilder",
                                "append",
                                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                                false
                            ))
                            //endTime==>局部变量表指定索引类型为 long 型数值压入栈顶
                            add(VarInsnNode(Opcodes.LLOAD,localVariablesSize + 3))
                            add(VarInsnNode(Opcodes.LLOAD,localVariablesSize + 1))
                            //将栈顶两个long值求差后压入栈顶
                            add(InsnNode(Opcodes.LSUB))
                            //调用append方法怕拼接差值
                            add(MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "java/lang/StringBuilder",
                                "append",
                                "(J)Ljava/lang/StringBuilder;",
                                false
                            ))
//                            调用sb的toString()方法
                            add(MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "java/lang/StringBuilder",
                                "toString",
                                "()Ljava/lang/String;",
                                false
                            ))
                            //打印结果，调用println
                            add(MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "java/io/PrintStream",
                                "println",
                                "(Ljava/lang/String;)V",
                                false
                            ))
                        })

                        /*methodNode.instructions.insertBefore(it, InsnList().apply {
                            add(MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J"))
                            // 注意，Long 是占两个局部变量槽位的，所以这里要较之前 +3，而不是 +2
                            add(VarInsnNode(Opcodes.LSTORE, localVariablesSize + 3))
                            add(FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
                            add(VarInsnNode(Opcodes.LLOAD, localVariablesSize + 3))
                            add(VarInsnNode(Opcodes.LLOAD, localVariablesSize + 1))
                            add(InsnNode(Opcodes.LSUB))
                            add(MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V"))
                        })*/

                    }
            }
        }
    }
    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS)
    classNode.accept(classWriter)
    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/MeasureMethodTimeTreeClass.class")
        loadClass(clzName.replace("/", "."))?.apply {
            getMethod("measure").invoke(getConstructor().newInstance())
        }
    }
}
