package com.fido.common.common_utils.design_pattern.interceptor2.interceptors

import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_util.ext.topActivity
import com.fido.common.common_utils.design_pattern.interceptor2.DialogPass
import com.fido.common.common_utils.design_pattern.interceptor2.InterceptorChain

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class TipsIntercept : InterceptorChain<DialogPass>() {

    override fun intercept(data: DialogPass?) {
        topActivity.showNormalConfirmDialog(content = "你确定要发送验证码吗？", onConfirmBlock = {
            super.intercept(data)
        }, onCancelBlock = {

        })
    }
}