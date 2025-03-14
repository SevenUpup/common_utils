package com.fido.common.common_utils.customview.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: FiDo
 * date :  2022/3/16 0016.16:34
 * desc :
 */
public class MyScrollView extends ScrollView {
    private RecyclerView recyclerView;
    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private int itemCount;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
                    firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                    lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                    itemCount = layoutManager.getItemCount();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int nowY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nowY = y;
                intercept = super.onInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstVisibleItemPosition == 0 && y > nowY) {
                    intercept = true;
                    break;
                } else if (lastVisibleItemPosition == itemCount-1 && y<nowY) {
                    intercept = true;
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }


        return intercept;
    }
}
