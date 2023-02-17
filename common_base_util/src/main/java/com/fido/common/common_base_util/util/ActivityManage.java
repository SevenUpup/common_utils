package com.fido.common.common_base_util.util;


import android.app.Activity;
import android.content.Context;

import java.util.Stack;


/**
 * 管理activity的manage
 */
public class ActivityManage {

    private static Stack<Activity> activityStack;

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }
        activityStack.push(activity);
    }

    //获取activity栈
    public static  Stack<Activity> getActivityStack(){
        return activityStack;
    }

    /**
     * 只是移除栈，不用结束Activity
     */
    public static void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        Activity activity = activityStack.pop();
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束当前页面之前的全部页面
     */
    public static void finishBeforActivity() {
        for (int i = 0; i < activityStack.size() ; i++) {
            Activity activity = activityStack.get(i);
            if (activity != null) finishActivity(activity);
        }
    }

    /**
     * 退出应用程序
     */

    public static void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            manager.killBackgroundProcesses(context.getPackageName());
//            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}