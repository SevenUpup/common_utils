package com.fido.common.common_utils.eventbus;

import java.lang.reflect.Method;

/**
 * @author FiDo
 * @description:
 * @date :2023/10/27 8:52
 */
public class SubscribeMethod {

    private Method method;
    private ThreadMode threadMode;
    private Class<?> eventType;

    public SubscribeMethod(Method method, ThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
