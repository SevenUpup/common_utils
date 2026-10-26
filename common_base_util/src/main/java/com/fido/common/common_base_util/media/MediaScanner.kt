package com.fido.common.common_base_util.media

import android.content.Context
import android.database.ContentObserver
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume

/**
 * FileScanner
 * 功能：
 * 1. 注册媒体库 ContentObserver，媒体库变化时触发扫描（防抖）。
 * 2. scanFiles 支持浅层（传目录本身）与深度（递归枚举媒体文件）扫描。
 * 3. 使用协程 + Mutex 保证单次串行扫描，防止并发重复消耗。
 * 4. 提供 unregister 与 cleanup 释放资源。
 * 非目标：不负责媒体文件查询（应使用 MediaStore 查询）。
 *
 * 参考 ： https://juejin.cn/post/7573587915810308137
 *
 * 使用方式
 * 1. 申请权限
 * private boolean hasRequiredMediaPermissions() {
 *   if (Build.VERSION.SDK_INT >= 33) {
 *     return checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
 *         && checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
 *   } else {
 *     return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
 *   }
 * }
 *
 * 2.启动监听（如在 Application 或 Service 中）：
 * FileScanner.registerMediaObserver(applicationContext)
 *
 * 3.触发扫描（如用户点击按钮）
 * // 深度扫描指定目录
 * FileScanner.scanFiles(context, arrayOf("/path/to/new/photos"), deepScan = true)
 * // 仅扫描公共目录，浅层
 * FileScanner.scanFiles(context, deepScan = false)
 *
 *  4. 清理资源（在 onDestroy 或 onStop 中）：
 *  FileScanner.cleanup(applicationContext)
 */
object FileScanner {
    private val mutex = Mutex()
    // 使用 IO 调度器，确保文件扫描在后台线程执行
    private var scope: CoroutineScope = createScope()
    private var mediaObserver: MediaContentObserver? = null
    // 用于防抖的 Job
    private var debounceJob: Job? = null
    // 观察者注册状态
    private val isRegistered = AtomicBoolean(false)
    private const val DEBOUNCE_DELAY = 1000L // 1秒防抖

    // 支持的媒体扩展名（统一为小写）
    private val mediaExtensions = setOf(
        "jpg", "jpeg", "png", "gif", "webp", "bmp", // 图片
        "mp4", "avi", "mov", "mkv", "3gp", "webm" // 视频
    )

    private fun createScope() = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private fun ensureScope(): CoroutineScope {
        if (!scope.isActive) {
            // 如果 Scope 被取消，则重新创建一个新的
            scope = createScope()
        }
        return scope
    }

    /** 判断是否是支持的媒体文件 */
    private fun isMediaFile(file: java.io.File): Boolean {
        if (!file.isFile) return false
        val name = file.name
        val dot = name.lastIndexOf('.')
        // 确保有扩展名且不在文件名开头或结尾
        if (dot <= 0 || dot == name.length - 1) return false
        val ext = name.substring(dot + 1).lowercase()
        return mediaExtensions.contains(ext)
    }

    /**
     * 扫描指定路径列表。paths 为 null 时扫描常用公共目录（DCIM / Pictures / Movies）。
     * deepScan=true：递归枚举目录下所有媒体文件；false：仅把目录路径交给系统扫描。
     */
    fun scanFiles(context: Context, paths: Array<String>? = null, deepScan: Boolean = true) {
        // 1. 实现防抖：取消旧的防抖任务
        if (debounceJob?.isActive == true) {
            debounceJob?.cancel()
        }
        // 2. 启动新的防抖任务
        debounceJob = ensureScope().launch {
            delay(DEBOUNCE_DELAY)
            // 3. 进入 Mutex 保护区，串行执行扫描逻辑
            mutex.withLock {
                performMediaScan(context, paths, deepScan)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun performMediaScan(context: Context, paths: Array<String>?, deepScan: Boolean) {
        // 默认扫描路径
        val inputPaths = paths ?: arrayOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath
        )

        val filesToScan = mutableListOf<String>()

        inputPaths.forEach { path ->
            val f = java.io.File(path)
            if (!f.exists()) return@forEach
            if (f.isDirectory) {
                if (deepScan) {
                    // 深度扫描：递归收集目录下所有媒体文件
                    collectMediaFiles(f, filesToScan)
                } else {
                    // 浅层扫描：仅把目录本身路径加入扫描列表
                    filesToScan.add(path)
                }
            } else {
                // 单文件扫描：如果不是深度扫描，或者文件是支持的媒体文件，则加入
                if (!deepScan || isMediaFile(f)) {
                    filesToScan.add(path)
                }
            }
        }

        if (filesToScan.isEmpty()) return

        val scanPaths = filesToScan.toTypedArray()

        // 将 MediaScannerConnection.scanFile 的异步回调封装成挂起函数
        suspendCancellableCoroutine<Unit> { continuation ->
            val total = scanPaths.size
            val completed = AtomicInteger(0)
            val resumed = AtomicBoolean(false) // 避免重复 resume

            MediaScannerConnection.scanFile(context, scanPaths, null) { _, _ ->
                val done = completed.incrementAndGet()
                // 所有文件扫描完成，或协程仍未被取消
                if (done == total && resumed.compareAndSet(false, true)) {
                    continuation.resume(Unit)
                }
            }

            // 协程被取消时（如 debounceJob 被取消）
            continuation.invokeOnCancellation {
                // 可选：如果 MediaScannerConnection 有取消机制，可以在这里调用
            }
        }
    }

    /** 递归收集媒体文件（深度扫描用） */
    private fun collectMediaFiles(dir: java.io.File, out: MutableList<String>) {
        val children = dir.listFiles() ?: return
        for (child in children) {
            if (child.isFile) {
                if (isMediaFile(child)) out.add(child.absolutePath)
            } else if (child.isDirectory && !child.name.startsWith('.')) {
                // 忽略隐藏目录
                collectMediaFiles(child, out)
            }
        }
    }

    /** 注册媒体库变化观察者 */
    fun registerMediaObserver(context: Context) {
        if (!isRegistered.compareAndSet(false, true)) return
        try {
            val handler = Handler(Looper.getMainLooper())
            // 媒体库变化时，触发一次防抖扫描
            mediaObserver = MediaContentObserver(handler) {
                scanFiles(context)
            }
            context.contentResolver.registerContentObserver(
                // 监听所有文件变化
                MediaStore.Files.getContentUri("external"),
                true, // 深度监听子目录
                mediaObserver!!
            )
        } catch (e: Exception) {
            isRegistered.set(false)
            throw e
        }
    }

    /** 注销媒体库观察者 */
    fun unregisterMediaObserver(context: Context) {
        if (!isRegistered.compareAndSet(true, false)) return
        debounceJob?.cancel()
        debounceJob = null
        mediaObserver?.let {
            try { context.contentResolver.unregisterContentObserver(it) } catch (_: Exception) {}
        }
        mediaObserver = null
    }

    /** 清理全部资源（通常在宿主销毁时调用） */
    fun cleanup(context: Context? = null) {
        try { context?.let { unregisterMediaObserver(it) } } catch (_: Exception) {}
        // 取消所有协程任务
        scope.cancel()
    }
}

/** ContentObserver 简单封装 */
class MediaContentObserver(
    handler: Handler,
    private val onMediaChange: () -> Unit,
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        onMediaChange()
    }
}
