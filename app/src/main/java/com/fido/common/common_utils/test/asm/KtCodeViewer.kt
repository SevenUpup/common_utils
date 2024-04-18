package com.fido.common.common_utils.test.asm

import android.view.View
import com.fido.common.common_utils.asm.clickcheck.ClickViewMonitor

/**
 * @author: FiDo
 * @date: 2024/4/18
 * @des:
 */
class KtCodeViewer {

    fun hasReturn(view: View) {
//        println("666")
        if (ClickViewMonitor.enableClick(view)) {
            System.out.println("666");
        }
    }

}