package com.fido.common.common_utils.widgets.chat_bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.fido.common.R;

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:
 */
public class BarChartItem extends View {
    private static final String TAG = "BarCharView";
    private Paint paint;
    private int measuredWidth;
    private int measuredHeight;
    private double ratio;
    private double barRatio;
    private GradientDrawable gradientDrawable;

    public BarChartItem(Context context) {
        super(context);
        initPaint();
    }

    public BarChartItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public BarChartItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    //设置第一层bar的比例
    public void setRatio(double ratio) {
        this.ratio = ratio;
        invalidate();
    }

    //设置内部bar的比例
    public void setBarRatio(double barRatio) {
        this.barRatio = barRatio;
        invalidate();
    }

    //初始化画笔与设置顶部圆角
    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.white));
        //抗锯齿
        paint.setAntiAlias(true);

        gradientDrawable = new GradientDrawable();

        //设置顶部圆角
        gradientDrawable.setCornerRadii(new float[]{15,15,15,15,0,0,0,0});
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //背景（可以填充背景，本例子中无影响）
        canvas.drawRect(0, 0, measuredWidth, measuredHeight, paint);
        //第一层bar
        if (ratio != 0){
            myDraw(canvas,ratio, Color.parseColor("#FFFFCC00"));
        }
        //内部bar
        if (barRatio != 0){
            myDraw(canvas,ratio * barRatio,Color.parseColor("#FF41C7DB"));
        }
    }

    private void myDraw(Canvas canvas,double myRatio,int color) {
        canvas.save();
        gradientDrawable.mutate();
        gradientDrawable.setColor(color);
        int drawHeight = (int) (measuredHeight * myRatio + 0.5); //四舍五入了下
        //这里把画布移到绘画地方的左上角
        canvas.translate(0,measuredHeight - drawHeight);
        Log.d(TAG, "myDraw: measuredHeight=" + measuredHeight + " drawHeight=" + drawHeight + " measuredHeight-drawHeight =" + (measuredHeight - drawHeight)
                + "\nmyRatio="+myRatio + " ratio=" + ratio + " barRatio="+barRatio);

        //设置绘制矩形
        gradientDrawable.setBounds(0, 0, measuredWidth, drawHeight);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }

}
