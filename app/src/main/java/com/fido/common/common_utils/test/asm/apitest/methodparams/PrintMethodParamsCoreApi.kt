package com.fido.common.common_utils.test.asm.apitest.methodparams

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import java.lang.reflect.Modifier

/**
 * @author: FiDo
 * @date: 2024/4/30
 * @des:
 */
fun main() {
    printMethodParamsByCoreApi()
}

private const val clzName = "fido66/PrintMethodParamsCoreClass"

private fun printMethodParamsByCoreApi() {

    val classReader = ClassReader(PrintMethodParams::class.java.canonicalName)
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
            //修改包名和类名
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
            // 不是构造方法、抽象方法、Native 方法
            if (name != "<init>" && !Modifier.isAbstract(access) && !Modifier.isNative(access)) {
                println("""
                    ==============visitMethod=============
                    name = $name
                    access=${Modifier.toString(access)}
                    descriptor=${descriptor}
                """.trimIndent())
                return InternalMethodVisitor(vm,access,descriptor?:"")
            }
            return vm
        }
    }, ClassReader.SKIP_DEBUG)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/PrintMethodParamsCoreClass.class")
        loadClass(clzName.replace("/", "."))?.apply {
            val method = getMethod("print", String::class.java, Int::class.java)
            method.invoke(getConstructor().newInstance(), "FiDo", 66)
        }
    }
}

private class InternalMethodVisitor(
    methodVisitor: MethodVisitor,
    private val methodAccess: Int,
    private val methodDes: String
) :
    MethodVisitor(Opcodes.ASM9, methodVisitor) {
    override fun visitCode() {
        // 局部变量表的索引，非 static 方法，第 0 个是 this 指针
        var slotIndex = if (Modifier.isStatic(methodAccess)) 0 else 1
        val type = Type.getMethodType(methodDes)
        type.argumentTypes.forEach { type ->
            when (type.sort) {
                //参数类型 int 判断
                Type.INT -> {
                    mv.visitVarInsn(Opcodes.ILOAD, slotIndex)
                    //打印参数 名称&值
                    printInt()
                }
                //参数类型 string 判断
                Type.getType(String::class.java).sort -> {
                    mv.visitVarInsn(Opcodes.ALOAD, slotIndex)
                    printString()
                }
            }
            // type.size 就是该局部变量所占的槽位大小，对于 Long、Double 占两个槽位，其他类型占一个槽位
            slotIndex += type.size
        }
        super.visitCode()
    }

    override fun visitInsn(opcode: Int) {
        when (opcode) {
            Opcodes.ARETURN->{
                mv.visitInsn(Opcodes.DUP)
                printObject()
            }
            Opcodes.IRETURN->{
                println("========================> visitInsn IRETURN")
                mv.visitInsn(Opcodes.DUP)
                printInt()
            }
            // 其他类型大差不差就不写了
        }
        super.visitInsn(opcode)
    }

    private fun printString(){
        mv.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;")
        mv.visitInsn(Opcodes.SWAP)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )
    }

    private fun printObject(){
        mv.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;")
        mv.visitInsn(Opcodes.SWAP)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(${Type.getDescriptor(Object::class.java)})V",
            false
        )
    }

    private fun printInt() {
        mv.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;")
        mv.visitInsn(Opcodes.SWAP)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(I)V",
            false
        )
    }

}