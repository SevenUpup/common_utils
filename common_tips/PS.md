### PS 运行命令报错，提示 xxx 禁止运行脚本   about_Execution_Policies。

## 解决方案：更改 PowerShell 执行策略

1. 以管理员身份打开 PowerShell
按 Win 键 → 搜索 PowerShell → 右键选择 “以管理员身份运行”

2. 查看当前执行策略
Get-ExecutionPolicy

3. 设置更宽松的执行策略（推荐以下之一）
推荐（安全且实用）执行: Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

RemoteSigned 含义：
本地脚本（如你自己的 claude.ps1）可直接运行
从网络下载的脚本必须有可信签名才能运行
-Scope CurrentUser：只影响当前用户，无需系统级权限，更安全

