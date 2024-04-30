package com.fido.common.common_utils.test.asm.apitest.generate_class

import com.fido.common.common_utils.test.asm.common.getRootPath
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type


/**
 * @author: FiDo
 * @date: 2024/4/29
 * @des:
 */
fun main() {
    genrateMainClassCoreApi()
}

/**
 * 使用CoreApi生成 [Main] 类
 */
private fun genrateMainClassCoreApi() {
    val packageName =
        "com.fido.common.common_utils.test.asm.apitest.generate_class".replace(".", "/")
    //ClassWriter.COMPUTE_FRAMES ASM会自动计算max stacks、max locals和stack map frames的具体值,但是不能省略visitMaxs()
    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES)
    val clzName = "${packageName}/GenerateMain"
    classWriter.apply {
        visit(Opcodes.V11, Opcodes.ACC_PUBLIC, clzName, null, "java/lang/Object", null)

        // 生成 [private static final String TAG = "Main";]
        visitField(
            Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL,
            "TAG",
            "Ljava/lang/String;",
            null,
            "Main"
        )
    }
    // 生成构造方法
    classWriter.visitMethod(
        Opcodes.ACC_PUBLIC,
        "<init>",
        "()V",
        null,
        null
    ).apply {
        visitVarInsn(Opcodes.ALOAD, 0)
        visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/Object",
            "<init>",
            "()V",
            false
        )
        visitInsn(Opcodes.RETURN)
        visitMaxs(1, 1)
        visitEnd()
    }
    // 生成 main 方法 [public static void main(String[] args) {}]
    classWriter.visitMethod(
        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
        "main",
        "([Ljava/lang/String;)V",
        null,
        null
    ).apply {
        visitCode()
        visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        visitLdcInsn("Hello World!")
        visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(${Type.getDescriptor(String::class.java)})V",
            false
        )
        visitInsn(Opcodes.RETURN)
        visitMaxs(1, 1)
        visitEnd()
    }
    classWriter.visitEnd()


    val prefixPath =
        "${getRootPath}/app/src/main/java/${packageName}/"

    //写入class
    classWriter.toByteArray().apply {
//        toFile("${prefixPath}GenerateMain.class")
        toFile("${getRootPath}/GenerateMain.class")
        loadClass(clzName.replace("/","."))?.apply {
            declaredFields.forEach {
                it.isAccessible = true
                println("field name: ${it.name}, value: ${it.get(null)}")
            }
            getMethod("main", Array<String>::class.java).invoke(null, null)
        }
    }

}