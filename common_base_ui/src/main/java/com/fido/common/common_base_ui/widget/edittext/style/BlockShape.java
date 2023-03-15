package com.fido.common.common_base_ui.widget.edittext.style;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lwj on 2019/1/17.
 * lwjfork@gmail.com
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({BlockShape.STROKE, BlockShape.SOLID, BlockShape.UNDERLINE, BlockShape.NONE})
public @interface BlockShape {
    int STROKE = 1; // 边框
    int SOLID = 2;  // 填充
    int UNDERLINE = 3; // 下划线
    int NONE = -1;  // 什么都不画
}