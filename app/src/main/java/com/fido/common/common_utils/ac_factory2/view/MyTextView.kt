package com.fido.common.common_utils.ac_factory2.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.fido.common.R
import com.fido.common.common_base_util.getColor

/**
 * @author: FiDo
 * @date: 2024/10/8
 * @des:
 */
class MyTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    
    init {
        textSize = 25f
        setTextColor(R.color.purple_200.getColor)
    }

}