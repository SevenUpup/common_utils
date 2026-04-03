# Android Window 相关知识点

## 问题：显示一个带简单布局的 Activity 时，屏幕上通常存在几个 Window？

通常存在 3 个 window，分别是:

### 1. 状态栏 Window（Status Bar）
- 属于 SystemUI 进程管理的系统级 Window。
- 位于屏幕顶部，负责显示时间、电量、通知图标等。

### 2. 导航栏 Window (Navigation Bar)
- 属于 SystemUI 进程管理的系统级 Window。
- 位于屏幕底部，负责显示返回、home、多任务切换等图标。

### 3. Activity 上的主 Window (Application Window)
- 属于我们自己应用进程的 Window。
- 它是 PhoneWindow 的实例，内部包裹着 DecorView。
- 我们在 Activity 中写的那个"简单布局"（通过 setContentView 传入的），就是挂载在这个 Window 的 DecorView 之中的。