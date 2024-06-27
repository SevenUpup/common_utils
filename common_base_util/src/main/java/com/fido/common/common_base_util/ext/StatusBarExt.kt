package com.fido.common.common_base_util.ext

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.abs

/**
 * @author: FiDo
 * @date: 2024/6/27
 * @des:
 */

// =======================  StatusBar begin ↓ =========================
/**
 * 老的方法获取状态栏高度
 */
val Context.statusBarHeight:Int
    get() {
        var result = 0
        runCatching {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

/**
 * 新方法获取状态栏高度
 */
fun Activity.getStatusBarHeight(heightCall:(height:Int)->Unit){
    findViewById<View>(android.R.id.content).getStatusBarHeight(heightCall)
}

fun View.getStatusBarHeight(heightValue:(height:Int)->Unit){
    val attachedToWindow: Boolean = this.isAttachedToWindow

    if (attachedToWindow) {
        val windowInsets = ViewCompat.getRootWindowInsets(this)
        val top = windowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top?:0
        val bottom = windowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.bottom?:0
        val height = abs(bottom - top)
        if (height > 0) {
            heightValue(height)
        } else {
            heightValue(context.statusBarHeight)
        }
    } else {
        this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                val windowInsets = ViewCompat.getRootWindowInsets(v)
                val top = windowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top?:0
                val bottom = windowInsets?.getInsets(WindowInsetsCompat.Type.statusBars())?.bottom?:0
                val height = abs(bottom - top)
                if (height > 0) {
                    heightValue(height)
                } else {
                    heightValue(context.statusBarHeight)
                }
            }

            override fun onViewDetachedFromWindow(v: View) {}
        })
    }
}

// =======================  NavigationBar begin ↓ =========================
/**
 * 老的方法获取导航栏的高度
 */
val Context.navigationBarHeight:Int
    get() {
        var result = 0
        runCatching {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
        }
        return result

    }

fun Activity.immersiveNavigationBar(){
    runCatching {
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.hide(WindowInsetsCompat.Type.navigationBars())
    }.onFailure {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val decorView = window.decorView
            decorView.systemUiVisibility = (decorView.systemUiVisibility
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.navigationBarColor = Color.TRANSPARENT
        }
    }
}

/**
 * 获取底部导航栏的高度
 */
fun Activity.getNavigationBarHeight(callback: (navigationBarHeight:Int)->Unit) {
    getNavigationBarHeight(findViewById<View>(R.id.content), callback)
}

/**
 * 获取底部导航栏的高度
 */
fun getNavigationBarHeight(view: View, callback: (navigationBarHeight:Int)->Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
        val navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
        val top = navInsets.top
        val bottom = navInsets.bottom
        val height = Math.abs(bottom - top)
        if (height > 0) {
            callback(height)
        } else {
            callback(view.context.navigationBarHeight)
        }
        insets
    }
}

// =======================  NavigationBar StatusBar Hide Show begin ↓ =========================
/**
 * 显示隐藏底部导航栏（注意不是沉浸式效果）
 */
fun showHideNavigationBar(activity: Activity, isShow: Boolean) {
    val decorView = activity.findViewById<View>(R.id.content)
    val controller = ViewCompat.getWindowInsetsController(decorView)
    if (controller != null) {
        if (isShow) {
            controller.show(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
        } else {
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

/**
 * 显示隐藏顶部的状态栏（注意不是沉浸式效果）
 */
fun showHideStatusBar(activity: Activity, isShow: Boolean) {
    val decorView = activity.findViewById<View>(R.id.content)
    val controller = ViewCompat.getWindowInsetsController(decorView)
    if (controller != null) {
        if (isShow) {
            controller.show(WindowInsetsCompat.Type.statusBars())
        } else {
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }
    }
}

/**
 * 当前是否显示了底部导航栏
 */
fun hasNavigationBars(activity: Activity, callback: (hasNav:Boolean)->Unit) {
    val decorView = activity.findViewById<View>(R.id.content)
    val attachedToWindow = decorView.isAttachedToWindow
    if (attachedToWindow) {
        val windowInsets = ViewCompat.getRootWindowInsets(decorView)
        if (windowInsets != null) {
            val hasNavigationBar =
                windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                        windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0
            callback(hasNavigationBar)
        }
    } else {
        decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                val windowInsets = ViewCompat.getRootWindowInsets(v)
                if (windowInsets != null) {
                    val hasNavigationBar =
                        windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                                windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0
                    callback(hasNavigationBar)
                }
            }
            override fun onViewDetachedFromWindow(v: View) {}
        })
    }
}