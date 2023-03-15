package com.fido.common.common_base_ui.ext.textview

import android.graphics.Color
import android.text.InputType
import com.fido.common.common_base_ui.widget.edittext.CodeEditText
import com.fido.common.common_base_ui.widget.edittext.style.BlockShape
import com.fido.common.common_base_ui.widget.edittext.style.CodeInputType


fun CodeEditText.textMode(
    @BlockShape shape: Int = BlockShape.STROKE,
    @CodeInputType codeInputType: Int = CodeInputType.TEXT,
    blockConer: Int = 5,
    codeMaxLen: Int = 9,
    codeTextColor: Int = Color.BLACK,
    codeTextSize: Int = 15,
    blockNormalColor: Int = codeTextColor,
    blockFocusColor: Int = codeTextColor,
    blockOrLineWidth: Int = 2,
    blockSpace: Int = 15,
    isShowCursor: Boolean = true,
    cursorDuration: Int = 500,
    cursorWidth: Int = 4,
    cursorColor: Int = codeTextColor,
    editInputType: Int = InputType.TYPE_CLASS_TEXT,
) = apply {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.blockShape = shape
    this.setBlockCorner(dp2px(blockConer.toFloat()))
    this.blockNormalColor = blockNormalColor
    this.blockFocusColor = blockFocusColor
    this.blockLineWidth = dp2px(blockOrLineWidth.toFloat())
    this.blockSpace = dp2px(blockSpace.toFloat())
    this.codeInputType = codeInputType
    this.maxCodeLength = codeMaxLen
    this.codeTextColor = codeTextColor
    this.codeTextSize = sp2px(codeTextSize.toFloat())
    this.isShowCursor = isShowCursor
    this.cursorDuration = cursorDuration
    this.cursorWidth = dp2px(cursorWidth.toFloat())
    this.cursorColor = cursorColor
    this.inputType = editInputType
}


fun CodeEditText.passwordMode(
    @BlockShape shape: Int = BlockShape.SOLID,
    @CodeInputType codeInputType: Int = CodeInputType.PASSWORD,
    dotRadius: Int = 8,
    blockConer: Int = 5,
    codeMaxLen: Int = 6,
    codeTextColor: Int = Color.WHITE,
    codeTextSize: Int = 15,
    blockNormalColor: Int = Color.BLACK,
    blockFocusColor: Int = Color.YELLOW,
    blockOrLineWidth: Int = 2,
    blockSpace: Int = 15,
    isShowCursor: Boolean = true,
    cursorDuration: Int = 500,
    cursorWidth: Int = 5,
    cursorColor: Int = codeTextColor,
    editInputType: Int = InputType.TYPE_CLASS_NUMBER,
) = apply {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.dotRadius = dp2px(dotRadius.toFloat())
    this.blockShape = shape
    this.setBlockCorner(dp2px(blockConer.toFloat()))
    this.blockNormalColor = blockNormalColor
    this.blockFocusColor = blockFocusColor
    this.blockLineWidth = dp2px(blockOrLineWidth.toFloat())
    this.blockSpace = dp2px(blockSpace.toFloat())
    this.codeInputType = codeInputType
    this.maxCodeLength = codeMaxLen
    this.codeTextColor = codeTextColor
    this.codeTextSize = sp2px(codeTextSize.toFloat())
    this.isShowCursor = isShowCursor
    this.cursorDuration = cursorDuration
    this.cursorWidth = dp2px(cursorWidth.toFloat())
    this.cursorColor = cursorColor
    this.inputType = editInputType
}