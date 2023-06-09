package com.fido.common.common_utils.widgets.motion

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout

/**
@author FiDo
@description:
@date :2023/6/7 15:18
 */
class CollapsibleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
)  :
    MotionLayout(context, attrs, defStyleAttr),AppBarLayout.OnOffsetChangedListener {

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        progress = -verticalOffset / (appBarLayout?.totalScrollRange?.toFloat()?:0f)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        (parent as? AppBarLayout)?.addOnOffsetChangedListener(this)
        /*val viewgroup= (parent as? ViewGroup)
        for (i in 0 until (viewgroup?.childCount?:0)) {
            val child = viewgroup?.getChildAt(i)
            if (child is )
        }*/
    }

}