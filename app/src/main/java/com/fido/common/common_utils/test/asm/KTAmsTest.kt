package com.fido.common.common_utils.test.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:
 */
class KTAmsTest {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val clazz = PrintTest::class.java
            val clzReader = ClassReader(FileInputStream(ASMJavaTest.getClassFilePath(clazz)))
            val clzWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val newVisitor  = AddPrintlnClassVisitor(Opcodes.ASM9,clzWriter)
            clzReader.accept(newVisitor,0)

            val byteArr = clzWriter.toByteArray()
            val prefixPath =
                "G:\\PraticeDemo\\common_utils\\app\\src\\main\\java\\com\\fido\\common\\common_utils\\test\\asm\\"
            val newClazzName = prefixPath + clazz.simpleName + "_new.class";
            val fos =FileOutputStream(newClazzName)
            fos.write(byteArr)
            fos.flush()
            fos.close()

            ClassReaderUtils.readClassMethodAndFiled(newClazzName)
        }
    }

    fun test() {
        println("hello world!")
//        println("hello engineer!")
    }

}

class AddPrintlnClassVisitor(api: Int, classVisitor: ClassVisitor?) :
    ClassVisitor(api, classVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)

        val newVM = MyMethodVisitor(vm)

        return newVM
    }

    class MyMethodVisitor(private val methodVisitor: MethodVisitor) :
        MethodVisitor(Opcodes.ASM9, methodVisitor) {
        override fun visitMethodInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            println("owner=${owner} name=${name} descriptor=${descriptor} isInterface=${isInterface}")
            if (opcode == Opcodes.INVOKEVIRTUAL
                && owner == "java/io/PrintStream"
                && name == "println"
                && descriptor == "(Ljava/lang/String;)V"
                && !isInterface
            ) {

                // 1
//                val lable = Label()
//                super.visitLabel(lable)
//                super.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out",
//                    "Ljava/io/PrintStream;")
//                super.visitLdcInsn("Hello engineer!")
//                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println",
//                    "(Ljava/lang/String;)V",false)
                //或 2
                methodVisitor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out",
                    "Ljava/io/PrintStream;")
                methodVisitor.visitLdcInsn("Hello engineer!")
                methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println",
                    "(Ljava/lang/String;)V",false)
            }
        }
    }

}
