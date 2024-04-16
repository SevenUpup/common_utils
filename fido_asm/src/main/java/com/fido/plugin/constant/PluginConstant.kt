package com.fido.plugin.constant

/**
 * @author: FiDo
 * @date: 2024/4/15
 * @des:
 */
object PluginConstant {

    // ======================== hook method plugin =========================
    const val CUSTOM_METHOD_TAG = "HookCustomMethod"
    const val ADD = "ADD"
    const val DELETE = "DELETE"
    const val REPLACE = "REPLACE"

}

fun String.hookMethodMode():String{
    if (this.contains(PluginConstant.ADD, true) || this == "A") {
        return PluginConstant.ADD
    }
    if (this.contains(PluginConstant.DELETE, true) || this == "D") {
        return PluginConstant.DELETE
    }
    if (this.contains(PluginConstant.REPLACE, true) || this == "R") {
        return PluginConstant.REPLACE
    }
    return PluginConstant.ADD
}
