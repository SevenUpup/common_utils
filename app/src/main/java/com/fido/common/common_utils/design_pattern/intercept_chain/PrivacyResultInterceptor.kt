package com.fido.common.common_utils.design_pattern.intercept_chain

import com.fido.common.common_base_util.ext.toast

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class PrivacyResultInterceptor(private val privacyInterceptBean: PrivacyInterceptBean):Intercept {
    override fun intercept(chain: InterceptChain) {
        if (privacyInterceptBean.isAgreedFull) {
            toast("您已同意全部协议，即将去往登录页")
        }
        if (privacyInterceptBean.isAgreedPart) {
            toast("您同意了部分协议，即将浏览首页")
        }
        chain.process()
    }
}