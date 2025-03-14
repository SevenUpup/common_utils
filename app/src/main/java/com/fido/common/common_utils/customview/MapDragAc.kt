package com.fido.common.common_utils.customview

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.isVisible
import com.fido.common.databinding.AcMapDragBinding

/**
 * @author: FiDo
 * @date: 2025/3/14
 * @des:  仿 mapView + content 拖拽效果
 */
class MapDragAc:AppCompatActivity() {

    private val binding:AcMapDragBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {

            btShow.throttleClick {
                scrollView.isVisible = true
            }

            btHide.throttleClick {
                scrollView.isVisible = false
            }

            //将透明控件的事件 分发给mapView
            transparentView.dispatchEvent = { motionEvent->
                //将事件分发给MapView
                map.dispatchTouchEvent(motionEvent)
            }

            //在滑动事件中判断当前事件是否自己处理（通过点击区域判断，在透明控件区域内就不拦截把事件交给子view自己处理，让他去分发给mapview）
            scrollView.interceptEvent = { motionEvent->
                val rect = Rect()
                transparentView.getLocalVisibleRect(rect)
                val isContains = rect.contains(motionEvent.x.toInt(), motionEvent.y.toInt())
                Log.d("FiDo", "点击坐标是否在透明控件上: $isContains")
                !isContains
            }

        }
    }
}

class TransparentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var dispatchEvent:((MotionEvent)->Boolean)?=null

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (dispatchEvent != null) {
            Log.d("FiDo", "TransparentView dispatchTouchEvent: ${event.action}")
            return dispatchEvent!!(event)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("FiDo", "TransparentView onTouchEvent: ${event.action}")
        return super.onTouchEvent(event)
    }

}

class MyNestedScrollView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,def:Int = 0
) : NestedScrollView(context, attrs,def) {

    var interceptEvent:((MotionEvent)->Boolean)?=null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (interceptEvent != null) {
            Log.d("FiDo", "MyNestedScrollView2 onInterceptTouchEvent: ${ev.action}")
            return interceptEvent!!(ev)
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("FiDo", "MyNestedScrollView2 onTouchEvent: ${ev.action}")
        return super.onTouchEvent(ev)
    }

}
