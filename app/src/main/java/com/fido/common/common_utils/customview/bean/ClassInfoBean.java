package com.fido.common.common_utils.customview.bean;

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:
 */
public class ClassInfoBean {
    public static int MaxGuardSum = 0;
    public static int WorkerSum = 2444;
    private String OnJobClassName;
    private int onGuardNum;
    private int GuardSum;

    public void setOnJobClassName(String onJobClassName) {
        OnJobClassName = onJobClassName;
    }

    public void setOnGuardNum(int onGuardNum) {
        this.onGuardNum = onGuardNum;
    }

    public void setGuardSum(int guardSum) {
        GuardSum = guardSum;
        if(GuardSum > MaxGuardSum){
            MaxGuardSum = GuardSum;
        }
    }

    public String getOnJobClassName() {
        return OnJobClassName;
    }

    public int getOnGuardNum() {
        return onGuardNum;
    }

    public int getGuardSum() {
        return GuardSum;
    }

    public int getSumRatio() {
        return 0 == MaxGuardSum ? 0 : GuardSum * 100 / MaxGuardSum;
    }

    public int getOnGuardNumRatio() {
        return 0 == GuardSum ? 0 : onGuardNum * 100 / GuardSum;
    }
}

