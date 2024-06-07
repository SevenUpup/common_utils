package com.fido.constant

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

    // ======================== ViewClick Plugin ===========================
    const val VIEW_CLICK_TAG = "ViewClick"

    // ======================== ReplaceClass Plugin ========================
    const val REPLACE_TAG = "ReplaceClass"

    // =========================== Toast Plugin ===========================
    const val TOAST_TAG = "Toast"

    // =========================== Hook Class ============================
    const val HOOK_CLASS_TAG = "HookClass"

    // =========================== Replace Method ============================
    const val REPLACE_METHOD_TAG = "REPLACE_METHOD"

    /**
     * 收集需要替换的类的List
     */
    const val FIDO_ASM_REPLACE_METHOD_CLASS = "fido_asm_replace_method_class"

    /**
     * 替换类方法的Task开关
     */
    const val FIDO_ASM_REPLACE_METHOD_ENABLE = "fido_asm_replace_method_enable"
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
