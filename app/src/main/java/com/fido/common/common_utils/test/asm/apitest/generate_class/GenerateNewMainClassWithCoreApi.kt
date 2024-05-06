package com.fido.common.common_utils.test.asm.apitest.generate_class

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */
fun main() {
    generateNewMainClassWithCoreApi()
}

private const val clzName = "fido66/GenerateNewMainClass"

fun generateNewMainClassWithCoreApi() {
    val classReader = ClassReader(NewMain::class.java.canonicalName)
    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES)

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

            val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            if (name == "main" && descriptor == "([Ljava/lang/String;)V") {
                return InternalMethodVisitor(mv, access, name, descriptor)
            }
            if (name == "<clinit>") {
                println("执行了<clinit>")
            }
            if (name == "<init>") {
                println("执行了<init>")
                return object :MethodVisitor(Opcodes.ASM9,mv){
                    override fun visitFieldInsn(
                        opcode: Int,
                        owner: String?,
                        name: String?,
                        descriptor: String?
                    ) {
                        super.visitFieldInsn(opcode, owner, name, descriptor)
                        println("""
                            ============visitFieldInsn=============
                            opcode=${opcode}
                            owner = ${owner}
                            name = ${name}
                            des = ${descriptor}
                        """.trimIndent())
//                        if (opcode == Opcodes.GETSTATIC) {
//
//                        }
                    }
                }

            }
            return mv
        }

        override fun visitEnd() {
            super.visitEnd()
            println("visitEnd")
            //不用final修饰也行？,虽然生成的.class文件TAG2没见到赋值，但是确实有值
            //用final修饰会直接给TAG2赋值
//            val fv = cv.visitField(
//                Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "TAG2",
//                Type.getDescriptor(String::class.java), null, "HT"
//            )
            val fv = cv.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "TAG2",
                Type.getDescriptor(String::class.java), null, "HT"
            )
            fv?.visitEnd()
        }

    }, ClassReader.SKIP_DEBUG)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/GenerateNewMainClass.class")
        loadClass(clzName.replace("/", "."))?.apply {
            getMethod("main", Array<String>::class.java).invoke(
                getConstructor().newInstance(),
                null
            )
        }
    }
}

private class InternalMethodVisitor(
    mv: MethodVisitor?,
    access: Int,
    name: String,
    descriptor: String
) : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {

    override fun onMethodEnter() {
        super.onMethodEnter()
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            "java/lang/System",
            "out",
            "Ljava/io/PrintStream;"
        )
        //直接加载一个“Main”字符串到栈顶
//        mv.visitLdcInsn("Main")
        //或者加载静态常量TAG
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            clzName,
            "TAG",
            "Ljava/lang/String;"
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )

        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            "java/lang/System",
            "out",
            "Ljava/io/PrintStream;"
        )
        mv.visitFieldInsn(
            Opcodes.GETSTATIC,
            "$clzName",
            "TAG2",
            "Ljava/lang/String;"
        )
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )

    }

}
