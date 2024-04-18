package com.fido.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
internal data class ReplaceClassConfig(
    val originClass:String,
    val targetClass:String,
    val include:Set<String>,
    val exclude:Set<String>
):Serializable{
    companion object{
        operator fun invoke(parameter: ReplaceClassPluginParameter): ReplaceClassConfig? {
            if (parameter.originClass.isEmpty() || parameter.targetClass.isEmpty()) return null
            return ReplaceClassConfig(
                originClass = parameter.originClass,
                targetClass = parameter.targetClass,
                include = parameter.include,
                exclude = parameter.exclude
            )
        }
    }
}

open class ReplaceClassPluginParameter:Serializable{
    var originClass:String = ""
    var targetClass:String = ""
    var include:Set<String> = setOf()
    var exclude:Set<String> = setOf()
}
