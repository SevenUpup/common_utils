package com.fido.common.common_base_ui.widget.edittext.block;

import android.graphics.RectF;

/**
 * 任性，就是什么都不画
 */
public class NoneBlockDrawer extends BaseBlockDrawer {

    public NoneBlockDrawer(int blockNormalColor, int blockFocusedColor, int blockErrorColor, int blockShape, int blockLineWidth, int blockCorner) {
        super(blockNormalColor, blockFocusedColor, blockErrorColor, blockShape, blockLineWidth, blockCorner);
    }

    @Override
    protected void drawFocusedBlock(RectF rectF) {

    }

    @Override
    protected void drawNormalBlock(RectF rectF) {

    }

    @Override
    protected void drawErrorBlock(RectF rectF) {

    }
}
