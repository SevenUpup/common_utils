package com.fido.common.common_utils.design_pattern.intercept_chain

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
data class PrivacyInterceptBean(
    var showFullPrivacy:Boolean = true,
    var showPartPrivacy:Boolean = false,
    var isAgreedFull:Boolean = false,
    var isAgreedPart:Boolean = false,

)
