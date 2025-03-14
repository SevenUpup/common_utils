package com.fido.common.common_utils.customview.custom;

/**
 * @author: FiDo
 * @date: 2025/3/12
 * @des:  子view按横向/竖向排列的 可滚动view group
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class CustomScrollViewGroup extends ViewGroup {
    public enum Orientation { HORIZONTAL, VERTICAL }

    private Orientation orientation = Orientation.HORIZONTAL; // 默认横向滑动

    private int currentIndex = 0;
    private float downX, downY;
    private Scroller scroller;

    public CustomScrollViewGroup(Context context) {
        this(context, null);
    }

    public CustomScrollViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0, top = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                if (orientation == Orientation.HORIZONTAL) {
                    child.layout(left, 0, left + getWidth(), getHeight());
                    left += getWidth();
                } else {
                    child.layout(0, top, getWidth(), top + getHeight());
                    top += getHeight();
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(ev.getX() - downX);
                float dy = Math.abs(ev.getY() - downY);
                if (orientation == Orientation.HORIZONTAL) {
                    if (dx > dy) return true; // 拦截横向滑动
                } else {
                    if (dy > dx) return true; // 拦截纵向滑动
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float deltaX = moveX - downX;
                float deltaY = moveY - downY;

                if (orientation == Orientation.HORIZONTAL) {
                    scrollBy((int) -deltaX, 0);
                } else {
                    scrollBy(0, (int) -deltaY);
                }

                downX = moveX;
                downY = moveY;
                break;

            case MotionEvent.ACTION_UP:
                smoothScrollToNearestChild();
                break;
        }
        return true;
    }

    private void smoothScrollToNearestChild() {
        int targetIndex;
        if (orientation == Orientation.HORIZONTAL) {
            int scrollX = getScrollX();
            targetIndex = (scrollX + getWidth() / 2) / getWidth();
        } else {
            int scrollY = getScrollY();
            targetIndex = (scrollY + getHeight() / 2) / getHeight();
        }
        scrollToPosition(targetIndex);
    }

    public void scrollToPosition(int index) {
        if (index < 0) index = 0;
        if (index >= getChildCount()) index = getChildCount() - 1;

        currentIndex = index;
        int dx = 0, dy = 0;

        if (orientation == Orientation.HORIZONTAL) {
            dx = index * getWidth() - getScrollX();
        } else {
            dy = index * getHeight() - getScrollY();
        }

        scroller.startScroll(getScrollX(), getScrollY(), dx, dy, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}

