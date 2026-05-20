# 记录下由应用端导致系统服务(system_service) InputMethodManager 的 binder leak

## 应用端TextView设置跑马灯效果并在文本更新时主动调用requestFocus()获取焦点

##  问题原因：
### 每次 requestFocus() 后，IMMS 会将其视为需要输入法连接的新焦点 View
### 马跑灯每次循环或文本更新时调用 requestFocus()，导致高频重复 START INPUT

##  解决方案
### 1. 移除 requestFocus()通过setSelected(true)也可实现跑马灯效果。
