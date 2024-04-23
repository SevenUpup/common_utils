package com.fido.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/19
 * @des:
 */
data class HookClassConfig(
//    val className:String,
//    val parameterName:String,
//    val parameterNewValue: String,
//    val split:String,
    val className:List<String>,
    val parameterName:List<String>,
    val parameterNewValue: List<Any>,
    val split:String,
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
                    split = parameter.split.ifBlank { "," }
                )
            }
        }
    }

}

open class HookClassParameter{
    var className:List<String> = emptyList()
    var parameterName:List<String> = emptyList()
    var parameterNewValue:List<Any> = emptyList()
    var split = ","
}