package com.fido.common.common_utils.customview.fake_taobao

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.horizontal
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.gradientColorBgAnim
import com.fido.common.common_base_util.ext.height
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.margin
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.ext.widthAndHeight
import com.fido.common.common_base_util.getColor
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.databinding.AcFakeTaobaoBinding
import com.fido.kingkongview.KingKongStyle
import com.fido.kingkongview.KingKongView
import com.fido.kingkongview.utils.easyKHolderCreator
import kotlin.math.abs
import kotlin.math.max


/**
 * @author: FiDo
 * @date: 2024/7/19
 * @des:
 */
class FakeTaobaoKingkongViewAc : AppCompatActivity() {

    private val binding: AcFakeTaobaoBinding by binding()
    private var list  = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (i in (0 until 15)) {
            list.add("test${i}")
        }

        val kkView = binding?.kkView!!
        initKingKongView(kkView)

        binding?.tvGradientColor?.apply {
            gradientColorBgAnim(
                Color.parseColor("#9932CC"),
                Color.parseColor("#FF69B4"),
                radius = 15.dp.toFloat(),
                autoStart = false,
                infinite = true
            ){valueAnim->
                click {
                    valueAnim?.start()
                }
            }
        }

        val allDataSize = maxSpan * visibleCount * 2
        if (list.size < allDataSize) {
//            for (i in list.size until allDataSize) {
//                list.add((visibleCount),null)
//            }
            for (i in 0  until (maxSpan - 1) * visibleCount) {
                list.add((visibleCount),null)
            }
        }
        loge("allDataSize=${list} size = ${list.size}")
       /*val indexNeedChangeList =  mutableListOf<Int>()
        list.forEachIndexed { index, s ->
            if (index >= allDataSize / 2 && s == null) {
                indexNeedChangeList.add(index)
            }
        }
        if (indexNeedChangeList.isNotEmpty()) {
            indexNeedChangeList.forEach {
                list.add(null)
                list.removeAt(it)
            }
            repeat(indexNeedChangeList.size) {

            }
        }*/

//        val newList2 = java.util.ArrayList<String>(list)
        val newList = rearrange(list,maxSpan,visibleCount).toMutableList()
        loge("newList=${newList} size=${newList.size}")
//        val transformList = HandleDataUtils.transformList(newList)
//        loge("newList2 = $newList2 \n==> newList=${newList}  \ngroupListIndexByMod=${HandleDataUtils.groupListIndexByMod(newList,maxSpan)}" +
//                " \ngroupListByMod=${HandleDataUtils.groupListByMod(newList,maxSpan)}")

        binding?.apply {
            val mRv = binding?.mRv!!
            mRv.horizontal(maxSpan)
            mAdapter = MyAdapter(newList){holder, position ->
//                if (position % maxSpan != 0  && position < (maxSpan*visibleCount)) {
//                    holder.itemView.widthAndHeight(0,0)
//                }else{
//                    holder.itemView.widthAndHeight(itemWidth,itemHeight)
//                }
                holder.itemView.widthAndHeight(itemWidth,itemHeight)
                holder.setText(newList[position].toString())
            }
            mRv.adapter = mAdapter

            //监听View的视图树变化,防止初始化未获取到滚动条比例
            binding?.mRv?.getViewTreeObserver()
                ?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        binding?.mRv?.getViewTreeObserver()?.removeOnPreDrawListener(this)
//                        computeScrollScale()
                        logd("visibleCount=${getScreenWidthPx()/itemWidth} Rv实际宽度=${binding?.mRv?.computeHorizontalScrollRange()}")
                        return true
                    }
                })

//            mRv.addItemDecoration(SpacesItemDecoration())

            binding?.mRv?.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        val lastPos = layoutManager.findLastVisibleItemPosition()

                        val scrollX = recyclerView.computeHorizontalScrollOffset()
                        val maxScrollX = recyclerView.computeHorizontalScrollRange() - recyclerView.width
                        val scrollPercent = scrollX.toFloat() / maxScrollX.toFloat()

                        if (scrollPercent > 0.33) {
                            recyclerView.smoothScrollToPosition(newList.size-1)
                        } else {
                            recyclerView.smoothScrollToPosition(0)
                        }
                    }
                    logd("newState = ${newState}")

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // 累加水平滑动距离
                    // 根据滑动距离动态调整列数
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPos = layoutManager.findLastVisibleItemPosition()
                    adjustSpanCount()
                    Log.e("FiDo", "onScrolled dx: $dx  itemWidth =${itemWidth} itemMargin=${itemMargin} lastMargin=${lastMargin}")
                    for (position in firstVisibleItemPosition..lastPos) {
                        val itemView = layoutManager?.findViewByPosition(position)
                        if (itemView != null) {
                            val margin = max(lastMargin,itemMargin)
                            if (itemMargin > lastMargin) {
                                lastMargin = itemMargin
                            }
                            if (lastMargin == itemMargin) {
                                lastMargin = 0f
                            }
                            itemView.margin(leftMargin = margin.toInt()/3*2,rightMargin = margin.toInt()/3)
//                            itemView.margin(leftMargin = margin.toInt())
//                            itemView.setPadding(margin.toInt()/2,0,margin.toInt()/2,0)
                        }
                    }
                    /*if (recyclerView.scrollPercent > 0.33) {

                        for (position in firstVisibleItemPosition..lastPos) {
                            val itemView = layoutManager?.findViewByPosition(position)
                            if (itemView != null) {
                                val margin = max(lastMargin,itemMargin)
                                if (itemMargin > lastMargin) {
                                    lastMargin = itemMargin
                                }
                                if (lastMargin == itemMargin) {
                                    lastMargin = 0f
                                    itemMargin = 0f
                                }
                                itemView.margin(rightMargin = margin.toInt())
                            }
                        }
                    }else{
                        val scrollX = recyclerView.computeHorizontalScrollOffset()
                        for (position in firstVisibleItemPosition..lastPos) {
                            val itemView = layoutManager?.findViewByPosition(position)
                            if (itemView != null) {
//                                val margin = max(lastMargin,itemMargin)
//                                if (itemMargin > lastMargin) {
//                                    lastMargin = itemMargin
//                                }
//                                itemView.margin(rightMargin = if (scrollX == 0) 0 else margin.toInt())
                                itemView.margin(rightMargin = 0)
                            }
                        }
                    }*/
                }
            })

            initRv2(binding?.mRv2!!)

        }
    }

    private fun initKingKongView(kkView: KingKongView<Any?>) {
        kkView.setUp(list, style = KingKongStyle.FAKE_ALI,3,4,0,{data, pos ->
            toast("pos=${pos} data=${data}")
        }, holderCreator = easyKHolderCreator(R.layout.item_rv_text_img, onBind = {itemView, holder, data, position ->
            itemView.findViewById<TextView>(R.id.mTv).text = data.toString()
        }))
        kkView.setupIndicator(
            style = KingKongStyle.FAKE_ALI,
            indicatorWidth = 10.dp,
            indicatorHeight = 5.dp,
            indicatorBgColor = R.color.colorBgBtnNormal.getColor,
            indicatorThumbColor = R.color.purple_200.getColor,
        )
    }

    private var lastMargin = 0f

    private lateinit var mAdapter2:MyAdapter
    private var offScreenWidth = 100
    private var maxSpan = 4
    private val itemHeight = 88.dp
    private val visibleCount = 4
    private val itemWidth by lazy {
        app.getScreenWidthPx()/visibleCount - (if (offScreenWidth>0) offScreenWidth/visibleCount else 0)
    }
    private lateinit var mAdapter:MyAdapter
    private fun initRv2(mRv2: RecyclerView) {

        val newList = rearrange(list,maxSpan,visibleCount).toMutableList()
        val groupList = HandleDataUtils.groupListIndexByMod(list,maxSpan)
        logd("rearrange = ${newList} \n${groupList}")

        mRv2.horizontal(maxSpan)
        mAdapter2 = MyAdapter(list){holder, position ->
            holder.itemView.widthAndHeight(itemWidth,itemHeight)
//            var text = ""
//            if (position>=(visibleCount*maxSpan)){
//                text = list[position-(visibleCount*(maxSpan-1))].toString()
//            }else{
//                text = newList[position].toString()
//            }
            holder.setText(list[position].toString())
        }
        mRv2.adapter=  mAdapter2
        // 添加滚动监听器
        mRv2.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    val scrollX = recyclerView.computeHorizontalScrollOffset()
                    val maxScrollX = recyclerView.computeHorizontalScrollRange() - recyclerView.width
                    val scrollPercent = scrollX.toFloat() / maxScrollX.toFloat()

                    if (scrollPercent > 0.33) {
                        recyclerView.smoothScrollToPosition(list.size-1)
                    } else {
                        recyclerView.smoothScrollToPosition(0)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 获取当前可见的第一个和最后一个 Item 的位置
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // 计算需要动态改变高度的 Item 的范围
//                val startChangePosition = firstVisibleItemPosition
//                val endChangePosition = lastVisibleItemPosition

                val startChangePosition = 0
                val endChangePosition = list.size-1

                val scrollX = recyclerView.computeHorizontalScrollOffset()
                logd("firstVisibleItemPosition=${firstVisibleItemPosition} lastVisibleItemPosition=${lastVisibleItemPosition} scrollX=${scrollX}")

                // 根据滚动距离动态调整指定 Item 的高度
                /*for (position in startChangePosition..endChangePosition) {
                    val itemView = layoutManager.findViewByPosition(position)
                    if (itemView != null) {
                        var mHeight = itemHeight
                        if (scrollX > 0 && position % maxSpan != 0) {
                            mHeight = if (position<(visibleCount*maxSpan)) 0 else calculateItemHeightBasedOnScroll(itemView, recyclerView)
                        }
//                        if (scrollX == 0 && position % maxSpan != 0 && position < (visibleCount * maxSpan)) {
                        if (scrollX == 0 && position % maxSpan != 0 ) {
                            mHeight = 0
                        }
//                        val itemHeight = if ((position+1) %2 ==0 ) 0 else (if (scrollX>0) calculateItemHeightBasedOnScroll(itemView, recyclerView) else itemHeight)
                        itemView.layoutParams.height = mHeight
//                        itemView.layoutParams.width = itemWidth + itemMargin.toInt()
//                        itemView.margin(leftMargin = itemMargin.toInt()/2, rightMargin = itemMargin.toInt()/2)
//                        val horizontalPadding = itemMargin.toInt()/2
//                        itemView.setPadding(horizontalPadding,itemView.paddingTop,horizontalPadding,itemView.paddingBottom)
                        //设置marginLeft会导致rv滑动不到最左边导致scrollPercent!=1
                        itemView.margin(rightMargin = itemMargin.toInt())
//                        itemView.requestLayout() // 刷新布局
                        binding?.llRv2?.height( itemHeight * (maxSpan-1) + mHeight)
                    }
                }*/

                val scrollPercent = recyclerView.scrollPercent
                if (scrollX == 0) {
                    for (position in startChangePosition..endChangePosition) {
                        var mHeight = 0
                        val itemView = layoutManager.findViewByPosition(position)
                        if (itemView != null) {
                            if (groupList[0].contains(position)) {
                                mHeight = itemHeight
                            }
                            resetItemViewHeight(itemView,mHeight)
                            binding?.llRv2?.height(itemHeight)
//                            resetRvHeight(itemHeight)
                        }
                    }
                }else{
                    //滑动了
                    if (scrollPercent < 0.5) {
                        //中间层pos数组下标
                        val middleListPos = groupList.size / 2
                        Log.d("Fake", "middleListPos:${middleListPos} scrollPercent=${scrollPercent}")
                        groupList.forEachIndexed { index, groupPoitions ->
                            if (index > 0) {
                                if (index > middleListPos) {
                                    groupPoitions.forEach {
                                        Log.d("Fake", "将${it} 位置的高度置为0")
                                        val itemView = layoutManager.findViewByPosition(it)
                                        val dynamicHeight= calculateItemHeightBasedOnScroll(itemView, recyclerView)
                                        resetItemViewHeight(itemView, 0)
                                        resetRvHeight(itemHeight)
                                    }
                                } else {
                                    //需要动态调整高度的item
                                    groupPoitions.forEach {
                                        val itemView = layoutManager.findViewByPosition(it)
                                        val dynamicHeight= calculateItemHeightBasedOnScroll(itemView, recyclerView)
                                        Log.d("Fake", "开始调整${it} 位置的高度 = ${dynamicHeight}")
                                        resetItemViewHeight(itemView, (dynamicHeight/scrollPercent).toInt())
//                                        resetRvHeight(itemHeight + dynamicHeight)
                                    }
                                }
                            }
                        }
                    } else {
                        val middleListPos = groupList.size / 2
                        groupList.forEachIndexed { index, groupPoitions ->
//                            if (index > 0 && index>middleListPos) {
                            if (index > 0 ) {
                                if (index > middleListPos) {
                                    //需要动态调整高度的item
                                    groupPoitions.forEach {
                                        val itemView = layoutManager.findViewByPosition(it)
                                        val dynamicHeight = calculateItemHeightBasedOnScroll(itemView, recyclerView)
                                        Log.d("king", "超过半屏 开始调整${it} 位置的高度 =${dynamicHeight}")
                                        resetItemViewHeight(itemView,dynamicHeight)
                                        resetRvHeight(itemHeight*index + dynamicHeight)
                                    }
                                }else{
                                    groupPoitions.forEach {
                                        val itemView = layoutManager.findViewByPosition(it)
//                                        val dynamicHeight = calculateItemHeightBasedOnScroll(itemView, recyclerView)
                                        val dynamicHeight = itemHeight
//                                        Log.d("king", "超过半屏 开始调整${it} 位置的高度 =${dynamicHeight}")
                                        resetItemViewHeight(itemView,dynamicHeight)
//                                        resetRvHeight(itemHeight*index + dynamicHeight)
                                    }
                                }
                            }
                        }
                    }
                }

            }
        })
    }

    private fun resetItemViewHeight(itemView: View?, height: Int){
        if (itemView == null) return
        itemView.layoutParams.height = height
        //设置marginLeft会导致rv滑动不到最左边导致scrollPercent!=1
        itemView.margin(rightMargin = itemMargin.toInt())
        itemView.requestLayout()
    }

    private fun resetRvHeight(containerHeight:Int){
//        binding?.llRv2?.height(containerHeight)
    }

    private val RecyclerView.scrollPercent:Float
        get() {
            val scrollX = computeHorizontalScrollOffset()
            val maxScrollX = computeHorizontalScrollRange() - width
            return scrollX.toFloat() / maxScrollX.toFloat()
        }

    private var itemMargin = 0f
    // 根据滚动距离计算 Item 的高度
    private fun calculateItemHeightBasedOnScroll(itemView: View?, recyclerView: RecyclerView): Int {
        // 假设根据 RecyclerView 的滚动距离来计算 Item 的高度
        val scrollX = recyclerView.computeHorizontalScrollOffset()
        val maxScrollX = recyclerView.computeHorizontalScrollRange() - recyclerView.width
        val scrollPercent = scrollX.toFloat() / maxScrollX.toFloat()

        // 根据滚动比例计算 Item 的高度
        val minHeight = 0 // 最小高度
        val maxHeight = itemHeight // 最大高度
//        val dynamicHeight = minHeight + (maxHeight - minHeight) * scrollPercent
        val dynamicHeight = maxHeight * scrollPercent

        itemMargin = (offScreenWidth/visibleCount) * scrollPercent

        val containerHeight = abs(((itemHeight*maxSpan)*scrollPercent).toInt())
        if (containerHeight >= itemHeight) {
            binding?.llRv2?.height(containerHeight)
        }
        logd("scrollPercent=${scrollPercent} dynamicHeight=${dynamicHeight} containerHeight=${containerHeight}")

        return dynamicHeight.toInt()
    }

    private fun adjustSpanCount() {
        val scrollPercent = binding?.mRv?.scrollPercent?:0f

        itemMargin = (offScreenWidth/visibleCount) * scrollPercent

        if (scrollPercent == 0f) {
            binding?.cardRv!!.height(itemHeight)
        }else{
            val dynamicHeight = abs(((itemHeight*maxSpan)*scrollPercent).toInt())
            logd("scrollPercent=${scrollPercent} dynamicHeight=${dynamicHeight}")
            if (dynamicHeight >= itemHeight) {
                binding?.cardRv!!.height(dynamicHeight)
            }
        }
    }

    // 动态改变 RecyclerView 的高度
    private fun changeRecyclerViewHeight(view: View, newHeight: Int) {
        val layoutParams = view.layoutParams
        val animator = ValueAnimator.ofInt(view.height, newHeight)

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            layoutParams.height = animatedValue
            view.layoutParams = layoutParams
        }

        animator.duration = 300 // 动画时长 300 毫秒
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    private inner class MyAdapter(val list:List<String?>,val onBind:(Holder,Int)->Unit) : RecyclerView.Adapter<MyAdapter.Holder>() {

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun setText(text:String){
                itemView.findViewById<TextView>(R.id.tv_number).text = text
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_kingkong_test,parent,false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            onBind.invoke(holder,position)
        }

    }


    /**
     * 重新排列数据，使数据转换成分页模式
     * 原始数据：
     * 1 3 5 7 9   11 13 15
     * 2 4 6 8 10  12 14 16
     * ==============================
     * 转换之后：（数据会增加null值）
     * 1 2 3 4 5   11 12 13 14 15
     * 6 7 8 9 10  16 null...
     */
    private fun <T>rearrange(data: List<T>,lines:Int,spanCount:Int): List<T?> {
        if (lines <= 1) return data
        if (data == null || data.isEmpty()) return data
        val pageSize: Int = lines * spanCount
        val size = data.size
        //如果数据少于一行
        if (size <= spanCount) {
            return ArrayList<T>(data)
        }
        val destList: MutableList<T?> = ArrayList()
        //转换后的总数量，包括空数据
        val sizeAfterTransform = if (size < pageSize) {
            // sizeAfterTransform = pageSize;
            if (size < spanCount) size * lines else pageSize
        } else if (size % pageSize == 0) {
            size
        } else {
            // sizeAfterTransform = (size / pageSize + 1) * pageSize;
            if (size % pageSize < spanCount) size / pageSize * pageSize + size % pageSize * lines else (size / pageSize + 1) * pageSize
        }
        //类似置换矩阵
        for (i in 0 until sizeAfterTransform) {
            val pageIndex = i / pageSize
            val columnIndex: Int = (i - pageSize * pageIndex) / lines
            val rowIndex: Int = (i - pageSize * pageIndex) % lines
            val destIndex: Int = rowIndex * spanCount + columnIndex + pageIndex * pageSize
            if (destIndex >= 0 && destIndex < size) {
                destList.add(data[destIndex])
            } else {
                destList.add(null)
            }
        }
        return destList
    }

}