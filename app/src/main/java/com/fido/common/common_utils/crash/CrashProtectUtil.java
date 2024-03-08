package com.fido.common.common_utils.crash;

import android.os.Looper;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * @author FiDo
 * @description:
 * @date :2023/5/15 14:58
 */
public class CrashProtectUtil implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mOldHandler;

    public void init(){
        mOldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (mOldHandler != this) {
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        if (needBandage(e)) {
            bandage();
            return;
        }

        //崩吧
        if (mOldHandler != null) {
            mOldHandler.uncaughtException(t, e);
        }
    }

    private boolean needBandage(Throwable ex){
        //如果是没磁盘空间了，尝试清理一波缓存
        if (isNoSpaceException(ex)) {
            // cleanAppCache
            return true;
        }

        //BadTokenException
        if (isBadTokenException(ex)) {
            return true;
        }

        return false;
    }

    private boolean isNoSpaceException(Throwable ex) {
        String message = ex.getMessage();
        return !TextUtils.isEmpty(message) && message.contains("No space left on device");
    }

    private boolean isBadTokenException(Throwable ex) {
        return ex instanceof WindowManager.BadTokenException;
    }

    /**
     * 让当前线程恢复运行
     */
    private void bandage() {
        try {
            //fix No Looper; Looper.prepare() wasn't called on this thread.
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            Looper.loop();
        } catch (Exception e) {
            uncaughtException(Thread.currentThread(), e);
        }
    }

}
