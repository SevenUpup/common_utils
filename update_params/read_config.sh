#!/bin/bash

# 开启调试模式
set -x

# 读取配置文件
CONFIG_FILE="./update_params/config.json"

# 检查配置文件是否存在
if [ ! -f "$CONFIG_FILE" ]; then
    echo "配置文件 $CONFIG_FILE 不存在"
    exit 1
fi

# 逐行读取 JSON 配置文件并解析
while IFS= read -r line; do
    # 检查并提取文件路径
    if echo "$line" | grep -q '"file_path"'; then
        file_path=$(echo "$line" | sed -n 's/.*"file_path": "\(.*\)".*/\1/p' | tr -d ',')
    fi
    # 检查并提取旧值
    if echo "$line" | grep -q '"old_value"'; then
        old_value=$(echo "$line" | sed -n 's/.*"old_value": "\(.*\)".*/\1/p' | tr -d ',')
    fi
    # 检查并提取新值
    if echo "$line" | grep -q '"new_value"'; then
        new_value=$(echo "$line" | sed -n 's/.*"new_value": "\(.*\)".*/\1/p' | tr -d ',')
    fi

    # 当三个变量都有值时，生成 sed 命令并清空变量
    if [ -n "$file_path" ] && [ -n "$old_value" ] && [ -n "$new_value" ]; then
        echo "sed -i 's|$old_value|$new_value|g' \"$file_path\""
        file_path=""
        old_value=""
        new_value=""
    fi
done < "$CONFIG_FILE"

# 关闭调试模式
set +x
