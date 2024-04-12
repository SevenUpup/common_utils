package com.fido.common.common_utils.test.asm

import org.objectweb.asm.Type

/**
 * @author: FiDo
 * @date: 2024/4/12
 * @des:
 */

fun getVisitPosition(
    argumentTypes: Array<Type>,
    parameterIndex: Int,
    isStaticMethod: Boolean
): Int {
    if (parameterIndex < 0 || parameterIndex >= argumentTypes.size) {
        throw Error("getVisitPosition error")
    }
    return if (parameterIndex == 0) {
        if (isStaticMethod) {
            0
        } else {
            1
        }
    } else {
        getVisitPosition(
            argumentTypes,
            parameterIndex - 1,
            isStaticMethod
        ) + argumentTypes[parameterIndex - 1].size
    }
}