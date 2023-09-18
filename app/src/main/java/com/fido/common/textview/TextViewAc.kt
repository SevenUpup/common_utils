package com.fido.common.textview

import android.os.Bundle
import android.text.TextPaint
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.common_utils.databinding.AcTextViewBinding


/**
@author FiDo
@description:
@date :2023/9/15 14:37
 */
class TextViewAc:AppCompatActivity() {

    val binding:AcTextViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         *  1. autoSizeTextType：设置 TextView 是否支持自动改变文本大小，none 表示不支持，uniform 表示支持。
            2. autoSizeMinTextSize：最小文字大小，例如设置为10sp，表示文字最多只能缩小到10sp。
            3. autoSizeMaxTextSize：最大文字大小，例如设置为18sp，表示文字最多只能放大到18sp。
            4. autoSizeStepGranularity：缩放粒度，即每次文字大小变化的数值，例如设置为1sp，表示每次缩小或放大的值为1sp。
         */
        TextViewCompat.setAutoSizeTextTypeWithDefaults(binding.tvSizeDynamic2,TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(binding.tvSizeDynamic2,10,18,1, TypedValue.COMPLEX_UNIT_SP)

        adjustTvTextSize(binding.tvSizeDynamic2,getScreenWidthPx(),binding.tvSizeDynamic2.text.toString())

    }


    private fun adjustTvTextSize(tv: TextView, maxWidth: Int, text: String) {
        val avaiWidth = maxWidth - tv.paddingLeft - tv.paddingRight
        if (avaiWidth <= 0) {
            return
        }
        val textPaintClone = TextPaint(tv.paint)
        var trySize = textPaintClone.textSize
        while (textPaintClone.measureText(text) > avaiWidth) {
            trySize--
            textPaintClone.textSize = trySize
        }
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize)
    }

}