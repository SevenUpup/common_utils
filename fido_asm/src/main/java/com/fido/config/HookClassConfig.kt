package com.fido.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/19
 * @des:
 */

/**
 * @param replaceSymbol 重复类 替换符号默认*
 * 使用：如果className中元素重复，可以将相同元素用*代替 如：className = [A,*,*,B,*]
 */
data class HookClassConfig(
    val className:List<String>,
    val parameterName:List<String>,
    val parameterNewValue: List<Any>,
//    val replaceSymbol:String,
) :Serializable{

    companion object{
        operator fun invoke(parameter: HookClassParameter):HookClassConfig?{
            return if (parameter.className.isEmpty() || parameter.parameterName.isEmpty()  || parameter.parameterNewValue.isEmpty()) {
                null
            } else {
                HookClassConfig(
                    className = parameter.className,
                    parameterName = parameter.parameterName,
                    parameterNewValue = parameter.parameterNewValue,
//                    replaceSymbol = parameter.replaceSymbol.ifBlank { "*" }
                )
            }
        }
    }

}

open class HookClassParameter{
    var className:List<String> = emptyList()
    var parameterName:List<String> = emptyList()
    var parameterNewValue:List<Any> = emptyList()
//    var replaceSymbol = "*"
}