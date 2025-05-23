package com.fido.common.common_utils.customview

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.horizontal
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_ui.util.dp
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.children
import com.fido.common.common_base_util.ext.setDrawable
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.ext.widthAndHeight
import com.fido.common.common_base_util.getColor
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_utils.rv.bindData
import com.fido.common.common_utils.rv.setText
import com.fido.common.databinding.AcSlidingConflictBinding


/**
 * @author: FiDo
 * @date: 2025/3/12
 * @des:  滑动冲突Ac
 *
 * 滑动冲突总结：
 *                  fun dispatchTouchEvent(ev: MotionEvent): Boolean{
 *                      var consume = false
 *                      if(onInterceptToucheEvent(ev)){
 *                          consume = onTouchEvent(ev)
 *                      }else{
 *                          consume = child.dispatchTouchEvent(ev)
 *                      }
 *                      return consume
 *                  }
 *
 */
class SlidingConflictAc:AppCompatActivity() {

    private val binding:AcSlidingConflictBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private val data = Array(10){it.toString()}.toList()

    //返回true Activity将所有事件拦截,就玩不了哦
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        Log.d("SlidingConflictAc", "SlidingConflictAc dispatchTouchEvent: action=>${ev.action}")
//        return true
//    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.d("SlidingConflictAc", "SlidingConflictAc onTouchEvent: action=>${event.action}")
//        return true
//    }

    private fun initView() {
        binding.apply {
            btDragMap.throttleClick {
                startActivity<MapDragAc>()
            }

            dragContainer.setUp(
                initHintText = {
                    setDrawable(leftDrawable = R.mipmap.back_black)
                    setTextShadowBgColor(R.color.colorBgBtnText.getColor)
                }
            ) {
                toast("进入详情页面")
            }
            rvDragLookMore.horizontal()
                .bindData(data, R.layout.item_rv_img) { holder, position, item ->
                    holder.itemView.apply {
                        widthAndHeight(80.dp,100.dp)
                        findViewById<ImageView>(R.id.mIv).setImageResource(R.mipmap.flower)
                    }
                }

            rv1.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            rv2.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            mScrollView.setRecyclerView(rv3)
            rv3.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }

            recyclerView4.vertical()
                .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                    holder.setText(R.id.mTitle, item)
                }


            container.children.forEach {
                if (it is RecyclerView) {
                    (it as RecyclerView).vertical()
                        .bindData(data, R.layout.item_rv_text) { holder, position, item ->
                            holder.setText(R.id.mTitle, item)
                        }
                }
            }
        }
    }

}

