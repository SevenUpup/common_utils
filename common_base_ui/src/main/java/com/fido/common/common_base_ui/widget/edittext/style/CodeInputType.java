package com.fido.common.common_base_ui.widget.edittext.style;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef({CodeInputType.PASSWORD, CodeInputType.TEXT, CodeInputType.NONE})
public @interface CodeInputType {
    int PASSWORD = 1;  // 密码样式
    int TEXT = 2;  // 明文
    int NONE = -1; // 什么都不画
}
