package com.fido.common.common_base_ui.ext.imageview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @author FiDo
 * @description:
 * @date :2023/9/28 15:29
 */
public class ScrollWindowImageView extends AppCompatImageView {
    private boolean initialized;
    private Matrix matrix;
    public ScrollWindowImageView(Context context) {
        this(context, null);
    }

    public ScrollWindowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollWindowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initialized = true;
        initImage();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        initImage();
    }

    private void initImage() {
        final Drawable drawable = getDrawable();
        if (!initialized || drawable == null) {
            return;
        }
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();
        final int drawableWidth = drawable.getIntrinsicWidth();
        final int drawableHeight = drawable.getIntrinsicHeight();
        float scaleX = (float) viewWidth / drawableWidth;
        matrix = getImageMatrix();
        matrix.setScale(scaleX, scaleX);
        matrix.postTranslate(0, -(drawableHeight * scaleX - viewHeight));
        setImageMatrix(matrix);
    }


    public void scrollWindow(float scrollY) {
        final Drawable drawable = getDrawable();
        if (!initialized || drawable == null) {
            return;
        }
        float[] imageMatrixValues = new float[9];
        matrix.getValues(imageMatrixValues);
        float scale = Math.abs(imageMatrixValues[0]) + Math.abs(imageMatrixValues[1]);
        matrix.setScale(scale, scale);
        float translate = scrollY - (drawable.getIntrinsicHeight() * scale - getHeight());
        if (translate > 0) {
            translate = 0;
        }
        Log.e("FiDo", ScrollWindowImageView.this.hashCode() + " translate = " + translate );
        matrix.postTranslate(0, translate);
        setImageMatrix(matrix);
    }

    private int firstScrollY = 0;
    /**
     * ScrollWindowImageView 最好不要嵌套父布局，否则会循环查找
     * @param recyclerView     需要绑定的 Rv
     */
    public void scrollWindow(RecyclerView recyclerView){
        if (recyclerView == null || !(recyclerView.getLayoutManager() instanceof LinearLayoutManager) || firstScrollY>0) return;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int childCount = layoutManager.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = layoutManager.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        ViewGroup vg = (ViewGroup) view;
                        for (int j = 0; j < vg.getChildCount(); j++) {
                            if (vg.getChildAt(j) instanceof ScrollWindowImageView && view.hashCode() == ScrollWindowImageView.this.hashCode()) {
                                scrollY(recyclerView, vg);
                            }
                        }
                    }else {
                        // hashcode 判断 Rv如果存在多个 ScrollWindowImageView，识别唯一值用
                        if (view instanceof ScrollWindowImageView && view.hashCode() == ScrollWindowImageView.this.hashCode()) {
                            scrollY(recyclerView, view);
                        }
                    }
                }
            }
        });
    }

    private int _currentScroolY = 0;
    private void scrollY(RecyclerView recyclerView, View view) {
        int parentBottom = recyclerView.getBottom();
        int itemBottom = view.getBottom();
        if (itemBottom < parentBottom) {
            int scroolY = parentBottom - itemBottom;
            if (firstScrollY == 0) {
                firstScrollY = scroolY;
                Log.e("FiDo", ScrollWindowImageView.this.hashCode()+"scrollY: " + firstScrollY);
            } else {
                _currentScroolY = scroolY - firstScrollY;
                Log.e("FiDo", ScrollWindowImageView.this.hashCode()+ " _currentScroolY=" + _currentScroolY);
                scrollWindow(_currentScroolY);
            }
        }
    }

}
