package com.fido.config.replace_method

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:
 */
internal data class ReplaceMethodConfig(
    val enable:Boolean = true,
    val replaceClassName:List<String>,
):Serializable{
    companion object{
        operator fun invoke(parametersConfig: ReplaceMethodPluginParameters):ReplaceMethodConfig?{
            if (!parametersConfig.enable || parametersConfig.replaceClassName.isNullOrEmpty()) {
                return null
            }
            return ReplaceMethodConfig(parametersConfig.enable,parametersConfig.replaceClassName)
        }
    }
}

open class ReplaceMethodPluginParameters:Serializable{
    var enable:Boolean = true
    var replaceClassName:List<String> = emptyList()
}