package com.fido.common.common_base_ui.base.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isNotEmpty
import com.fido.common.common_base_ui.util.dp
import kotlin.math.abs

/**
 * @author: FiDo
 * @date: 2025/3/13
 * @des:  类似于咸鱼 拖拽到最右边时 显示 “更多” -> "释放查看"
 *        只能含有一个子View(里面再嵌套无所谓)
 */
class DragLookMoreContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val TAG = this.javaClass.simpleName

    private val mShowMoreTextView by lazy {
        getChildAt(this.childCount-1) as ShowMoreTextView
    }

    private val contentView by lazy {
        getChildAt(0)
    }

    private var contentViewWidth = 0
    private var contentViewHeight = 0
    private var showMoreTextWidth = 0
    private var showMoreTextHeight = 0

    private var mHintLeftMargin = 0f
    private var RELEASE_MORE = "查看详情"
    private var SCROLL_MORE = "释放查看"

    //右侧布局的偏移量
    private var mOffsetWidth = 0f

    //回弹动画
    private var mReleasedAnim:ValueAnimator?=null

    private var RATIO: Float = 0.4f //阻尼
    private var releaseAnimDuration = 300L
    private var hintTextView:ShowMoreTextView?=null

    //手指触摸的位置
    private var mLastX = 0f
    private var mLastY = 0f

    private var downX = 0f
    private var downY = 0f

    private var mShowMoreListener:(()->Unit)?=null

    /**
     * @param dampingRation         设置滑动阻尼系数 0 - 1.0
     * @param releaseAnimDuration   回弹释放动画时长
     * @param initHintText          init 拖拽后显示的Text
     * @param showMoreListener      触发释放时的回调
     */
    fun setUp(
        dampingRation:Float = RATIO,
        releaseAnimDuration:Long = this.releaseAnimDuration,
        initHintText:(ShowMoreTextView.()-> Unit)?=null,
        showMoreListener:(()->Unit)?=null,
    ) = apply {
        if (initHintText != null) {
            hintTextView?.apply {
                initHintText.invoke(this)
            }
        }
        this.releaseAnimDuration = releaseAnimDuration
        this.RATIO = dampingRation
        this.mShowMoreListener = showMoreListener
    }

    /**
     * 设置默认文案
     */
    fun setHintText(
        firstText:String = SCROLL_MORE,
        moreText:String = RELEASE_MORE,
    ) = apply {
        this.SCROLL_MORE = firstText
        this.RELEASE_MORE = moreText
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isNotEmpty() && !children.any { it is ShowMoreTextView }) {
            hintTextView = ShowMoreTextView(context).apply {
                text = SCROLL_MORE
                textSize = 13f
                gravity = Gravity.CENTER
                setPadding(15.dp,0,20.dp,0)
            }
            addView(hintTextView, LayoutParams(LayoutParams.WRAP_CONTENT,100.dp))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        contentViewWidth = contentView.measuredWidth
        contentViewHeight = contentView.measuredHeight

        showMoreTextWidth = mShowMoreTextView.measuredWidth
        showMoreTextHeight = mShowMoreTextView.measuredHeight

        mOffsetWidth = -showMoreTextWidth.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //测量容器布局
        this.measureChild(contentView,widthMeasureSpec,heightMeasureSpec)
        //测量ShowMore布局
        this.measureChild(mShowMoreTextView,widthMeasureSpec,heightMeasureSpec)
        //将宽高 设置为 以 容器宽高为主
        setMeasuredDimension(contentView.measuredWidth,contentView.measuredHeight)

//        Log.d(TAG, "onMeasure: measuredWidth=${contentView.measuredWidth} measuredHeight=${contentView.measuredHeight}")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        contentView.layout(0,0,contentViewWidth,contentViewHeight)
        mShowMoreTextView.layout(
            contentViewWidth,contentViewHeight/2 - showMoreTextHeight/2,
            contentViewWidth + showMoreTextWidth,contentViewHeight/2 - showMoreTextHeight/2 + showMoreTextHeight
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        Log.d(TAG, "onInterceptTouchEvent: action=>${ev.action}")
        var intercept = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                intercept = false
                downX = ev.rawX
                downY = ev.rawY

                mHintLeftMargin = 0f
                mLastY = ev.x
                mLastX = ev.y
            }
            MotionEvent.ACTION_MOVE ->{
                val canScrollLeft = contentView.canScrollHorizontally(1)
                val canScrollRight = contentView.canScrollHorizontally(-1)
//                Log.d(TAG, "onInterceptTouchEvent: 是否支持左滑=${canScrollLeft} 是否支持右滑=${canScrollRight}")
                if (canScrollLeft || canScrollRight){
                    val deltaX = ev.rawX - downX
                    val deltaY = ev.rawY - downY
                    //横向滑动
                    if (abs(deltaX) > abs(deltaY)) {
                        //右滑,不拦截交给子View(子View是一个滑动控件)自己处理
                        if (deltaX > 0) {
//                            intercept = false
                            intercept = !canScrollRight
                        }else{ //左滑
                            //是否能左滑
//                            val canScroll2Left = contentView.canScrollHorizontally(1)
                            //不能左滑，滑到最左边了，交给当前ViewGroup处理事件(把事件分发给了onTouchEvent)
                            intercept = !canScrollLeft
                        }
                    }
                    downX = ev.rawX
                    downY = ev.rawY

                    mLastX = ev.x
                    mLastY = ev.y
                }
            }
            MotionEvent.ACTION_UP-> intercept = false
        }
        return intercept
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.d(TAG, "onTouchEvent: action=>${event.action}")
        when (event.action) {
            MotionEvent.ACTION_DOWN->{
                mHintLeftMargin = 0f
                mLastY = event.x
                mLastX = event.y
            }
            MotionEvent.ACTION_MOVE->{
                if (mReleasedAnim != null && mReleasedAnim!!.isRunning) {
                    return false
                }
                var mDeltaX = event.x - mLastX
                val mDeltaY = event.y - mLastY

                if (abs(mDeltaX) > abs(mDeltaY)) {
                    //横向滑动
                    mDeltaX *= RATIO
//                    Log.d(TAG, "onTouchEvent: mDeltaX= $mDeltaX event.x=${event.x} mLastX=${mLastX}" )

                    if (mDeltaX > 0) {
                        //右滑
                        setHintTextTranslationX(mDeltaX)
                    }else if (mDeltaX < 0){
                        //左滑
                        setHintTextTranslationX(mDeltaX)
                    }
                }
                mLastX = event.x
                mLastY = event.y
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{

                //拦截事件-父布局滚
                parent.requestDisallowInterceptTouchEvent(false)

                // 释放动画
                if (mReleasedAnim != null && mReleasedAnim!!.isRunning) {
                    return false
                }

                //判断是否滑动到了指定位置，用以触发监听
                if (mOffsetWidth != 0f && mHintLeftMargin <= mOffsetWidth && mShowMoreListener != null) {
                    mShowMoreListener?.invoke()
                }

                // 设置弹回动画
                mReleasedAnim = ValueAnimator.ofFloat(1.0f,0f)
                mReleasedAnim?.duration = releaseAnimDuration
                mReleasedAnim?.addUpdateListener {
                    val value = it.animatedValue as Float
                    mShowMoreTextView.translationX = value * (mShowMoreTextView.translationX)
                    contentView.translationX *= value
                }
                mReleasedAnim?.start()
            }
        }

        return true
    }

    //将容器与 提示语一起 做平移动画
    private fun setHintTextTranslationX(deltaX: Float) {
        var offsetX = 0f
        mHintLeftMargin += deltaX
        if (mHintLeftMargin <= mOffsetWidth) {
            offsetX = mOffsetWidth
            mShowMoreTextView.setVerticalText(RELEASE_MORE)
        }else{
            offsetX = mHintLeftMargin
            mShowMoreTextView.setVerticalText(SCROLL_MORE)
        }
        mShowMoreTextView.setShadowOffset(offsetX,mOffsetWidth.toFloat())
        mShowMoreTextView.translationX = offsetX

        contentView.translationX = offsetX
//        Log.d(TAG, "setHintTextTranslationX: offsetX=${offsetX}")
    }


}