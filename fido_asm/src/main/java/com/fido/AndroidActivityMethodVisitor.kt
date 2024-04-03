package com.fido

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author: FiDo
 * @date: 2024/4/3
 * @des: 其实就是在 Activity 的所有方法中开始时和结束时分别调用 com.fido.common.common_utils.hook.MethodInOutHook#methodIn() 方法和 com.fido.common.common_utils.hook.MethodInOutHook#methodOut() 方法
 * 链接：https://juejin.cn/post/7351656743186726939
 */

class AndroidActivityMethodVisitor(
    private val classContext: ClassContext,
    private val outputVisitor: MethodVisitor,
    access: Int,
    private val name: String,
    des: String
) : AdviceAdapter(ASM9,outputVisitor,access,name,des) {

    override fun onMethodEnter() {
        super.onMethodEnter()
        Log.d(msg = "Hook method in: className=${classContext.currentClassData.className}, method=${name}")
        outputVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            METHOD_HOOK_CLASS_NAME,
            IN_HOOK_METHOD_NAME,
            IN_HOOK_METHOD_DES,
            false
        )
    }

    override fun onMethodExit(opcode: Int) {
        Log.d(msg = "Hook method out: className=${classContext.currentClassData.className}, method=${name}")
        outputVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            METHOD_HOOK_CLASS_NAME,
            OUT_HOOK_METHOD_NAME,
            OUT_HOOK_METHOD_DES,
            false
        )
        super.onMethodExit(opcode)
    }

    companion object{
        const val METHOD_HOOK_CLASS_NAME = "com/fido/common/common_utils/hook/MethodInOutHook"

        const val IN_HOOK_METHOD_NAME = "methodIn"
        const val IN_HOOK_METHOD_DES = "()V"

        const val OUT_HOOK_METHOD_NAME = "methodOut"
        const val OUT_HOOK_METHOD_DES = "()V"
    }

}