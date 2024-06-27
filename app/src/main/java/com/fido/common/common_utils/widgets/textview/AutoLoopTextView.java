package com.fido.common.common_utils.widgets.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 仿制EditText 一个一个字符的添加 和删除的效果，并轮播展示
 */
public class AutoLoopTextView extends AppCompatTextView implements DefaultLifecycleObserver {

    private ExecutorService mSingleThreadExecutor;

    public AutoLoopTextView(@NonNull Context context) {
        super(context);
    }

    public AutoLoopTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoopTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private List<String> mLoopDatas;
    private int mTextCount = 0;
    private int mCurIndex = 0;
    private long charTimeMilles = 50L;    //文本打印的时间间隔  默认50毫秒
    private long clearTimeMilles = 2500L;  //清空的时间间隔    默认2.5秒

    private Handler mHandler;


    /**
     * 设置下一个文本
     */
    @SuppressLint("SetTextI18n")
    private void setNextText(String str, int index) {
        if (TextUtils.isEmpty(str)) return;

        setTag(str);   //取值从Tag取

        if (mSingleThreadExecutor == null) return;
        mSingleThreadExecutor.execute(() -> {

            int n = index;
            int nn;

            try {
                String stv;
                if (str.length() < n) {
                    stv = str.substring(0, str.length());
                } else {
                    stv = str.substring(0, n);
                }

                post(() -> {
                    if (!TextUtils.isEmpty(stv)) {
                        setText(stv);
                    }
                });

                Thread.sleep(charTimeMilles);//休息片刻

                nn = n + 1; //n+1；多截取一个

                if (nn <= str.length()) { //如果还有汉字，那么继续开启线程，相当于递归的感觉
                    try {
                        setNextText(str, nn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //写完了，开始延时清除
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.sendEmptyMessageDelayed(102, clearTimeMilles);
                    } else {
                        resetHandle();
                    }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * 清空上一个文本
     */
    private void clearPreText(int index) {
        String str = getText().toString();
        if (TextUtils.isEmpty(str)) return;

        if (mSingleThreadExecutor == null) return;
        mSingleThreadExecutor.execute(() -> {

            int n = index == -1 ? str.length() : index;
            int nn;

            try {
                if (n == 0) {
                    post(() -> {
                        setText("");
                    });
                } else {
                    String stv;
                    if (str.length() < n) {
                        stv = str.substring(0, str.length());
                    } else {
                        stv = str.substring(0, n);
                    }

                    post(() -> {
                        if (!TextUtils.isEmpty(stv)) {
                            setText(stv);
                        }
                    });
                }

                Thread.sleep(charTimeMilles);//休息片刻

                nn = n - 1;//n-1；少截取一个

                if (nn >= 0) {  //如果还有汉字，那么继续开启线程，相当于递归的感觉
                    try {
                        clearPreText(nn);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //清除完了，开始写入
                    if (mHandler != null) {
                        mHandler.removeCallbacksAndMessages(null);
                        mHandler.sendEmptyMessage(101);
                    } else {
                        resetHandle();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * 设置轮播数据
     */
    public void setLoopDate(List<String> list) {
        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        initHandle();
        mLoopDatas = list;
        mTextCount = list.size();
    }

    @SuppressLint("HandlerLeak")
    private void initHandle() {
        //主线程创建Handle
        if (mHandler == null) {
            setupHandler();
        }
    }

    private void setupHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull @NotNull Message msg) {

                if (msg.what == 101) {
                    //开始写入
                    if (mCurIndex >= mTextCount) {
                        mCurIndex = 0;
                    }

                    try {
                        setNextText(mLoopDatas.get(mCurIndex), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mCurIndex++;

                } else if (msg.what == 102) {
                    //开始清除
                    try {
                        clearPreText(-1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    /**
     * 重启Handle
     */
    @SuppressLint("HandlerLeak")
    private void resetHandle() {
        if (mSingleThreadExecutor != null) {
            //先停止线程池
            mSingleThreadExecutor.execute(() -> {
                if (mLoopDatas!=null && !mLoopDatas.isEmpty()) {
                    mTextCount = mLoopDatas.size();
                }
            });
        }

        post(() -> {
            //主线程创建Handle
            if (mHandler == null) {
                setupHandler();
            }

            mHandler.sendEmptyMessage(101);
        });

    }

    /**
     * 开始轮播
     */
    public void startTextLoop() {
        if (mLoopDatas == null || mLoopDatas.isEmpty()) return;
        //从写入文本开始
        mSingleThreadExecutor.execute(() -> {
            if (mLoopDatas!=null && !mLoopDatas.isEmpty()) {
                mTextCount = mLoopDatas.size();
            }
        });
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessage(101);
        } else {
            resetHandle();
        }
    }

    /**
     * 停止轮播
     */
    public void stopTextLoop() {
        if (mLoopDatas == null || mLoopDatas.isEmpty()) return;

        mSingleThreadExecutor.execute(() -> {
            if (mLoopDatas!=null && !mLoopDatas.isEmpty()) {
                mTextCount = mLoopDatas.size();
            }
        });
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

    }

    /**
     * 销毁资源
     */
    public void destroyTextLoop() {
        stopTextLoop();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mSingleThreadExecutor != null && !mSingleThreadExecutor.isShutdown()) {
            mSingleThreadExecutor.shutdown();
        }
        mSingleThreadExecutor = null;
    }

    /**
     * **********************************************************************
     * ------------------------ 生命周期控制 --------------------------------*
     * **********************************************************************
     */

    public void addLoopLifecycleObserver(LifecycleOwner owner) {
        if (owner != null) {
            owner.getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        startTextLoop();
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        stopTextLoop();
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        destroyTextLoop();
    }

}

