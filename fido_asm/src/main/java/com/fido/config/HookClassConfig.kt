package com.fido.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/19
 * @des:
 */
data class HookClassConfig(
    val className:String,
    val parameterName:String,
    val parameterType: String,
    val parameterNewValue: String
) :Serializable{

    companion object{
        operator fun invoke(parameter: HookClassParameter):HookClassConfig?{
            return if (parameter.className.isEmpty() || parameter.parameterName.isEmpty() || parameter.parameterType.isEmpty() || parameter.parameterNewValue.isEmpty()) {
                null
            } else {
                HookClassConfig(
                    className = parameter.className,
                    parameterName = parameter.parameterName,
                    parameterType = parameter.parameterType,
                    parameterNewValue = parameter.parameterNewValue
                )
            }
        }
    }

}

open class HookClassParameter{
    var className:String = ""
    var parameterName:String = ""
    var parameterType:String = ""
    var parameterNewValue:String = ""
}