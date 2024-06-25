package com.fido.common.common_utils.design_pattern.interceptor2.interceptors

import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_util.ext.topActivity
import com.fido.common.common_utils.design_pattern.interceptor2.DialogPass
import com.fido.common.common_utils.design_pattern.interceptor2.InterceptorChain

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class VerifyIntercept() : InterceptorChain<DialogPass>() {

    override fun intercept(data: DialogPass?) {
        topActivity.showNormalInputDialog(content = "请输入验证码", confirmBlock = {
            super.intercept(data)
        })
    }

}