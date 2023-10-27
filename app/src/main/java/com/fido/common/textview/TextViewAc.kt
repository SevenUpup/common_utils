package com.fido.common.textview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.sp
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.adjustTextSize
import com.fido.common.common_base_util.ext.isFakeBoldText
import com.fido.common.common_base_util.getDrawable
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.common_utils.R
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

        binding.tvSizeDynamic2.apply {
            adjustTextSize(getScreenWidthPx(), text = text.toString())
        }
        binding.maskTextView.text = getSpanStr()
        binding.maskTextView.textSize = 36.sp.toFloat()
        binding.maskTextView.isFakeBoldText = true

        binding.maskTextView.throttleClick {
            binding.maskTextView.setMaskDrawable(R.drawable.layered_waves_haikei.getDrawable)
            val maskXAnimator: ObjectAnimator =
                ObjectAnimator.ofFloat(binding.maskTextView, "maskX", 0f, binding.maskTextView.width.toFloat())
            val maskYAnimator: ObjectAnimator =
                ObjectAnimator.ofFloat(binding.maskTextView, "maskY", 0f, (-binding.maskTextView.getHeight()).toFloat())
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(maskXAnimator, maskYAnimator)
            animatorSet.start()
        }

    }

    private fun getSpanStr(): SpannableString? {
        val string = SpannableString("宁静致远")
        val offset = 20
        val firstSpan = TextUpOrDownSpan(true,offset)
        string.apply {
            setSpan(firstSpan,0,1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(TextUpOrDownSpan(false,offset),1,2,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(TextUpOrDownSpan(true,offset),2,3,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(TextUpOrDownSpan(false,offset),3,4,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return string
    }

}