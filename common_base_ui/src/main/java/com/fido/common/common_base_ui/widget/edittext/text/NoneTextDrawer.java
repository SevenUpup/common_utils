package com.fido.common.common_base_ui.widget.edittext.text;

import android.graphics.Rect;

/**
 * 什么都不绘制
 */
public class NoneTextDrawer extends BaseTextDrawer {


    public NoneTextDrawer(int codeInputType, int codeTextColor, int codeTextSize, int dotRadius) {
        super(codeInputType, codeTextColor, codeTextSize, dotRadius);
    }

    @Override
    protected void drawText(Rect rect, char c) {

    }
}
