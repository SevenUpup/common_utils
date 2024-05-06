package com.fido.common.common_utils.test.asm.apitest.measure_methodtime

import com.fido.common.common_utils.test.asm.apitest.ASMConstant
import com.fido.common.common_utils.test.asm.common.isMethodReturn
import com.fido.common.common_utils.test.asm.common.loadClass
import com.fido.common.common_utils.test.asm.common.toFile
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:
 */

fun main() {
    measureMethodTimeCoreApi()
}

private const val clzName = "fido66/MeasureMethodTimeCoreClass"
fun measureMethodTimeCoreApi() {
    val classReader = ClassReader(MeasureMethodTime::class.java.canonicalName)
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
            val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
            return InternalMethodVisitor(access,descriptor,methodVisitor)
        }
    }, ClassReader.EXPAND_FRAMES)

    classWriter.toByteArray().apply {
        toFile(ASMConstant.ASM_Class_DIR + "/MeasureMethodTimeCoreClass.class")
        loadClass(clzName.replace("/", "."))?.apply {
            getMethod("measure").invoke(getConstructor().newInstance())
        }
    }
}

/**
 * MethodVisitor
 *
 * 这里使用 LocalVariablesSorter 是为了方便生成局部变量获取其索引
 */
private class InternalMethodVisitor(
    access: Int,
    descriptor: String?,
    methodVisitor: MethodVisitor
) : LocalVariablesSorter(Opcodes.ASM9,access,descriptor,methodVisitor) {

    /** 标记需要测量的方法 */
    private var trackMethodTag = false
    /** 起始时间索引 */
    var startTimeIndex = 0
    /** 起始时间索引 */
    var endTimeIndex = 0
    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (Type.getDescriptor(MeasureTime::class.java) == descriptor) {
            trackMethodTag = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitCode() {
        if (trackMethodTag) {
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false
            )
            startTimeIndex = newLocal(Type.LONG_TYPE)
            mv.visitVarInsn(Opcodes.LSTORE,startTimeIndex)
        }
        super.visitCode()
    }

    override fun visitInsn(opcode: Int) {
        if (trackMethodTag && opcode.isMethodReturn()) {
            // 生成endTime变量
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false
            )
            endTimeIndex = newLocal(Type.LONG_TYPE)
            mv.visitVarInsn(Opcodes.LSTORE,endTimeIndex)

            mv.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
            )
            //结束时间压入栈顶
            mv.visitVarInsn(Opcodes.LLOAD,endTimeIndex)
            //开始时间压入栈顶
            mv.visitVarInsn(Opcodes.LLOAD,startTimeIndex)
            // 将栈顶两个long值求差后压入栈顶
            mv.visitInsn(Opcodes.LSUB)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(J)V",
                false
            )
        }
        super.visitInsn(opcode)
    }

}
