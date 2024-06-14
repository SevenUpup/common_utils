package com.fido.common.common_base_ui.widget.ninegrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.fido.common.common_base_ui.R;

/**
 * @author: FiDo
 * @date: 2024/6/13
 * @des:
 */

public class NineGridLayout extends ViewGroup {

    private static final int MAX_CHILDREN_COUNT = 9;  //最大的子View数量
    private int horizontalSpacing = 20;  //每一个Item的左右间距
    private int verticalSpacing = 20;  //每一个Item的上下间距
    private boolean fourGridMode = true;  //是否支持四宫格模式
    private boolean singleMode = true;  //是否支持单布局模式
    private boolean singleModeScale = true;  //是否支持单布局模式按比例缩放
    private int singleWidth;
    private int singleHeight;

    private int itemWidth;
    private int itemHeight;

    private Adapter mAdapter;

    public NineGridLayout(Context context) {
        this(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);
            int spacing = a.getDimensionPixelSize(R.styleable.NineGridLayout_spacing, 0);
            horizontalSpacing = a.getDimensionPixelSize(R.styleable.NineGridLayout_horizontal_spacing, spacing);
            verticalSpacing = a.getDimensionPixelSize(R.styleable.NineGridLayout_vertical_spacing, spacing);
            singleMode = a.getBoolean(R.styleable.NineGridLayout_single_mode, true);
            fourGridMode = a.getBoolean(R.styleable.NineGridLayout_four_gird_mode, true);
            singleWidth = a.getDimensionPixelSize(R.styleable.NineGridLayout_single_mode_width, 0);
            singleHeight = a.getDimensionPixelSize(R.styleable.NineGridLayout_single_mode_height, 0);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int notGoneChildCount = getNotGoneChildCount();

        if (mAdapter == null || mAdapter.getItemCount() == 0 || notGoneChildCount == 0) {
            setMeasuredDimension(widthSize, 0);
            return;
        }

        if (notGoneChildCount == 1 && singleMode) {
            itemWidth = singleWidth > 0 ? singleWidth : widthSize;
            itemHeight = singleHeight > 0 ? singleHeight : widthSize;
            if (itemWidth > widthSize && singleModeScale) {
                itemWidth = widthSize;  //单张图片先定宽度。
                itemHeight = (int) (widthSize * 1f / singleWidth * singleHeight);  //根据宽度计算高度
            }
        } else {
            //除了单布局模式，其他的都是指定的固定宽高
            itemWidth = (widthSize - horizontalSpacing * 2) / 3;
            itemHeight = itemWidth;
        }

        //measureChildren内部调用measureChild，这里我们就可以指定宽高
        measureChildren(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));

        if (heightMode == MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {

            notGoneChildCount = Math.min(notGoneChildCount, MAX_CHILDREN_COUNT);
            int heightSize = ((notGoneChildCount - 1) / 3 + 1) *
                    (itemHeight + verticalSpacing) - verticalSpacing + getPaddingTop() + getPaddingBottom();

            setMeasuredDimension(widthSize, heightSize);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int notGoneChildCount = getNotGoneChildCount();

        if (mAdapter == null || mAdapter.getItemCount() == 0 || notGoneChildCount == 0) {
            return;
        }

        int position = 0;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int row = position / 3;    //当前子View是第几行（索引）
            int column = position % 3; //当前子View是第几列（索引）

            if (notGoneChildCount == 4 && fourGridMode) {
                row = position / 2;
                column = position % 2;
            }

            //当前需要绘制的光标的X与Y值
            int x = column * itemWidth + getPaddingLeft() + horizontalSpacing * column;
            int y = row * itemHeight + getPaddingTop() + verticalSpacing * row;

            child.layout(x, y, x + itemWidth, y + itemHeight);

            //最多只摆放9个
            position++;
            if (position == MAX_CHILDREN_COUNT) {
                break;
            }
        }

        performBind();

    }

    /**
     * 布局完成之后绑定对应的数据到对应的ItemView
     */
    private void performBind() {

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        post(() -> {

            for (int i = 0; i < getNotGoneChildCount(); i++) {
                int itemType = mAdapter.getItemViewType(i);
                View view = getChildAt(i);

                mAdapter.onBindItemView(view, itemType, i);
            }

        });

    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        inflateAllViews();
    }

    private void inflateAllViews() {
        removeAllViewsInLayout();

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        int displayCount = Math.min(mAdapter.getItemCount(), MAX_CHILDREN_COUNT);

        //单布局处理
        if (singleMode && displayCount == 1) {
            View view = mAdapter.onCreateItemView(getContext(), this, -1);
            addView(view);
            requestLayout();
            return;
        }

        //多布局处理
        for (int i = 0; i < displayCount; i++) {
            int itemType = mAdapter.getItemViewType(i);

            View view = mAdapter.onCreateItemView(getContext(), this, itemType);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addView(view);
        }
        requestLayout();
    }


//    /**
//     * 创建每一个不同的View
//     */
//    protected void inflateChildLayoutCustom(ViewGetter viewGetter) {
//        removeAllViews();
//        for (int i = 0; i < MAX_CHILDREN_COUNT; i++) {
//            addView(viewGetter.getView(i));
//        }
//    }
//
//    /**
//     * 一般用这个方法填充布局，每一个小布局的布局文件(相同的文件)
//     */
//    protected void inflateChildLayout(int layoutId) {
//        removeAllViews();
//        for (int i = 0; i < MAX_CHILDREN_COUNT; i++) {
//            LayoutInflater.from(getContext()).inflate(layoutId, this);
//        }
//    }


    //获取没有隐藏的子布局
    private int getNotGoneChildCount() {
        int childCount = getChildCount();
        int notGoneCount = 0;
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i).getVisibility() != View.GONE) {
                notGoneCount++;
            }
        }
        return notGoneCount;
    }

    /**
     * 设置显示的数量
     */
    public void setDisplayCount(int count) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setVisibility(i < count ? VISIBLE : GONE);
        }
    }

    /**
     * 设置单独布局的宽和高
     */
    public void setSingleModeSize(int w, int h) {
        if (w != 0 && h != 0) {
            this.singleMode = true;
            this.singleWidth = w;
            this.singleHeight = h;
        }
    }

    /**
     * 单独设置是否支持四宫格模式
     */
    public void setFourGridMode(boolean enable) {
        this.fourGridMode = enable;
    }

//    //为每一个子View提供创建方式
//    public interface ViewGetter {
//        View getView(int position);
//    }


    public static abstract class Adapter {

        //返回总共子View的数量
        public abstract int getItemCount();

        //根据索引创建不同的布局类型，如果都是一样的布局则不需要重写
        public int getItemViewType(int position) {
            return 0;
        }

        //简单点，根据类型创建对应的View布局
        public abstract View onCreateItemView(Context context, ViewGroup parent, int itemType);

        //可以根据类型或索引绑定数据
        public abstract void onBindItemView(View itemView, int itemType, int position);

    }
}

