package com.fido.common.common_utils.asm.toast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;

/**
 * @author: FiDo
 * @date: 2024/4/25
 * @des:
 */
public class OpenAppMarketUtils {

    public static void openAppMarket(Context context, String pkgName) {
        String uriString = "";
        String marketPkg = "";
        try {
            String lowerCase = Build.BRAND.toLowerCase(Locale.ROOT);
            if (lowerCase.equals("huawei") || lowerCase.equals("honor")) {
                uriString = "appmarket://details?id=";
                marketPkg = "com.huawei.appmarket";
            }
//            if (lowerCase.equals("honor")) {
//                marketPkg = "com.hihonor.appmarket";
//            }
            if(TextUtils.isEmpty(uriString)) {
                uriString = "market://details?id=";
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uriString + pkgName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            try {
                context.startActivity(intent);
            } catch (Exception unused) {
            }
        }catch (Exception e){

        }
    }

}
