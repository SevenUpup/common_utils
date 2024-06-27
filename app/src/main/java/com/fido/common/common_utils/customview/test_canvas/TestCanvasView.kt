package com.fido.common.common_utils.customview.test_canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_utils.widgets.dp

/**
 * @author: FiDo
 * @date: 2024/6/27
 * @des:
 */
class TestCanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            width = paddingLeft + paddingRight + 50.dp
        }
        if (height == 0 || heightMode == MeasureSpec.UNSPECIFIED) {
            height = width
        }
        logd("widthMode =${widthMode} heightMode=${heightMode} width=$width height=$height")

        setMeasuredDimension(width,height)
    }

    override fun onDraw(canvas: Canvas) {
        // 保存初始状态
        canvas.save()

        // 执行一些绘制操作
        canvas.translate(50f, 50f)
        canvas.drawCircle(0f, 0f, 20f, paint)

        // 恢复初始状态
        canvas.restore()

        // 继续其他不受影响的绘制操作
        canvas.drawLine(0f, 0f, 100f, 100f, paint)
    }

}