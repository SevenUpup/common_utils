package com.fido.common.common_base_ui.widget.edittext.block;

import android.graphics.Paint;

/**
 * 边框绘制者
 */
public class StrokeBlockDrawer extends SolidBlockDrawer {


    public StrokeBlockDrawer() {
        super();
    }


    public StrokeBlockDrawer(int blockNormalColor, int blockFocusedColor, int blockErrorColor, int blockShape, int blockLineWidth, int blockCorner) {
        super(blockNormalColor, blockFocusedColor, blockErrorColor, blockShape, blockLineWidth, blockCorner);
    }

    @Override
    protected void initPaint() {
        super.initPaint();
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(blockLineWidth);
    }

}
