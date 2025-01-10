package com.fido.common.common_utils.design_pattern.interceptor2.interceptors

import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbRequest
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import com.fido.common.common_base_ui.util.showLoading
import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_ui.util.showNormalListDialog
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.ext.topActivity
import com.fido.common.common_utils.design_pattern.interceptor2.DialogPass
import com.fido.common.common_utils.design_pattern.interceptor2.InterceptorChain

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 */
class OneIntercept : InterceptorChain<DialogPass>() {

    override fun intercept(data: DialogPass?) {
        if (data != null) {
            val interceptData = "当前的Data1:${data?.msg}"
            loge(interceptData)

            if (data.passType == 1) {
                topActivity.showNormalConfirmDialog(content = data.msg, onConfirmBlock = {

                    topActivity.showLoading("延迟模拟网络请求获取下一个弹窗的逻辑").show().delayDismiss(2000)
                    //测试代码随便写的
                    Handler(Looper.getMainLooper()).postDelayed(2500){
                        data.passType = 2
                        super.intercept(data)
                    }
                })
            } else {
                super.intercept(data)
            }

        } else {
            super.intercept(data)
        }
    }
}

class TwoIntercept : InterceptorChain<DialogPass>() {

    override fun intercept(data: DialogPass?) {
        if (data != null) {
            val interceptData = "当前的Data2:${data.msg}"
            loge(interceptData)

            if (data.passType == 2) {
                topActivity.showNormalConfirmDialog(title = "隐私协议标题",content = "隐私协议内容", onConfirmBlock = {
                    data.passType = 3
                    super.intercept(data)
                }, onCancelBlock = {
                    super.intercept(data)
                })
            } else {
                super.intercept(data)
            }

        } else {
            super.intercept(data)
        }
    }
}

class ThreeIntercept : InterceptorChain<DialogPass>() {

    override fun intercept(data: DialogPass?) {
        if (data != null) {
            val interceptData = "当前的Data3:${data.msg}"
            loge(interceptData)

            if (data.passType == 3) {
                topActivity.showNormalListDialog(data = listOf("全部","等待确认", "已完成")){position: Int, text: String ->
                    toast(text)
                    super.intercept(data);
                }
            } else {
                super.intercept(data)
            }

        } else {
            super.intercept(data)
        }
    }
}