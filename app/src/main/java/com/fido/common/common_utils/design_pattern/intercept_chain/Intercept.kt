package com.fido.common.common_utils.design_pattern.intercept_chain

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
interface Intercept {

    fun intercept(chain: InterceptChain)

}