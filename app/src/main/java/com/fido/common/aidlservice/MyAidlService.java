package com.fido.common.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fido.common.IMyAidlInterface;
import com.fido.common.IMyCallback;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: HuTao
 * @date: 2025/6/23
 * @des:
 */
public class MyAidlService extends Service {
    private final String TAG = "MyAidlService";

    private Timer timer;

    private RemoteCallbackList<IMyCallback> callbackList = new RemoteCallbackList<>();

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void getPay(int status, String message) throws RemoteException {

        }

        @Override
        public void registerCallback(com.fido.common.IMyCallback callback) throws RemoteException {
            callbackList.register(callback);
        }

        @Override
        public void unregisterCallback(com.fido.common.IMyCallback callback) throws RemoteException {
            callbackList.unregister(callback);
        }

        @Override
        public String getMessage() throws RemoteException {
            return "";
        }
    };
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+intent.getAction());
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: " + intent.getAction());
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        // 创建并启动定时器，每5秒执行一次任务
        timer = new Timer();
        // 立即开始，每5000毫秒(5秒)执行一次
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Timer task executed: " + System.currentTimeMillis());
                // 在这里可以添加定时任务的具体操作
                int broadcast = callbackList.beginBroadcast();
                for (int i = 0; i < broadcast; i++) {
                    try {
                        IMyCallback callback = callbackList.getBroadcastItem(i);
                        callback.onSuccess("Timer task executed: " + System.currentTimeMillis());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                callbackList.finishBroadcast();
            }
        }, 0, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ---");
        if (timer != null) {
            timer.cancel();
        }
    }
}
