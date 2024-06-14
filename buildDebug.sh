#!/bin/bash
./gradlew clean
./gradlew assembleDebug --stacktrace -PisDebug=true
adb install -r  build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.fido.common.common_utils/.MainActivity
echo "install and start app"
# 想要加什么控制命令，就在assembleDebug后面补就可以了。远端打包的话，去掉安装和打开就可以了。
#本地使用的话直接在Terminal里输入：
#./buildDebug.sh