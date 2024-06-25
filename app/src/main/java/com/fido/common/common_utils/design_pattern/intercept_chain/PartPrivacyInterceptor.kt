package com.fido.common.common_utils.design_pattern.intercept_chain

import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_util.ext.toast

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class PartPrivacyInterceptor(private var privacyInterceptBean: PrivacyInterceptBean) : Intercept {
    override fun intercept(chain: InterceptChain) {
        if (privacyInterceptBean.showPartPrivacy) {
            chain.context?.showNormalConfirmDialog("", "是否同意部分隐私？", onCancelBlock = {
                toast("您已拒绝，将在3s后退出")
                chain.clearIntercept()
            }, onConfirmBlock = {
                privacyInterceptBean.isAgreedPart = true
                chain.process()
            })
        }else{
            chain.process()
        }
    }
}