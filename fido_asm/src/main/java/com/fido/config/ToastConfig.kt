package com.fido.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
internal data class ToastConfig(
    val toasterClass:String,
    val showToastMethodName:String
) :Serializable{

    companion object{
        operator fun invoke(parameter: ToastPluginParameter):ToastConfig?{
            return if (parameter.toasterClass.isEmpty() || parameter.showToastMethodName.isEmpty()) {
                null
            } else {
                ToastConfig(
                    toasterClass = parameter.toasterClass,
                    showToastMethodName = parameter.showToastMethodName
                )
            }
        }
    }

}

open class ToastPluginParameter:Serializable{
    var toasterClass:String = ""
    var showToastMethodName:String = ""
}