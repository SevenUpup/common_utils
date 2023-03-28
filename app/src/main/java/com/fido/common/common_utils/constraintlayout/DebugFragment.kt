package com.fido.common.common_utils.constraintlayout

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.drake.debugkit.dev
import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.runMain
import com.fido.common.common_base_util.util.ShellUtils
import com.fido.common.common_utils.R
import com.lxj.xpopup.util.XPopupUtils.getScreenHeight
import com.lxj.xpopup.util.XPopupUtils.getScreenWidth


class DebugFragment : Fragment(R.layout.fragment_debug), View.OnTouchListener {

    private lateinit var container: ConstraintLayout
    private lateinit var mRv: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        mRv = view.findViewById(R.id.mRv)
//        mRv.vertical()
//            .bindData(listOf("1", "2", "3", "4"), R.layout.item_rv_text) { holder, position, item ->
//                holder.setText(R.id.mTitle, "$item ->$position")
//            }



        view.setOnTouchListener(this)
    }

    private var dx = 0f
    private var dy = 0f

    private var lastX = 0f
    private var lastY = 0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val temp = view ?: return false
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN->{
                dx = temp.x - event.rawX
                dy = temp.y - event.rawY

                lastX = x.toFloat()
                lastY = y.toFloat()
            }
            MotionEvent.ACTION_MOVE->{
//                temp.x = event.rawX + dy
//                temp.y = event.rawY + dy

                val offsetX = (x - lastX).toInt()
                val offsetY = (y - lastY).toInt()
                var left: Int = temp.getLeft() + offsetX
                var top: Int = temp?.getTop() + offsetY
                var right: Int = temp?.getRight() + offsetX
                var bottom: Int = temp?.getBottom() + offsetY
                if (left < 0) {
                    left = 0
                    right = left + temp!!.width
                }
                if (right > getScreenWidth()) {
                    right = getScreenWidth()
                    left = right - temp?.width!!
                }
                if (top < 0) {
                    top = 0
                    bottom = top + temp.height
                }
                if (bottom > getScreenHeight()) {
                    bottom = getScreenHeight()
                    top = bottom - temp.height
                }
                temp.layout(left, top, right, bottom)
                lastX = x.toFloat()
                lastY = y.toFloat()
            }

        }
        return true
    }

    private fun getScreenWidth(): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

}