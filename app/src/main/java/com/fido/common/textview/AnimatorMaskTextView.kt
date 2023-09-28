package com.fido.common.textview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
@author FiDo
@description:
@date :2023/9/18 16:06
 */
class AnimatorMaskTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var shader:BitmapShader?=null
    private var shaderMatrix : Matrix = Matrix()
    private var offsetY = 0f

    var maskY = 0f
        set(value){
            field = value
            invalidate()
        }
    var maskX = 0f
        set(value) {
            field = value
            invalidate()
        }

    fun setMaskDrawable(source: Drawable) {
        val maskW: Int = source.intrinsicWidth
        val maskH: Int = source.intrinsicHeight

        val b = Bitmap.createBitmap(maskW, maskH, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)

        c.drawColor(currentTextColor)
        source.setBounds(0, 0, maskW, maskH)
        source.draw(c)

        shader = BitmapShader(b, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        paint.shader = shader
        offsetY = ((height - maskH) / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        shaderMatrix.setTranslate(maskX, offsetY + maskY)
        shader?.setLocalMatrix(shaderMatrix)
        paint.shader = shader
        super.onDraw(canvas)
    }

}

class TextUpOrDownSpan(private val isUp:Boolean, private val offset: Int): CharacterStyle() {

    override fun updateDrawState(tp: TextPaint?) {
        tp?.baselineShift = if(isUp) - offset else offset
    }
}
