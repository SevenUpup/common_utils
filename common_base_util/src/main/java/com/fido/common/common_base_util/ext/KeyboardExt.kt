package com.fido.common.common_base_util.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

/**
 * 软键盘操作 扩展函数
 */


// ======================= 针对 Android 11 及以上 ，否则调用无效 ==========================
fun Activity.showKeyboard() = changeKeyboard(true)
fun Activity.hideKeyboard() = changeKeyboard(false)

fun Activity.changeKeyboard(isShow:Boolean){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (isShow){
            window?.insetsController?.show(WindowInsets.Type.ime())
        } else{
            window?.insetsController?.hide(WindowInsets.Type.ime())
        }
    } else {
        if (isShow) {
            getInsetsController()?.show(WindowInsetsCompat.Type.ime())
        } else {
            getInsetsController()?.hide(WindowInsetsCompat.Type.ime())
        }
        /*ViewCompat.getWindowInsetsController(contentView).let { controller ->
            if (isShow){
                controller?.show(WindowInsetsCompat.Type.ime())
            } else{
                controller?.hide(WindowInsetsCompat.Type.ime())
            }
        }*/
    }
}
fun Activity.getInsetsController() = WindowCompat.getInsetsController(window,window.decorView)
// ======================== Android 10 好像没效果 ==========================

/*
  ---------- Context ----------
 */
fun Context.showSoftInput(view: View) {
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
    inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun Context.hideSoftInput(view: View) {
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideSoftInput(activity: Activity) {
    val view: View = activity.currentFocus ?: activity.window.decorView
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.isSoftInputActive(): Boolean {
    return inputMethodManager?.isActive ?: false
}

/*
  ---------- Fragment ----------
 */

fun Fragment.showSoftInput(view: View) {
    activity?.showSoftInput(view)
}

fun Fragment.hideSoftInput(view: View) {
    activity?.hideSoftInput(view)
}

fun Fragment.hideSoftInput() {
    activity?.hideSoftInput(activity!!)
}

fun Fragment.isSoftInputActive() {
    activity?.isSoftInputActive()
}


/*
  ---------- View ----------
 */
fun View.showSoftInput(view: View) {
    context.showSoftInput(view)
}

fun View.hideSoftInput(view: View) {
    context.hideSoftInput(view)
}

fun View.isSoftInputActive() {
    context.isSoftInputActive()
}