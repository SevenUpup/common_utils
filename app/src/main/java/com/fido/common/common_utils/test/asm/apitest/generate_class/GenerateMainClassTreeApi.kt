package com.fido.common.common_utils.test.asm.apitest.generate_class

import com.fido.common.common_utils.test.asm.common.getRootPath
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

/**
 * @author: FiDo
 * @date: 2024/4/29
 * @des:
 */
fun main(){

    generateMainClzTreeApi()

}

fun generateMainClzTreeApi() {

    val packageName = "com.fido.common.common_utils.test.asm.apitest.generate_class".replace(".", "/")
    val clzName = packageName + "/GenerateMainByTreeApi"

    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    val classNode = ClassNode(Opcodes.ASM9)

    classNode.apply {
        version = Opcodes.V11
        name = clzName
        superName = "java/lang/Object"
        access = Opcodes.ACC_PUBLIC

        // 生成 [private static final String TAG = "Main";]
        fields.add(
            FieldNode(
                Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
                "TAG",
                Type.getDescriptor(String::class.java),
                null,
                "Main"
            )
        )
        //生成构造方法
        methods.add(MethodNode(
            Opcodes.ACC_PUBLIC,
            "<init>",
            "()V",
            null,
            null
        ).apply {
            instructions.add(
                InsnList().apply {
                    add(VarInsnNode(Opcodes.ALOAD,0))
                    add(MethodInsnNode(Opcodes.INVOKESPECIAL,"java/lang/Object","<init>","()V",false))
                    add(InsnNode(Opcodes.RETURN))
                }
            )
            visitMaxs(1,1)
        })
        //生成 main 方法 [public static void main(String[] args) {}]
        methods.add(
            MethodNode(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "main",
                "([Ljava/lang/String;)V",
                null,
                null
            ).apply {
                instructions.apply {
                    add(FieldInsnNode(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;"))
                    add(LdcInsnNode("Hello World!"))
                    add(MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V",false))
                    add(InsnNode(Opcodes.RETURN))
                }
                visitMaxs(1,1)
            }
        )
    }

    classNode.accept(classWriter)

    classWriter.toByteArray().apply {
        val prefixPath =
            "$getRootPath/app/src/main/java/"
//        toFile("${prefixPath}$clzName.class")
        toFile("${getRootPath}/GenerateMainByTreeApi.class")
        loadClass(clzName.replace("/","."))?.apply {
            declaredFields.forEach {
                it.isAccessible = true
                println("field : ${it.name} ${it.get(null)}")
            }
            getMethod("main",Array<String>::class.java).invoke(null,null)
        }
    }
}
