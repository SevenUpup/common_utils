package com.fido.common.common_utils.customview.custom

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.fido.common.R
import com.fido.common.common_base_util.ext.loge

/**
 * @author: FiDo
 * @date: 2025/3/4
 * @des:
 */
class TestToggleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mSlideMarginLeft = 0f

    private val bgBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.bg_msg_bubble_left)
    }

    private val slideBitmap by lazy {
        BitmapFactory.decodeResource(resources,R.drawable.bg_date_label)
    }

    init {

        setOnClickListener {
            if (mSlideMarginLeft == 0f) {
                mSlideMarginLeft =  (bgBitmap.width - slideBitmap.width).toFloat()
            }else{
                mSlideMarginLeft = 0f
            }
            invalidate()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        var height = 0
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val measureWidth  = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val measureHeight  = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            //有确切宽度 如 =50dp or match parent
            width = measureWidth
        }else{
            width = bgBitmap.width
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = measureHeight
        }else{
            height = bgBitmap.height
        }
        setMeasuredDimension(width,height)

        loge("width=${measureWidth} height = $measureHeight")
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 先画背景图
        canvas.drawBitmap(bgBitmap,0f + paddingLeft,0f,paint)
        //再画滑块,用mSlideMarginLeft来控制滑块距离左边的距离。
        canvas.drawBitmap(slideBitmap,mSlideMarginLeft + paddingLeft,0f,paint)
    }

}