package com.fido.common.common_utils.test.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
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

    companion object {

        val prefixPath =
            "G:\\PraticeDemo\\common_utils\\app\\src\\main\\java\\com\\fido\\common\\common_utils\\test\\asm\\"

        @JvmStatic
        fun main(args: Array<String>) {
            val clazz = PrintTest::class.java
            val clzReader = ClassReader(FileInputStream(ASMJavaTest.getClassFilePath(clazz)))
            val clzWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val newVisitor = AddPrintlnClassVisitor(Opcodes.ASM9, clzWriter)
            clzReader.accept(newVisitor, 0)

            val byteArr = clzWriter.toByteArray()

            val newClazzName = prefixPath + clazz.simpleName + "_new.class";
            val fos = FileOutputStream(newClazzName)
            fos.write(byteArr)
            fos.flush()
            fos.close()

            ClassReaderUtils.readClassMethodAndFiled(newClazzName)

            //测试插桩代码4 InsertCodeTest
            testInspectCode()

        }

        private fun testInspectCode() {
            //--------------------------------------------------------------------------------
            //1.在使用 new ClassWriter(0)时，不会自动计算任何东西。必须自行计算帧、局部变 量与操作数栈的大小。
            //--------------------------------------------------------------------------------
            //2.在使用 new ClassWriter(ClassWriter.COMPUTE_MAXS)时，将为你计算局部变量与操作数栈部分的大小。
            // 还是必须调用 visitMaxs，但可以使用任何参数：它们将被忽略并重新计算。使用这一选项时，仍然必须自行计算这些帧。
            //--------------------------------------------------------------------------------
            //3.在 new ClassWriter(ClassWriter.COMPUTE_FRAMES)时，一切都是自动计算。不再需要调用 visitFrame，
            // 但仍然必须调用 visitMaxs（参数将被忽略并重新计算）
            //--------------------------------------------------------------------------------
            val cr = ClassReader("com.fido.common.common_utils.test.asm.InsertCodeJavaTest")
            val cw = ClassWriter(cr, ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS)
            cr.accept(object : ClassVisitor(Opcodes.ASM9, cw) {

                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?
                ): MethodVisitor {
                    val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
                    return object : MethodVisitor(Opcodes.ASM9, mv) {
                        override fun visitLineNumber(line: Int, start: Label?) {
                            println("经过这个测试行数:" + line + "可以对应到源代码的行数");
                            super.visitLineNumber(line, start)
                        }

                        override fun visitInsn(opcode: Int) {

                            //这里是访问语句结束，在return结束之前添加语句（通过判断语句）
                            //其中的 owner 必须被设定为所转换类的名字。现在必须在任意 RETURN 之前添加其他四条
                            //指令，还要在任何 xRETURN 或 ATHROW 之前添加，它们都是终止该方法执行过程的指令。这些
                            //指令没有任何参数，因此在 visitInsn 方法中访问。于是，可以重写这一方法，以增加指令：
                            if ("<init>" != name && (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                                //在方法return之前添加代码,及从后面插入，return之前(类似于从下往上)
                                //调用 System.currentTimeMillis()
                                mv.visitMethodInsn(
                                    Opcodes.INVOKESTATIC,
                                    "java/lang/System",
                                    "currentTimeMillis",
                                    "()J",
                                    false
                                )
                                //存储指令： LSTORE 	栈顶 long 型数值存入指定局部变量
                                //注意：long占两个变量索引位置
                                mv.visitVarInsn(Opcodes.LSTORE, 3)

                                mv.visitFieldInsn(
                                    Opcodes.GETSTATIC,
                                    "java/lang/System",
                                    "out",
                                    "Ljava/io/PrintStream"
                                )
                                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder")
                                //复制栈顶值并压入栈顶
                                mv.visitInsn(Opcodes.DUP)

                                //构建一个StringBuilder
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "<init>",
                                    "()V",
                                    false
                                )
                                //通过LDC指令将常量 cost: 压入栈顶
                                mv.visitLdcInsn("cost:")
                                //调用append
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(Ljava/lang/String;)Ljava/lang/StringBuilder",
                                    false
                                )
                                //取出 var3 long类型至栈顶
                                mv.visitVarInsn(Opcodes.LLOAD, 3)
                                //取出var1 long到栈顶
                                mv.visitVarInsn(Opcodes.LLOAD, 1)
                                //将两个long类型相减
                                mv.visitInsn(Opcodes.LSUB)
                                //append (var3 - var1)
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "append",
                                    "(J)Ljava/lang/StringBuilder",
                                    false
                                )
                                //调用StringBuilder toString()
                                mv.visitMethodInsn(
                                    Opcodes.INVOKEVIRTUAL,
                                    "java/lang/StringBuilder",
                                    "toString",
                                    "()Ljava/lang/String",
                                    false
                                )
                                //调用 PrintStream 的println方法
                                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println",
                                    "(Ljava/lang/String;)V",false)
                            }
                            mv.visitInsn(opcode)
                        }

                        override fun visitCode() {
                            super.visitCode()
                            //方法开始（可以在此处添加代码，在原来的方法之前执行）
                            println("方法名字=========>"+name);
                            //在方法开始时插入var1
                            System.currentTimeMillis()
                            if ("<init>" != name) {
                                //在这里 生成var3
                                mv.visitMethodInsn(Opcodes.INVOKESTATIC,"java/lang/System","currentTimeMillis",
                                    "()J",false)
                                //插入位置 = 1 的 变量索引位置
                                mv.visitVarInsn(Opcodes.LSTORE,1)
                            }
                        }
                    }
                }
            }, 0)
            //写入新的文件
            val path = prefixPath + "InsertCodeTest_new.class"
            println("类生成位置=${path}")
            FileOutputStream(path).apply {
                write(cw.toByteArray())
                flush()
                close()
            }
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
                methodVisitor.visitFieldInsn(
                    Opcodes.GETSTATIC, "java/lang/System", "out",
                    "Ljava/io/PrintStream;"
                )
                methodVisitor.visitLdcInsn("Hello engineer!")
                methodVisitor.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
                    "(Ljava/lang/String;)V", false
                )
            }
        }
    }

}
