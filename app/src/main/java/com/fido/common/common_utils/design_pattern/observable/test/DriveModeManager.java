package com.fido.common.common_utils.design_pattern.observable.test;

public class DriveModeManager {

    //private static final String TAG = "DriveModeManager"; // TAG is not needed for standard Java logs
    private static String CURRENT_DRIVE_MODE = "";
    private static final String XMODE_DRIVE_MODE = "7";

    private static boolean isXModeDriveMode = false;

    // 用于记录上次的驱动模式
    private static String PREVIOUS_DRIVE_MODE = "";

    public static boolean isXModeDriveMode(){
        return isXModeDriveMode;
    }

    /**
     * "0:""No Driving Mode"",
     * 1:""Super ECO"",
     * 2:""ECO"",
     * 3:""Normal"",
     * 4:""Sport"",
     * 5:""Super Sport"",
     * 6:""Reserved"",
     * 7:""Customization Mode"""
     *
     */
    public static void handleSignal(String signal) {
        isXModeDriveMode = XMODE_DRIVE_MODE.equals(signal);

        // Use System.out.println for logging instead of Android Log.d
//        System.out.println("handleSignal: " + signal + (XMODE_DRIVE_MODE.equals(signal) ? " now enter XMode " : "") + " CURRENT_DRIVE_MODE=" + CURRENT_DRIVE_MODE);

        if (signal != null && !signal.isEmpty() && !CURRENT_DRIVE_MODE.equals(signal)) {
            PREVIOUS_DRIVE_MODE = CURRENT_DRIVE_MODE;
            CURRENT_DRIVE_MODE = signal;
            resetUIForDriveMode(signal);
        }
    }

    private static void resetUIForDriveMode(String value) {
        boolean isXMode = DriveModeManager.checkIsXMode(value);
        if (isXMode) {
            System.out.println("进入Xmode 切换成Xmode卡片");

        } else {
            boolean enterOrExitXMode = DriveModeManager.enterOrExitXMode(value);
            if (enterOrExitXMode) {
                System.out.println("当前进入或退出Xmode模式，开始切换卡片");
                return;
            }
            System.out.println("不切换卡片 signal=" + value);
        }
    }

    /**
     * 是否 进入了Xmode / 退出了Xmode
     */
    public static boolean enterOrExitXMode(String currentValue){
        if (XMODE_DRIVE_MODE.equals(currentValue) && !XMODE_DRIVE_MODE.equals(PREVIOUS_DRIVE_MODE)) {
            // Entered Xmode
            return true;
        } else if (XMODE_DRIVE_MODE.equals(PREVIOUS_DRIVE_MODE) && !XMODE_DRIVE_MODE.equals(currentValue)) {
            // Exited Xmode
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkIsXMode(String signal){
        return XMODE_DRIVE_MODE.equals(signal);
    }

    public static void main(String[] args) {
        DriveModeManager.handleSignal("7");
        DriveModeManager.handleSignal("4");
        DriveModeManager.handleSignal("3");
        DriveModeManager.handleSignal("2");


        DriveModeManager.handleSignal("7");
        DriveModeManager.handleSignal("3");
        DriveModeManager.handleSignal("4");
        DriveModeManager.handleSignal("5");
        DriveModeManager.handleSignal("2");
        DriveModeManager.handleSignal("7");
    }
}
