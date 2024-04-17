package com.fido.plugin.config

import java.io.Serializable

/**
 * @author: FiDo
 * @date: 2024/4/17
 * @des:
 */
internal data class ViewClickConfig(
    val onClickClass: String,
    val onClickMethodName: String,
    val checkViewOnClickAnnotation: String,
    val uncheckViewOnClickAnnotation: String,
    val hookPointList: Set<ViewClickHookPoint>,
    val include: Set<String>,
    val exclude:Set<String>
) : Serializable {
    companion object {
        operator fun invoke(parameter: ViewClickPluginParameter): ViewClickConfig? {
            val onClickClass = parameter.onClickClass
            val onClickMethodName = parameter.onClickMethodName
            return if (onClickClass.isEmpty() || onClickMethodName.isEmpty()) {
                 null
            } else {
                ViewClickConfig(
                    onClickClass = onClickClass,
                    onClickMethodName = onClickMethodName,
                    checkViewOnClickAnnotation = parameter.checkViewOnClickAnnotation,
                    uncheckViewOnClickAnnotation = parameter.uncheckViewOnClickAnnotation,
                    include = parameter.include,
                    exclude = parameter.exclude,
                    hookPointList = parameter.hookPointList.ifEmpty {
                        setOf(
                            ViewClickHookPoint(
                                interfaceName = "android/view/View\$OnClickListener",
                                methodName = "onClick",
                                methodNameWithDesc = "onClick(Landroid/view/View;)V"
                            ),
                            ViewClickHookPoint(
                                interfaceName = "com/chad/library/adapter/base/listener/OnItemClickListener",
                                methodName = "onItemClick",
                                methodNameWithDesc = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
                            ),
                            ViewClickHookPoint(
                                interfaceName = "com/chad/library/adapter/base/listener/OnItemChildClickListener",
                                methodName = "onItemChildClick",
                                methodNameWithDesc = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V",
                            ),
                        )
                    }
                )
            }
        }
    }
}

data class ViewClickHookPoint(
    val interfaceName: String,
    val methodName: String,
    val methodNameWithDesc: String
) : Serializable {
    val interfaceSignSuffix = "L$interfaceName;"
}

open class ViewClickPluginParameter {

    var onClickClass: String = ""
    var onClickMethodName: String = ""

    var checkViewOnClickAnnotation: String = ""
    var uncheckViewOnClickAnnotation: String = ""

    var hookPointList:Set<ViewClickHookPoint> = setOf()
    var include: Set<String> = setOf()
    var exclude:Set<String> = setOf()
}