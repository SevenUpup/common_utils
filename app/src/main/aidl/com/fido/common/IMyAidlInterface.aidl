// IMyAidlInterface.aidl
package com.fido.common;
import com.fido.common.IMyCallback;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    void getPay(int status, String message);

    void registerCallback(IMyCallback callback);

    void unregisterCallback(IMyCallback callback);


    String getMessage();
}