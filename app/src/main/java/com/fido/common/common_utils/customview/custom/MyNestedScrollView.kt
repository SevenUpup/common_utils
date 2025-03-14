package com.fido.common.common_utils.customview.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_util.ext.children

/**
 * @author: FiDo
 * @date: 2025/3/12
 * @des:  解决子组件是Rv 导致滑动冲突的问题，
 */
class MyNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0,
) : NestedScrollView(context, attrs,defStyleAttr) {

    //找到里面的Rv
    private val mRv by lazy {
        (getChildAt(0) as? ViewGroup)?.children?.find { it is RecyclerView } as? RecyclerView
    }

    private var rvFirstVisibleItemPosition = 0
    private var rvLastVisibleItemPosition = 0
    private var rvItemCount = 0

    init {
        mRv?.also {
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = it.layoutManager as LinearLayoutManager
                    rvFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    rvLastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    rvItemCount = layoutManager.itemCount
                }
            })
        }

    }

    private var downY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val y = ev.y
        var intercept = false
        when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                downY = ev.y
                intercept = super.onInterceptTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE ->{
                if (rvFirstVisibleItemPosition == 0 && y > downY){  //下滑
                    intercept = true
                }
                if (rvLastVisibleItemPosition == rvItemCount -1 && y < downY){ //上滑
                    intercept = true
                }
            }
            MotionEvent.ACTION_UP ->{
                intercept = true
            }
        }
//        return super.onInterceptTouchEvent(ev)
        return intercept
    }

}