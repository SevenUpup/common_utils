package com.fido.common.common_utils.eventbus;

/**
 * @author FiDo
 * @description:
 * @date :2023/10/26 11:30
 */
public enum ThreadMode {

    /**
     * 事件在UI线程
     */
    MAIN,

    /**
     * 当前线程
     */
    POST,

    /**
     * IO线程
     */
    ASYNC,

}
