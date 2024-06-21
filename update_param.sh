#!/bin/bash
# 定义要修改的文件和参数
TARGET_FILE="../common_utils/app/src/main/java/com/fido/common/common_utils/test/asm/PrintTest.java"
OLD_VALUE="true"
NEW_VALUE="false"

TARGET_FILE2="../common_utils/app/src/main/java/com/fido/common/common_utils/design_pattern/DesignPatternAc.kt"
OLD_VALUE2="false"
NEW_VALUE2="true"

OLD_VALUE3="false"
NEW_VALUE3="true"

# 使用 sed 命令替换参数值
sed -i "s/public static boolean C = $OLD_VALUE;/public static boolean C = $NEW_VALUE;/" $TARGET_FILE
sed -i "s/private val isChange2 = $OLD_VALUE2/private val isChange2 = $NEW_VALUE2/" $TARGET_FILE2
sed -i "s/private var isChange3 = $OLD_VALUE3/private var isChange3 = $NEW_VALUE3/" $TARGET_FILE2

# 确保 sed 命令正确运行
if [ $? -eq 0 ]; then
    echo "Parameter updated successfully."
else
    echo "Failed to update parameter."
    exit 1
fi

# 清理旧的构建
./gradlew clean

# 重新编译并打包
#./gradlew assembleRelease
./gradlew assembleDebug

adb install -r  build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.fido.common.common_utils/.MainActivity
echo "install and start app"

# 确保打包成功
if [ $? -eq 0 ]; then
    echo "APK built successfully."
else
    echo "Failed to build APK."
    exit 1
fi