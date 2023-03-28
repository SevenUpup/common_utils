package com.fido.common.common_base_ui.widget.edittext.cusor;

import android.graphics.drawable.ShapeDrawable;

/**
 * 光标
 */
public class CursorDrawable extends ShapeDrawable {
    private int mHeight;

    public CursorDrawable(int cursorColor, int cursorWidth, int cursorHeight) {
        mHeight = cursorHeight;
        setDither(false);
        getPaint().setColor(cursorColor);
        setIntrinsicWidth(cursorWidth);
    }

    public void setBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.setBounds(paramInt1, paramInt2, paramInt3, this.mHeight + paramInt2);
    }

}
