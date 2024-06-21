#!/bin/bash

# terminal 执行 ./update_params/update_param.sh

# 开启调试模式
#set -x

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
./gradlew assembleDebug

# 关闭调试模式
#set +x
