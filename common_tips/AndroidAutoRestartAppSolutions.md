# Android 车机应用异常 Kill 后自动拉起方案总结

> 场景：仪表（Cluster）等关键系统应用，在被 LMK/OOM/异常 kill 后需要立刻重启，保证用户体验不中断。

---

## 方案总览

| # | 方案 | 实现层 | 可靠性 | 适用角色 | 是否需要系统签名 |
|---|------|--------|--------|----------|-----------------|
| 1 | `persistent=true` | AMS | ★★★★★ | 系统应用开发者 | 是 |
| 2 | `init.rc` Service 守护 | Init (PID 1) | ★★★★★ | ROM / 系统厂商 | - |
| 3 | Shell 守护脚本轮询 | Init + Shell | ★★★☆☆ | 系统厂商 | - |
| 4 | Watchdog 心跳检测 | SystemServer | ★★★★☆ | 系统厂商 | - |
| 5 | oom_adj 优先级保护 | LMK / ProcessList | ★★★★☆ | 系统厂商 | - |
| 6 | 双进程互相守护 | App + Native | ★★★☆☆ | 应用开发者 | 否（效果有限） |
| 7 | Native Daemon (fork 监控) | Native (C/C++) | ★★★★☆ | 系统厂商 / NDK 开发者 | 否（需 root 或系统权限） |
| 8 | Account Sync 定时拉起 | ContentService | ★★☆☆☆ | 普通应用 | 否 |
| 9 | JobScheduler / WorkManager | App | ★★☆☆☆ | 普通应用 | 否 |
| 10 | AlarmManager 定时唤醒 | App | ★☆☆☆☆ | 普通应用 | 否 |
| 11 | CarService 定制机制（AAOS） | AOSP Car Framework | ★★★★★ | 车机系统厂商 | - |
| 12 | Vendor HAL / MCU 看门狗 | 硬件层 | ★★★★★ | 芯片 / Tier1 厂商 | - |

---

## 方案详解

### 方案 1：`android:persistent="true"`（最推荐）

**原理**：AMS（ActivityManagerService）内置机制，声明为 persistent 的应用进程被 kill 后，AMS 会自动调用 `startProcessLocked()` 重新拉起。

**实现步骤**：

**Step 1 — AndroidManifest.xml**
```xml
<application
    android:persistent="true"
    android:process="com.your.ivi.cluster"
    ... >
```

**Step 2 — 系统签名**

必须使用平台签名（platform key），或在 `Android.mk` / `Android.bp` 中声明：

```makefile
# Android.mk
LOCAL_MODULE := ClusterApp
LOCAL_MODULE_PATH := $(TARGET_OUT_PRIVILEGED_APPS)
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true
```

```json
// Android.bp
android_app {
    name: "ClusterApp",
    certificate: "platform",
    privileged: true,
    product_specific: true,
}
```

**Step 3 — 预装路径**

APK 必须预装到以下目录之一：
- `/system/app/`
- `/system/priv-app/`（推荐）
- `/product/priv-app/`
- `/vendor/app/`

**AMS 内部逻辑（简化）**：
```java
// ActivityManagerService.java
private final boolean appDiedLocked(ProcessRecord app, ...) {
    // ...
    if (app.persistent || keeping) {
        // 非用户主动 force-stop，自动重启
        startProcessLocked(app.processName, app.info.name, ...);
    }
    // ...
}
```

**注意事项**：
- 用户通过"设置 → 强制停止"手动 kill 后不会自动重启（`PROCESS_STOPPED` 标志位）
- 开机后 AMS 会自动拉起所有 persistent 应用，无需额外配置
- `oom_adj` 会被设置为 `PERSISTENT_PROC_ADJ (-800)`，LMK 几乎不会杀它

---

### 方案 2：`init.rc` Service 守护（最底层、最可靠）

**原理**：Android 的 `init` 进程（PID=1）负责拉起和管理系统关键服务，子进程退出后根据策略自动重启。

**实现**：

在 `device/<vendor>/<board>/init.<board>.rc` 中添加：

```rc
# 定义系统应用为 init service
service cluster_app /system/bin/app_process \
    -Xms64m -Xmx256m \
    /system/framework/am.jar \
    com.android.commands.am.Am start \
    -n com.your.ivi.cluster/.MainActivity
    class main
    user system
    group system
    onrestart restart cluster_app
    writepid /dev/cpuset/system-background/tasks
    # 关键：退出后自动重启
    # init 默认行为就是 service 退出后重启
```

或者更常见的做法 — 守护一个监控脚本：

```rc
service cluster_monitor /system/bin/cluster_monitor.sh
    class late_start
    user root
    group root
    disabled
    oneshot

# 在 boot 完成后启动
on property:sys.boot_completed=1
    start cluster_monitor
```

**优势**：
- 最底层守护，连 System Server 崩溃后 init 都能恢复
- 不受 AMS 状态影响

**限制**：
- 需要修改系统 `init.rc`，只有系统厂商能做
- 调试不方便

---

### 方案 3：Shell 守护脚本轮询

**原理**：编写一个 Shell 脚本作为 init service，定期检查目标进程是否存活，不存活则通过 `am` 命令拉起。

**脚本示例**（`/system/bin/cluster_monitor.sh`）：

```bash
#!/system/bin/sh

TARGET_PACKAGE="com.your.ivi.cluster"
CHECK_INTERVAL=5  # 秒
MAX_RETRY=3
RETRY_DELAY=2

while true; do
    # 检查进程是否存活
    if ! pidof "$TARGET_PACKAGE" > /dev/null 2>&1; then
        log -t "ClusterMonitor" "Process $TARGET_PACKAGE not found, restarting..."

        retry=0
        while [ $retry -lt $MAX_RETRY ]; do
            am start -n "$TARGET_PACKAGE/.MainActivity" \
                --activity-clear-task \
                --activity-single-top

            sleep $RETRY_DELAY

            if pidof "$TARGET_PACKAGE" > /dev/null 2>&1; then
                log -t "ClusterMonitor" "Restart success on retry $((retry+1))"
                break
            fi
            retry=$((retry + 1))
        done

        if [ $retry -ge $MAX_RETRY ]; then
            log -t "ClusterMonitor" "Failed to restart after $MAX_RETRY retries"
            # 可选：触发系统级告警
        fi
    fi

    sleep $CHECK_INTERVAL
done
```

**配合 init.rc**：
```rc
service cluster_monitor /system/bin/cluster_monitor.sh
    class late_start
    user root
    group root
    # 不设 oneshot，让它持续运行
```

**优势**：灵活，可定制重试策略和日志
**劣势**：轮询有延迟（`CHECK_INTERVAL` 秒），不够实时

---

### 方案 4：Watchdog 心跳检测

**原理**：系统 Watchdog 服务定期检测关键进程的心跳，超时未响应则判定死亡并重启。

**实现思路**：

```
┌──────────────────────┐                    ┌──────────────────────┐
│   SystemServer        │                    │   仪表应用进程         │
│   Watchdog 模块       │                    │   HeartBeat Service   │
│                       │                    │                       │
│  ┌─────────────────┐ │   定期 ping        │  ┌─────────────────┐ │
│  │ HeartbeatChecker │─┼──────────────────>│  │  Binder Handler  │ │
│  │                 │ │   expect pong      │  │  onPing()→pong   │ │
│  │  timeout=30s    │ │<──────────────────┼─ │                  │ │
│  └─────────────────┘ │                    │  └─────────────────┘ │
│         │            │                    │                       │
│   timeout! → kill + restart               │                       │
└──────────────────────┘                    └──────────────────────┘
```

**应用侧 Binder 心跳接口**：
```kotlin
// IClusterHeartbeat.aidl
interface IClusterHeartbeat {
    // Watchdog 调用此方法，应用必须在规定时间内响应
    boolean ping()
}
```

**Watchdog 侧（系统服务中）**：
```java
// 简化伪代码
private void checkClusterAlive() {
    try {
        boolean alive = clusterService.ping(); // Binder call, 超时抛 DeadObjectException
        if (!alive) {
            Slog.w(TAG, "Cluster heartbeat failed, restarting...");
            killAndRestartCluster();
        }
    } catch (DeadObjectException e) {
        // Binder 已死，进程确实挂了
        killAndRestartCluster();
    }
}
```

**优势**：
- 比轮询更实时（Binder 死亡即刻感知）
- 可以检测"进程在但 ANR 无响应"的情况

---

### 方案 5：oom_adj 优先级保护

**原理**：通过提高进程的 `oom_adj` 值（越小越不容易被杀），让 LMK 在内存不足时优先杀其他进程。

**oom_adj 层级**：
```
值       名称                     说明
-1000    NATIVE_ADJ              native 进程（init）
 -900    SYSTEM_ADJ              system_server
 -800    PERSISTENT_PROC_ADJ     persistent 应用 ← 仪表应用目标
 -700    PERSISTENT_SERVICE_ADJ  persistent 服务
    0    FOREGROUND_APP_ADJ      前台应用
  100    VISIBLE_ADJ             可见但非前台
  200    PERCEPTIBLE_ADJ         可感知（如后台音乐）
  300    BACKUP_ADJ              备份中
  400    SERVICE_ADJ             普通服务
  500    HOME_ADJ                Home 应用
  900    CACHED_APP_MIN_ADJ      缓存应用（最容易被杀）
  999    CACHED_APP_MAX_ADJ
```

**设置方式**：

系统厂商在 `init.rc` 或系统服务中为关键进程设置：
```rc
on boot
    # 确保仪表进程的 cgroup 和 oom_adj
    write /proc/<pid>/oom_adj -800
    write /dev/cpuset/system-background/tasks <pid>
```

**配合 `persistent=true`**：设置后 `oom_adj` 自动为 -800，通常不需要手动配置。

**LMK 杀进程顺序**：
```
内存不足时 LMK 扫描顺序（从先到后杀）：
CACHED (999→900) → SERVICE (400) → PERCEPTIBLE (200) → VISIBLE (100) → FOREGROUND (0)
                                                                            ↓
                                                              ↑ 仪表应用在这里 (-800)，几乎不会被杀
```

---

### 方案 6：双进程互相守护

**原理**：主进程和守护进程互相监控，一方死亡另一方立即拉起。

```
┌──────────────┐   监控    ┌──────────────┐
│   主进程       │ ──────> │   守护进程     │
│  (UI 进程)    │ <────── │  (Native/远端) │
└──────────────┘   监控    └──────────────┘
      │                          │
      └── 检测到对方死亡 → 拉起对方 ──┘
```

**实现方式**：

守护进程（可以是 Native 进程或另一个 Java 进程）：
```kotlin
// DaemonService.kt — 运行在独立进程
class DaemonService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkRunnable = object : Runnable {
        override fun run() {
            if (!isMainProcessAlive()) {
                // 拉起主进程
                val intent = Intent(this@DaemonService, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            handler.postDelayed(this, 5000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        handler.post(checkRunnable)
    }
}
```

**限制**：
- 普通应用效果有限，系统可能在杀主进程时连带杀守护进程
- 在 Android 8.0+ 后台限制下，守护进程存活困难
- 车机系统应用中可用，但不如方案 1 可靠

---

### 方案 7：Native Daemon（C/C++ 守护进程）

**原理**：用 Native 代码编写一个独立守护进程，通过 `fork` + `waitpid` 或轮询监控目标进程。

**C 代码示例**：
```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>
#include <android/log.h>

#define TAG "ClusterDaemon"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

int is_process_alive(pid_t pid) {
    return kill(pid, 0) == 0;
}

void restart_app() {
    LOGI("Restarting cluster app...");
    execlp("am", "am", "start",
           "-n", "com.your.ivi.cluster/.MainActivity",
           "--activity-clear-task", NULL);
}

int main() {
    pid_t target_pid = -1;

    while (1) {
        // 查找目标进程 PID
        // ... (通过 /proc 遍历或 pidof)

        if (target_pid > 0 && !is_process_alive(target_pid)) {
            LOGI("Target process %d died, restarting...", target_pid);
            restart_app();
            target_pid = -1;
        }

        sleep(3);  // 检查间隔
    }
    return 0;
}
```

**Android.mk**：
```makefile
include $(CLEAR_VARS)
LOCAL_MODULE := cluster_daemon
LOCAL_SRC_FILES := cluster_daemon.c
LOCAL_LDLIBS := -llog
include $(BUILD_EXECUTABLE)
```

**优势**：
- 不受 Java 层 GC / ANR 影响
- 资源占用小

---

### 方案 8：Account Sync 定时拉起

**原理**：利用 Android 的账号同步机制，系统会定期触发 sync，在 sync adapter 中可以拉起目标组件。

**实现**：
```kotlin
class ClusterSyncAdapter : AbstractThreadedSyncAdapter {
    override fun onPerformSync(...) {
        // 检查并拉起应用
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
```

**限制**：
- 同步间隔不可控（通常 30 分钟+）
- 不够实时，不适合关键系统应用
- 仅适合非关键的后台数据同步场景

---

### 方案 9：JobScheduler / WorkManager

**原理**：注册一个持久化的定时任务，任务执行时检查并拉起目标应用。

```kotlin
// 注册持久化 Job
val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(context, ClusterJobService::class.java))
    .setPeriodic(15 * 60 * 1000)  // 最小 15 分钟
    .setPersisted(true)            // 重启后依然有效
    .build()

jobScheduler.schedule(jobInfo)
```

**限制**：
- 最短间隔 15 分钟（Android 7.0+）
- 不保证准时执行
- 应用被 force-stop 后 Job 会被取消
- **不适合实时性要求高的场景**

---

### 方案 10：AlarmManager 定时唤醒

**原理**：设置精确定时闹钟，在回调中检查并拉起应用。

```kotlin
val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
val intent = Intent(context, ClusterRestartReceiver::class.java)
val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

alarmManager.setExactAndAllowWhileIdle(
    AlarmManager.RTC_WAKEUP,
    System.currentTimeMillis() + 60_000,  // 1 分钟后
    pendingIntent
)
```

**限制**：
- Android 6.0+ Doze 模式下不可靠
- 进程被 kill 后 BroadcastReceiver 也可能无法接收
- 最小间隔受限（Android 8.0+ 约 9 分钟）
- **最不可靠的方案，仅做了解**

---

### 方案 11：CarService 定制机制（AAOS 专用）

**原理**：Android Automotive OS (AAOS) 提供了 `CarService` 框架层，可配置关键应用的保活策略。

**CarPackageManagerService**：
```java
// CarService 中可以配置 persistent apps
// frameworks/opt/car/services/src/com/android/car/CarPackageManagerService.java

// 通过 overlay 配置关键应用
// packages/services/Car/service/res/values/config.xml
<string-array name="config_persistentApplications">
    <item>com.your.ivi.cluster</item>
    <item>com.your.ivi.hvac</item>
</string-array>
```

**GarageMode / Shutdown 保护**：
```
AAOS 的 Garage Mode 保证在车辆熄火后，
关键系统应用仍然有机会完成数据保存和状态恢复。
下次上电时，CarService 会按配置顺序依次拉起这些应用。
```

**Cluster 专属 API（Android 10+）**：
```java
// CarInstrumentClusterManager
CarInstrumentClusterManager clusterManager =
    (CarInstrumentClusterManager) car.getCarManager(
        CarInstrumentClusterManager.SERVICE_NAME);

// 注册 cluster 应用，系统会保证其运行
clusterManager.registerClusterActivityListener(listener);
```

---

### 方案 12：Vendor HAL / MCU 看门狗（硬件层）

**原理**：车机硬件层面的 MCU（微控制器）提供硬件看门狗，SoC 侧应用定期喂狗，超时未喂狗则 MCU 触发 SoC 复位或重启指定应用。

```
┌─────────────────┐    I2C/SPI/CAN     ┌─────────────────┐
│      MCU         │ <───────────────> │      SoC         │
│  (STM32/Renesas) │    喂狗信号        │  (Android 系统)  │
│                  │                    │                  │
│  ┌────────────┐  │                    │  ┌────────────┐  │
│  │ HW Watchdog│  │   timeout!         │  │ Cluster App│  │
│  │  Timer     │──┼──> 复位 SoC 或     │  │            │  │
│  │  30s 超时   │  │    发送重启指令     │  │ 定期喂狗    │  │
│  └────────────┘  │                    │  └────────────┘  │
└─────────────────┘                    └─────────────────┘
```

**应用侧喂狗**：
```kotlin
// 通过 JNI 调用 HAL 层喂狗接口
class WatchdogFeeder {
    companion object {
        init { System.loadLibrary("watchdog_jni") }
    }

    // Native method - 通过 ioctl 与 MCU 通信
    external fun feedWatchdog()

    // 在 Service 中定期调用
    fun startFeeding() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                feedWatchdog()
                handler.postDelayed(this, 10_000) // 10 秒喂一次
            }
        }, 0)
    }
}
```

**优势**：
- 硬件级保护，最高可靠性
- 可以应对整个 SoC 死机的情况

---

## 推荐组合方案

对于车机仪表等关键系统应用，建议**多层组合**使用：

```
┌─────────────────────────────────────────────────────┐
│                    防护层级图                          │
│                                                       │
│   ┌─────────────────────────────────────────────┐   │
│   │  第 1 层：persistent=true + 系统签名           │   │
│   │  → AMS 自动拉起，oom_adj=-800                 │   │
│   │  → 覆盖 90% 的场景                             │   │
│   └─────────────────────────────────────────────┘   │
│                          ↓ 如果第 1 层失效             │
│   ┌─────────────────────────────────────────────┐   │
│   │  第 2 层：Watchdog 心跳检测                    │   │
│   │  → 检测 ANR / Binder 死亡，主动重启            │   │
│   │  → 覆盖进程假死场景                             │   │
│   └─────────────────────────────────────────────┘   │
│                          ↓ 如果第 2 层失效             │
│   ┌─────────────────────────────────────────────┐   │
│   │  第 3 层：init.rc 守护 / Shell 脚本             │   │
│   │  → 系统级兜底，进程级保护                        │   │
│   └─────────────────────────────────────────────┘   │
│                          ↓ 如果第 3 层失效             │
│   ┌─────────────────────────────────────────────┐   │
│   │  第 4 层：MCU 硬件看门狗（如有）                 │   │
│   │  → 终极兜底，硬件级复位                          │   │
│   └─────────────────────────────────────────────┘   │
│                                                       │
└─────────────────────────────────────────────────────┘
```

---

## 应用开发者 Checklist

- [ ] `AndroidManifest.xml` 中声明 `android:persistent="true"`
- [ ] 使用平台签名（platform key）
- [ ] 预装到 `/system/priv-app/` 或 `/product/priv-app/`
- [ ] `onCreate()` 中做好状态重建（不依赖上次保存的内存状态）
- [ ] 关键 Service 使用 `START_STICKY` 返回值
- [ ] 配合系统 Watchdog 实现心跳 Binder 接口
- [ ] 处理 `onTrimMemory()` 和 `onLowMemory()` 回调，主动释放非必要资源

## 系统厂商 Checklist

- [ ] 配置 LMK 阈值，保护 persistent 进程
- [ ] 实现 Watchdog 心跳检测机制
- [ ] 配置 `init.rc` 守护脚本作为兜底
- [ ] AAOS 场景配置 `config_persistentApplications`
- [ ] MCU 侧实现硬件看门狗（如适用）
- [ ] 提供进程状态监控日志和告警机制

---

## 参考

- [Android AMS Process Management](https://source.android.com/docs/core/architecture/kernel/low-memory-killer)
- [Android Automotive OS Documentation](https://source.android.com/docs/devices/automotive)
- [AOSP ActivityManagerService.java](https://cs.android.com/android/platform/superproject/+/master:frameworks/base/services/core/java/com/android/server/am/ActivityManagerService.java)
- [AOSP CarPackageManagerService.java](https://cs.android.com/android/platform/superproject/+/master:packages/services/Car/service/src/com/android/car/CarPackageManagerService.java)
