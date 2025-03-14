package com.fido.common.common_utils.customview.custom.intercept

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import kotlin.math.abs

/**
 * @author: FiDo
 * @date: 2025/3/12
 * @des:
 */
class InterceptLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,def:Int = 0
) : ScrollView(context, attrs,def) {

    private var downY =0f
    private var downX =0f

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView4)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = ev.y
                downX = ev.x
                intercept = super.onInterceptTouchEvent(ev)
            }
            /*MotionEvent.ACTION_MOVE->{
                val moveY = ev.y
                val moveX = ev.x
                val dx = moveX - downX
                val dy = moveY - downY
                if (abs(dx)> abs(dy)){
                    //水平滑动
                    intercept = false
                }else{
                    //垂直滑动
                    if (dy != 0f) {
                        intercept = true
                    }
                    Log.e("FiDo", "onInterceptTouchEvent: 垂直滑动 dy=${dy}" )
                }
            }*/
            MotionEvent.ACTION_MOVE -> {
                val moveY = ev.y
                val moveX = ev.x
                val dx = moveX - downX
                val dy = moveY - downY

                if (abs(dx) > abs(dy)) {
                    // 水平滑动，不拦截
                    intercept = false
                } else {
                    // 垂直滑动，检查 RecyclerView 是否可以滑动
                    if (dy < 0) { // 手指向上滑动
                        if (recyclerView != null && recyclerView.canScrollVertically(1)) {
                            // RecyclerView 还可以继续向上滑，ScrollView 不拦截
                            intercept = false
                        } else {
                            // RecyclerView 到底了，交给 ScrollView 处理
                            intercept = true
                        }
                    } else { // 手指向下滑动
                        if (recyclerView != null && recyclerView.canScrollVertically(-1)) {
                            // RecyclerView 还可以继续向下滑，ScrollView 不拦截
                            intercept = false
                        } else {
                            // RecyclerView 到顶了，交给 ScrollView 处理
                            intercept = true
                        }
                    }
                }
            }
        }
//        return super.onInterceptTouchEvent(ev)
        return intercept
    }

}