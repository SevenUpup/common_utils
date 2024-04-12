package com.fido.permission

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author: FiDo
 * @date: 2024/4/11
 * @des:
 */
class PermissionCheckClassVisitor(private val classContext: ClassContext, val classVisitor: ClassVisitor?) :
    ClassVisitor(Opcodes.ASM9, classVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val vm = super.visitMethod(access, name, descriptor, signature, exceptions)
        return PermissionCheckMethodVisitor(vm,access, name, descriptor,classContext)
    }

    class PermissionCheckMethodVisitor(
        methodVisitor: MethodVisitor?,
        access: Int,
        name: String?,
        descriptor: String?,
        val classContext: ClassContext
    ) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

        override fun visitMethodInsn(
            opcodeAndSource: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            // 注意点1
            /*if (opcodeAndSource == Opcodes.INVOKEVIRTUAL
                && name == "requestPermissions"
                && descriptor == "([Ljava/lang/String;I)V"
            ) {
                print("find hook point ==========")
                print("class=${classContext.currentClassData.className} name=${name} des=${descriptor}\nclassInfo=${classContext.currentClassData}")
                // 注意点2
                super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/fido/common/common_utils/hook/PermissionCheckDelegateWrapper",
                    "hookRequestPermission",
                    "(Ljava/lang/Object;[Ljava/lang/String;I)V",
                    false
                )
            } else {
                super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
            }*/
            println("start hook WriteCalendarAc")
            println("""
                opcodeAndSource=${opcodeAndSource}
                owner=${owner}
                name=${name}
                descriptor=${descriptor}
                isInterface=${isInterface}
            """.trimIndent())
            //TODO test 尝试hook WriteCalendarAc的 permissionCheckTest 方法测试
            if (opcodeAndSource == Opcodes.INVOKESPECIAL
                && name == "permissionCheckTest"
                && descriptor == "(Ljava/lang/String;I)V"
            ) {
                print("find hook point ==========")
                print("class=${classContext.currentClassData.className} name=${name} des=${descriptor}\nclassInfo=${classContext.currentClassData}")
                // 注意点2
                super.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "com/fido/common/common_utils/hook/PermissionCheckDelegateWrapper",
                    "testHook",
                    "(Ljava/lang/String;I)V",
                    false
                )
            } else {
                super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
            }

        }


    }

}