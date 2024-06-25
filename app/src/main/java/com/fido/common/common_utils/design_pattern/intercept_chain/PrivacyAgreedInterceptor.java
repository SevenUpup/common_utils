package com.fido.common.common_utils.design_pattern.intercept_chain;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:
 *
 * //是否同意全部权限 -> true 立即获取验证码? ->
 *         //                 false -> 是否同意部分权限 ——> true 进入app首页
 *                                         //             false exit
 */
public class PrivacyAgreedInterceptor implements Intercept{
    private PrivacyInterceptBean interceptBean;

    public PrivacyAgreedInterceptor(PrivacyInterceptBean interceptBean) {
        this.interceptBean = interceptBean;
    }

    @Override
    public void intercept(@NonNull InterceptChain chain) {
        if (interceptBean.getShowFullPrivacy()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(chain.getContext());
            builder.setMessage("是否同意全部隐私");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interceptBean.setAgreedFull(true);
                    chain.process();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interceptBean.setShowPartPrivacy(true);
                    chain.process();
                }
            });
            builder.create().show();
        }
    }
}
