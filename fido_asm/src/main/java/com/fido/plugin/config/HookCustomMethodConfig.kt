package com.fido.plugin.config

import com.fido.plugin.constant.PluginConstant
import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/15
 * @des:
 *       originalClassName   需要插装的原类名
 *       originalMethodName  需要插装的原方法名
 *       targetClassName:需要插装的目标类名
 *       targetMethodName:需要插装的目标方法名
 *       handleMode:需要插装的方式（增 ADD 删除 DELETE  替换 REPLACE）
 */
internal data class HookCustomMethodConfig(
    val targetClassName:String,
    val targetMethodName:String,
    val originalClassName:String,
    val originalMethodName:String,
    val handleMode:String,
):Serializable{

    companion object{
        operator fun invoke(parameter: CustomMethodPluginParameter):HookCustomMethodConfig{
            return HookCustomMethodConfig(
                originalClassName = parameter.originalClassName.replace(".","/"),
                originalMethodName = parameter.originalMethodName,
                targetClassName = parameter.targetClassName.replace(".","/"),
                targetMethodName = parameter.targetMethodName,
                handleMode = parameter.handleMode
            )
        }
    }
}

open class CustomMethodPluginParameter{
    var targetClassName:String = ""
    var targetMethodName:String = ""
    var originalClassName:String= ""
    var originalMethodName:String= ""
    var handleMode = PluginConstant.ADD
}

