package com.fido.common.common_utils.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.BlendModeCompat;
import androidx.core.graphics.PaintCompat;

import java.util.Arrays;

/**
 * @author: FiDo
 * @date: 2024/4/7
 * @des:
 */
public class CodeRainView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    private TextPaint mPaint;
    private TextPaint mBitmapPaint;

    private Surface surface;
    private boolean sizeChanged;
    private BitmapCanvas mBitmapCanvas;

    {
        initPaint();
    }

    public CodeRainView(Context context) {
        this(context, null);
    }

    public CodeRainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    private void initPaint() {
        //否则提供给外部纹理绘制
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(20);

        mBitmapPaint =  new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);

        PaintCompat.setBlendMode(mPaint, BlendModeCompat.PLUS);
    }


    Thread drawThread = null;


    char[] characters = "张三爱李四，Alice love you".toCharArray();
    int[] drops = null;

    private volatile boolean isRunning = false;
    private final Object lockSurface = new Object();

    Matrix matrix = new Matrix();

    @Override
    public void run() {
        while (true) {
            synchronized (surface) {
                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isRunning || Thread.currentThread().isInterrupted()) {
                    synchronized (lockSurface) {
                        if (surface != null && surface.isValid()) {
                            surface.release();
                        }
                        surface = null;
                    }
                    break;
                }


                Canvas canvas = null;
                if (sizeChanged || drops == null) {
                    if (mBitmapCanvas != null) {
                        mBitmapCanvas.recycle();
                    }
                    mBitmapCanvas = new BitmapCanvas(Bitmap.createBitmap(getWidth() / 2, getHeight() / 2, Bitmap.Config.RGB_565));
                    int columCount = (int) (mBitmapCanvas.getBitmap().getWidth() / mPaint.getTextSize());
                    drops = new int[columCount];
                    Arrays.fill(drops, 1);
                    sizeChanged = false;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    canvas = surface.lockHardwareCanvas();
                } else {
                    canvas = surface.lockCanvas(null);
                }
                drawChars(mBitmapCanvas, mPaint);

                matrix.reset();
                matrix.setScale(2, 2);

                canvas.drawBitmap(mBitmapCanvas.getBitmap(), matrix, mBitmapPaint);

                surface.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.drawThread = new Thread(this);
        this.surface = holder.getSurface();
        this.isRunning = true;
        this.drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Surface drawSurface = surface;
        if (drawSurface == null) {
            return;
        }
        synchronized (drawSurface) {
            isRunning = false;
        }
        if (drawThread != null) {
            try {
                drawThread.interrupt();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        drawThread = null;
    }

    static class BitmapCanvas extends Canvas {
        Bitmap bitmap;

        public BitmapCanvas(Bitmap bitmap) {
            super(bitmap);
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void recycle() {
            if (bitmap == null || bitmap.isRecycled()) {
                return;
            }
            bitmap.recycle();
        }
    }

    void drawChars(Canvas canvas, Paint paint) {
        canvas.drawColor(argb(0.1f, 0f, 0f, 0f));
        paint.setColor(0xFF00FF00);
        int height = getHeight();
        float textSize = paint.getTextSize();

        for (int i = 0; i < drops.length; i++) {
            int index = (int) Math.floor(Math.random() * characters.length);
            canvas.drawText(characters, index, 1, i * textSize, drops[i] * textSize, paint);
            if (drops[i] * textSize > height && Math.random() > 0.975) {
                drops[i] = 0;
            }
            drops[i]++;
        }
    }

    public static int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }


}
