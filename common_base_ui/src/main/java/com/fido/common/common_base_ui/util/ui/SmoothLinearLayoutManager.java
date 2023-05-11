package com.fido.common.common_base_ui.util.ui;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author FiDo
 * @description:
 * @date :2023/4/19 18:17
 */
public class SmoothLinearLayoutManager extends LinearLayoutManager {

    private float MILLISECONDS_PER_INCH = 25f;
    private Context contxt;
    boolean startScrolling = false;

    public SmoothLinearLayoutManager(Context context) {
        super(context);
        this.contxt = context;
    }

    public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.contxt = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

//        //简单的控制速度
//        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
//            @Override
//            public PointF computeScrollVectorForPosition(int targetPosition) {
//                //根据要滑动的索引计算需要滚动的距离，调用自带的方法计算即可
//                return SmoothLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
//            }
//
//            @Override
//            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
////                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
//                //滑动一个像素需要多少毫秒，需要我们手动调整
//                return 25f / displayMetrics.density;
//            }
//
//        };


        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            //太远了，没有找到View
            @Override
            protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
                //真实场景需要判断索引与方向
                int firstPos = findFirstVisibleItemPosition();
                int lastPos = findLastVisibleItemPosition();
                PointF pointF = computeScrollVectorForPosition(position);
                int height = recyclerView.getMeasuredHeight();

                float distance = Math.abs((position - firstPos) * getDecoratedMeasuredHeight(getChildAt(0))) / pointF.y;

                if (distance > 0) {
                    recyclerView.scrollBy(0, (int) distance - height);
                } else {
                    recyclerView.scrollBy(0, (int) distance + height);
                }

                recyclerView.smoothScrollToPosition(position);
            }

//            @Override
//            protected void onSeekTargetStep(int dx, int dy, RecyclerView.State state, Action action) {
//                YYLogUtils.w("太远了，没有找到View  dy：" + dy + " action-dy:" + action.getDy() + " action-du:" + action.getDuration());
//
//                //真实场景需要判断索引与方向
//                if (!startScrolling) {
//                    startScrolling = true;
//
//                    int firstPos = findFirstVisibleItemPosition();
//
//                    //根据真实场景判断是否超过索引边界与展示边界
//                    if (firstPos < position) {
//                        scrollToPositionWithOffset(position - 10, 0);
//                    } else {
//                        scrollToPositionWithOffset(position + 10, 0);
//                    }
//
//                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//                        @Override
//                        public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {
//                            if (startScrolling && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                                recyclerView.removeOnScrollListener(this);
//                                startScrolling = false;
//                            }
//                        }
//
//                        @Override
//                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                        }
//                    });
//
//                    recyclerView.smoothScrollToPosition(position);
//                }
//
//            }

            //慢慢滚慢慢找，找到了！
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                final int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
                final int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());

                //获取滚动距离
                final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                //根据滚动距离计算时间
                final int time = calculateTimeForDeceleration(distance);

                if (time > 0) {
                    action.update(-dx, -dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {

                int firstPos = findFirstVisibleItemPosition();
                int lastPos = findLastVisibleItemPosition();

                if (targetPosition < firstPos) {
                    return new PointF(0, -1);
                } else if (targetPosition > lastPos) {
                    return new PointF(0, 1);
                } else {
                    return super.computeScrollVectorForPosition(targetPosition);
                }
            }

            @Override
            protected int calculateTimeForDeceleration(int dx) {
                return 250;
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 15f / displayMetrics.densityDpi;
            }


        };


        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    //可以用来设置速度
    public void setSpeedSlow(float x) {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.3f + (x);
    }

}
