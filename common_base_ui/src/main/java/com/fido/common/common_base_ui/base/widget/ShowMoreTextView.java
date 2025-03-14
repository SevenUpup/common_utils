package com.fido.common.common_base_ui.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.fido.common.common_base_ui.util.ViewUtilsKt;

/**
 * 右侧的查看滑动更多，竖版排列文本效果，并绘制贝塞尔曲线背景
 */
public class ShowMoreTextView extends AppCompatTextView {

    // 默认文本
    private CharSequence mDefaultText = "更多";

    //默认使用文本画笔
    protected TextPaint mTextPaint;
    //每个文字的间距
    private int mCharSpacing;

    // 贝塞尔阴影画笔
    private Paint mShadowPaint;
    // 贝塞尔的路径
    private Path mShadowPath;
    //贝塞尔曲线的控制点-变量动态控制
    private float mShadowOffset = 0;
    //默认的间距
    private int mNormalSpaceing;

    private int textBgColor = Color.parseColor("#4FCCCCCC");

    public ShowMoreTextView(Context context) {
        this(context, null);
    }

    public ShowMoreTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowMoreTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //画笔的一些配置
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setAntiAlias(true);

        //默认间距
        mCharSpacing = ViewUtilsKt.getDp(4);
        mNormalSpaceing = ViewUtilsKt.getDp(8);

        //画笔赋值
        mShadowPaint = new Paint();
        mShadowPaint.setColor(textBgColor);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setStrokeWidth(1);

        mShadowPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextPaint.setTextSize(getTextSize());
        mTextPaint.setColor(getCurrentTextColor());
        mTextPaint.setTypeface(getTypeface());

        //竖版文本的绘制
        CharSequence text = mDefaultText;
        if (text != null && !text.toString().trim().equals("")) {
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(text.toString(), 0, text.length(), bounds);

            float startX = getLayout().getLineLeft(0) + getPaddingLeft();

            //处理drawleft的间距
            if (getCompoundDrawables()[0] != null) {
                Rect drawRect = getCompoundDrawables()[0].getBounds();
                startX += (drawRect.right - drawRect.left);
            }

            startX += getCompoundDrawablePadding();

            float startY = getBaseline();

            //不处理bounds会导致间距异常
            int cHeight = (bounds.bottom - bounds.top + mCharSpacing);

            // 居中水平对齐
            startY -= (text.length() - 1) * cHeight / 2;

            for (int i = 0; i < text.length(); i++) {
                String c = String.valueOf(text.charAt(i));
                canvas.drawText(c, startX, startY + i * cHeight, mTextPaint);
            }

        }

        // 动态的绘制贝塞尔的背景
        mShadowPath.reset();
        mShadowPath.moveTo(getWidth(), 0);
        mShadowPath.quadTo(mShadowOffset, getHeight() / 2, getWidth(), getHeight());
        canvas.drawPath(mShadowPath, mShadowPaint);

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mDefaultText = text;
        super.setText("", type);
    }

    public void setVerticalText(CharSequence text) {
        if (TextUtils.isEmpty(text)) return;
        mDefaultText = text;
        invalidate();
    }


    /**
     * 暴露的方法，控制贝塞尔曲线的控制点
     */
    public void setShadowOffset(float offset, float maxOffset) {

        this.mShadowOffset = offset;
        float dis = maxOffset / 2 - mNormalSpaceing;
        if (mShadowOffset >= dis) {
            mShadowOffset = dis;
        } else {
            mShadowOffset = dis + (offset - dis) / 2;
        }
        invalidate();
    }
}
