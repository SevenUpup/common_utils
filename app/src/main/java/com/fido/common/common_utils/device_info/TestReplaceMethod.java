package com.fido.common.common_utils.device_info;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.fido.annotation.asm.AsmMethodOpcodes;
import com.fido.annotation.asm.AsmMethodReplace;
import com.fido.common.common_base_util.ext.ToastExtKt;
import com.fido.common.common_utils.asm.AsmHookActivity;
import com.fido.common.common_utils.asm.KtAsmToast;

/**
 * @author: FiDo
 * @date: 2024/5/24
 * @des:
 */
public class TestReplaceMethod {

    @AsmMethodReplace(oriClass = DeviceInfoAc.class,oriAccess = AsmMethodOpcodes.INVOKEVIRTUAL)
    public static void showCustomToast2(String text){
        ToastExtKt.toast(TestReplaceMethod.class.getSimpleName() + "通过AsmMethodReplace 替换方法");
    }

    // 替换Kotlin中的Companion静态方法，修饰符需是public 且oriClass是x.Companion.class
    @AsmMethodReplace(oriClass = AsmHookActivity.Companion.class,oriAccess = AsmMethodOpcodes.INVOKESPECIAL)
    public static void showCustomPop(Context context){
        ToastExtKt.toast("但是我通过asm修改成吐司啦");
    }

    /**
     * 替换类中调用的静态方法，改为目标类的静态方法
     */
    @AsmMethodReplace(oriClass = KtAsmToast.class,oriAccess = AsmMethodOpcodes.INVOKESTATIC)
    public static void showAsmCustomToast(Context context,String text){
        new AlertDialog.Builder(context).setMessage(text).show();
    }

}
