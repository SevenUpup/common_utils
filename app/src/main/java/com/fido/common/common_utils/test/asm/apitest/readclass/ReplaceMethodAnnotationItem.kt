package com.fido.common.common_utils.test.asm.apitest.readclass

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.MethodNode

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:
 */
class ReplaceMethodAnnotationItem(
    targetClass: String,
    methodNode: MethodNode,
    annotationNode: AnnotationNode
) {

    var oriClass: String? = null
    var oriMethod: String? = null
    var oriDesc: String? = null
    var oriAccess = Opcodes.INVOKESTATIC

    var targetClass: String = ""
    var targetMethod: String = ""
    var targetDesc: String = ""
    var targetAccess = Opcodes.INVOKESTATIC

    init {
        this.targetClass = targetClass
        this.targetMethod = methodNode.name
        this.targetDesc = methodNode.desc
        var sourceName = ""
        //注解是key value形式，
        for (i in 0 until annotationNode.values.size / 2) {
            val key = annotationNode.values[i * 2]
            val value = annotationNode.values[i * 2 + 1]
            if (key == "oriClass") {
                sourceName = value.toString()
                oriClass = sourceName.substring(1, sourceName.length - 1)
            } else if (key == "oriAccess") {
                oriAccess = value as Int
            } else if (key === "oriMethod") {
                oriMethod = value as String?
            }
        }
        if (oriMethod == null) {
            oriMethod = targetMethod
        }
        //静态方法，oriDesc 跟 targetDesc 一样
        if (oriAccess == Opcodes.INVOKESTATIC) {
            oriDesc = targetDesc
        } else {
            //非静态方法，约定第一个参数是实例类名，oriDesc 比 targetDesc 少一个参数，处理一下
            var param = targetDesc.split(")")[0] + ")" //(Landroid/telephony/TelephonyManager;)
            val returnValue = targetDesc.split(")")[1] //Ljava/lang/String;
            if (param.indexOf(sourceName) == 1) {
                param = "(" + param.substring(param.indexOf(sourceName) + sourceName.length)
            }
            oriDesc = param + returnValue

            //处理之后
            //targetDesc=(Landroid/telephony/TelephonyManager;)Ljava/lang/String;
            //oriDesc= Ljava/lang/String;
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is ReplaceMethodAnnotationItem) {
            return (other.oriAccess == oriAccess &&
                    other.oriClass == oriClass &&
                    other.oriDesc == oriDesc &&
                    other.oriMethod == oriMethod
                    )
        }

        return super.equals(other)
    }

    override fun toString(): String {
        return "ReplaceMethodAnnotationItem(oriClass=$oriClass, oriMethod=$oriMethod, oriDesc=$oriDesc, oriAccess=$oriAccess, targetClass='$targetClass', targetMethod='$targetMethod', targetDesc='$targetDesc', targetAccess=$targetAccess)"
    }

}