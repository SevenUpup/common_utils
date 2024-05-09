package com.fido.common.common_utils.test.asm.apitest.readclass

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

/**
 * @author: FiDo
 * @date: 2024/5/8
 * @des:
 */
fun main(){

    readTargetClassTreeApi()

}
internal val asmConfigs = mutableListOf<ReplaceMethodAnnotationItem>()
fun readTargetClassTreeApi() {
//    val classReader = ClassReader(PrivacyUtil::class.java.canonicalName)
    val classReader = ClassReader("com.fido.common.common_utils.device_info.PrivacyUtil")
//    println(PrivacyUtil::class.java.canonicalName)
    val classNode = ClassNode(Opcodes.ASM9)
    classReader.accept(classNode,ClassReader.SKIP_CODE)
    classNode.apply {
//        println("name = $name")
//        fields.forEach {
//            println("field:${it.name} ${it.access.accessName} ${it.desc} ${it.value}")
//        }
//        println("module = ${this.module}")
//        methods.forEach {
//            println("method:${it.name} ${it.access.accessName} ${it.desc} ${it.instructions}")
//        }
        collectHookAnntation(methods, name)
    }

    println(asmConfigs)
}

private const val REPLACE_METHOD_ANNO_DES = "Lcom/fido/annotation/asm/AsmMethodReplace;"
private fun collectHookAnntation(methods:List<MethodNode>,name:String) {
    methods.forEach { methodNode ->
        //收集需要修改的方法
        methodNode.invisibleAnnotations?.forEach { annotationNode ->
            if (annotationNode.desc == REPLACE_METHOD_ANNO_DES) {
                val asmItem = ReplaceMethodAnnotationItem(name, methodNode, annotationNode)
                /*LogPrint.normal("Collect Annotation"){
                    """
                        mOwner=${mOwner}
                        name=${name}
                        asmItem=${asmItem}
                    """.trimIndent()
                }*/
                if (!asmConfigs.contains(asmItem)) {
                    asmConfigs.add(asmItem)
                }
            }
        }
    }
}