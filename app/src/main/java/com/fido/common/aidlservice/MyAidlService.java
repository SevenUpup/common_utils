package com.fido.common.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.fido.common.IMyAidlInterface;

/**
 * @author: HuTao
 * @date: 2025/6/23
 * @des:
 */
public class MyAidlService extends Service {

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void getPay(int status, String message) throws RemoteException {

        }

        @Override
        public String getMessage() throws RemoteException {
            return "";
        }
    };
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
