package com.fido.utils

/**
 * @author: FiDo
 * @date: 2024/5/20
 * @des:
 */


import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import java.io.File

private const val APT_OPTION_MODULE_NAME = "moduleName"
private const val APT_OPTION_LOGGABLE = "loggable"

internal fun Project.apt(loggable: Boolean) {
//    val android = extensions.findByName("android") as? BaseExtension
    val android = extensions.findByType(CommonExtension::class.java)
    android?.apply {
        val options = mapOf(
            APT_OPTION_MODULE_NAME to name,
            APT_OPTION_LOGGABLE to loggable.toString()
        )
        defaultConfig.javaCompileOptions.annotationProcessorOptions.arguments(options)
        productFlavors.forEach { flavor ->
            flavor.javaCompileOptions.annotationProcessorOptions.arguments(options)
        }
    }
}


internal fun String.separator(): String {
    return replace('.', File.separatorChar).replace('/', File.separatorChar)
}