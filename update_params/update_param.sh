#!/bin/bash

# terminal 执行 ./update_params/update_param.sh

# 开启调试模式
#set -x

# 定义布尔值变量
IS_DEBUG="false"

# 读取并执行 sed 命令
source ./update_params/read_config.sh | while read -r cmd; do
    if [[ $cmd == sed* ]]; then
        echo "Executing command: $cmd"
        eval $cmd
        if [ $? -eq 0 ]; then
            echo "Executed: $cmd"
        else
            echo "Failed to execute: $cmd"
            exit 1
        fi
    else
        echo "$cmd" >&2
    fi
done

# 清理旧的构建
echo "Running gradlew clean"
./gradlew clean

# 重新编译并打包
#./gradlew assembleRelease
#./gradlew assembleDebug

# 检查 IS_DEBUG 的值并执行相应的代码
if [ "$IS_DEBUG" = "true" ]; then
    ./gradlew assembleDebug
    # 在调试模式下执行的代码
else
#
    ./gradlew assembleRelease
    # 在非调试模式下执行的代码
fi
# 在脚本的最后打开一个 .exe 文件
EXE_PATH="/g/HT/FiDoSignTool/fidoSign.exe"
"$EXE_PATH"

# Tips:gradlew命令可以通过-P参数生成properties 通过配置signingConfig中release下参数实现
#  ./gradlew app:assembleRelease -PsigningStore=F:/xx/xx/xx.jks -PsigningStorePassword=xxx -PsigningKeyAlias=xxx -PsigningKeyPassword=xxx
#    signingConfigs {
#        //TODO: 更换私钥并移除git仓库中的keystore, 见: https://zhuanlan.zhihu.com/p/91595756
#        release {
#            storeFile file(project.properties.get('signingStore', 'NOT_FOUND.jks'))
#            storePassword project.properties.get('signingStorePassword', 'NO_PASSWORD')
#            keyAlias project.properties.get('signingKeyAlias', 'android')
#            keyPassword project.properties.get('signingKeyPassword', 'NO_PASSWORD')
#
#            v1SigningEnabled true
#            v2SigningEnabled true
#        }
#
#        debug {
#            storeFile file("$rootDir/debug.keystore")
#
#            v1SigningEnabled true
#            v2SigningEnabled true
#        }
#    }



