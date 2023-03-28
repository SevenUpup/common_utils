package com.fido.common.common_base_ui.widget.edittext.text;

import android.graphics.Paint;
import android.graphics.Rect;

import com.fido.common.common_base_ui.widget.edittext.style.CodeInputType;

/**
 * 明文绘制
 */
public class TextDrawer extends BaseTextDrawer {
    private int textBaseLineY;
    public TextDrawer(@CodeInputType int codeInputType, int codeTextColor, int codeTextSize, int dotRadius) {
        super(codeInputType, codeTextColor, codeTextSize, dotRadius);
    }

    @Override
    protected void drawText(Rect rect, char c) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;// 基线到字体上边框的距离
        float bottom = fontMetrics.bottom;// 基线到字体下边框的距离
        textBaseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        canvas.drawText(c + "", rect.centerX(), textBaseLineY, textPaint);
    }
}
