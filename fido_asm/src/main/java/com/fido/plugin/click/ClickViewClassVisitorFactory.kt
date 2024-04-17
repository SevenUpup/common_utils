package com.fido.plugin.click

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.fido.plugin.config.ViewClickConfig
import com.fido.plugin.constant.PluginConstant
import com.fido.plugin.utils.LogPrint
import com.fido.plugin.utils.filterLambda
import com.fido.plugin.utils.getClassDesc
import com.fido.plugin.utils.hasAnnotation
import com.fido.plugin.utils.isStatic
import com.fido.plugin.utils.matches
import com.fido.plugin.utils.nameWithDesc
import com.fido.plugin.utils.replaceDotBySlash
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

/**
 * @author: FiDo
 * @date: 2024/4/17
 * @des:
 */

internal interface ClickViewConfigParameter : InstrumentationParameters {
    @get:Input
    val config: Property<ViewClickConfig>
}

abstract class ClickViewClassVisitorFactory : AsmClassVisitorFactory<ClickViewConfigParameter> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        return ViewClickClassVisitor(nextClassVisitor = nextClassVisitor, config = config)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val config = parameters.get().config.get()
        val include = config.include
        val exclude = config.exclude
        if (include.isEmpty()) {
            return !classData.matches(rules = exclude)
        }
        return classData.matches(rules = include) && !classData.matches(rules = exclude)
    }

}

internal class ViewClickClassVisitor(
    private val nextClassVisitor: ClassVisitor,
    private val config: ViewClickConfig
) : ClassNode(Opcodes.ASM9) {

    private val viewClassDescriptor = getClassDesc(className = "android.view.View")

    private val onClickMethodDescriptor = "(Landroid/view/View;)Z"

    private val uncheckViewOnClickAnnotationDesc =
        getClassDesc(config.uncheckViewOnClickAnnotation)

    override fun visitEnd() {
        super.visitEnd()
        collectHookInfo()
        accept(nextClassVisitor)
    }

    private fun collectHookInfo() {
        val shouldHookMethodList = mutableSetOf<MethodNode>()
        methods.forEach {methodNode ->
            when{
                methodNode.hasUncheckViewOnClickAnnotation()->{
                    //不处理包含 UncheckViewOnClick 注解的方法
                }
                methodNode.hasCheckViewOnClickAnnotation()->{
                    //使用了 CheckViewOnClick 注解的情况
                    shouldHookMethodList.add(methodNode)
                }
                methodNode.hasButterKnifeOnClickAnnotation()->{
                    //使用了 ButterKnife OnClick 注解的情况
                    shouldHookMethodList.add(methodNode)
                }
                methodNode.isHookPoint() ->{
                    shouldHookMethodList.add(methodNode)
                }
            }
            val dynamicNodes = methodNode.filterLambda {
                LogPrint.normal(PluginConstant.VIEW_CLICK_TAG){
                    """
                        =======InvokeDynamicInsnNode=====
                        name=${it.name}
                        desc=${it.desc}
                        bsmArgs = ${it.bsmArgs}
                        bsm=${it.bsm} bsm.name=${it.bsm.name} bsm.desc=${it.bsm.desc} 
                        bsm.owner=${it.bsm.owner} 
                        bsm.tag=${it.bsm.tag} 
                        bsm.isInterface=${it.bsm.isInterface}
                    """.trimIndent()
                }

                val nodeName = it.name
                val nodeDesc = it.desc
                config.hookPointList.any { point ->
                    nodeName == point.methodName && nodeDesc.endsWith(point.interfaceSignSuffix)
                }
            }
            dynamicNodes.forEach { node->
                val handle = node.bsmArgs[1] as? Handle
                if (handle != null) {
                    val nameWithDesc = handle.name + handle.desc
                    val method = methods.find {methodNode ->
                        methodNode.nameWithDesc == nameWithDesc
                    }
                    if (method != null) {
                        shouldHookMethodList.add(method)
                    }
                }
            }
        }
        if (shouldHookMethodList.isNotEmpty()) {
            shouldHookMethodList.forEach {
                hookMethod(it)
            }
            LogPrint.normal(tag = PluginConstant.VIEW_CLICK_TAG) {
                "$name 发现 ${shouldHookMethodList.size} 个 View.OnClickListener 指令，完成处理..."
            }
        }

    }

    private fun hookMethod(node: MethodNode) {
        val argumentTypes = Type.getArgumentTypes(node.desc)
        //计算 View 对象是方法的第几个入参参数
        val viewArgumentIndex = argumentTypes?.indexOfFirst {
            it.descriptor == viewClassDescriptor
        }?:-1
        if (viewArgumentIndex >= 0) {
            val instructions = node.instructions
            if (instructions != null && instructions.size() > 0) {
                val list = InsnList()
                //引用View对象
                list.add(VarInsnNode(
                    Opcodes.ALOAD,getVisitPosition(
                        argumentTypes,
                        viewArgumentIndex,
                        node.isStatic
                    )
                ))
                list.add(
                    MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        replaceDotBySlash(className = config.onClickClass),
                        config.onClickMethodName,
                        onClickMethodDescriptor
                    )
                )
                val labelNode = LabelNode()
                list.add(JumpInsnNode(Opcodes.IFNE,labelNode))
                list.add(InsnNode(Opcodes.RETURN))
                list.add(labelNode)
                instructions.insert(list)
            }
        }
    }

    private fun getVisitPosition(
        argumentTypes: Array<Type>,
        parameterIndex: Int,
        isStaticMethod: Boolean
    ): Int {
        if (parameterIndex < 0 || parameterIndex >= argumentTypes.size) {
            throw Error("getVisitPosition error")
        }
        return if (parameterIndex == 0) {
            if (isStaticMethod) {
                0
            } else {
                1
            }
        } else {
            getVisitPosition(
                argumentTypes,
                parameterIndex - 1,
                isStaticMethod
            ) + argumentTypes[parameterIndex - 1].size
        }
    }

    private fun MethodNode.isHookPoint():Boolean{
        val myInterfaces = interfaces
        if (myInterfaces.isNullOrEmpty()) {
            return false
        }
        val methodNameWithDesc = nameWithDesc
        return config.hookPointList.any {
            myInterfaces.contains(it.interfaceName) &&
                    methodNameWithDesc == it.methodNameWithDesc
        }
    }

    private fun MethodNode.hasUncheckViewOnClickAnnotation():Boolean{
        return hasAnnotation(annotationDesc = uncheckViewOnClickAnnotationDesc)
    }

    private fun MethodNode.hasCheckViewOnClickAnnotation(): Boolean {
        return hasAnnotation(annotationDesc = getClassDesc(config.checkViewOnClickAnnotation))
    }

    private fun MethodNode.hasButterKnifeOnClickAnnotation(): Boolean {
        return hasAnnotation(annotationDesc = "Lbutterknife/OnClick;")
    }
}




