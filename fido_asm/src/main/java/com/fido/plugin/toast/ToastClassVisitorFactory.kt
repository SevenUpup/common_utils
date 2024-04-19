package com.fido.plugin.toast

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.config.ToastConfig
import com.fido.constant.PluginConstant
import com.fido.utils.LogPrint
import com.fido.utils.replaceDotBySlash
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:  通过指定类名方法，全局替换Toast.show()方法
 */
internal interface ToastConfigParameter:InstrumentationParameters{
    @get:Input
    val config: Property<ToastConfig>
}
internal abstract class ToastClassVisitorFactory:AsmClassVisitorFactory<ToastConfigParameter> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return ToastClassVisitor(config, nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        return classData.className != config.toasterClass
    }

}

private class ToastClassVisitor(
    val config: ToastConfig,
    val nextClassVisitor: ClassVisitor
):ClassNode(Opcodes.ASM9){
    companion object {

        private const val TOAST = "android/widget/Toast"

    }
    override fun visitEnd() {
        super.visitEnd()
        val toastMethodInsnNodeList = mutableListOf<MethodInsnNode>()
        methods.forEach {methodNode ->
            methodNode.instructions.forEach {
                if (it is MethodInsnNode && it.opcode == Opcodes.INVOKEVIRTUAL && it.owner == TOAST && it.name == "show" && it.desc == "()V"){
                    toastMethodInsnNodeList.add(it)
                }
            }
        }
        // 原本： Toast.makeText(this$0, "hahaha", 0).show();
        // 现在： HToaster.hToast(Toast.makeText(this$0, "hahaha", 0));
        if (toastMethodInsnNodeList.isNotEmpty()) {
            toastMethodInsnNodeList.forEach {
                it.opcode = Opcodes.INVOKESTATIC
                it.owner = replaceDotBySlash(className = config.toasterClass)
                it.name = config.showToastMethodName
                it.desc = "(L$TOAST;)V"
                it.itf = false
            }
        }
        LogPrint.normal(tag = PluginConstant.TOAST_TAG) {
            name + " 发现 ${toastMethodInsnNodeList.size} 个 Toast.show 指令，完成处理..."
        }
        accept(nextClassVisitor)
    }

}