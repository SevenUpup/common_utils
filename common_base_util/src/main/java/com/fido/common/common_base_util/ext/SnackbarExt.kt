package com.fido.common.common_base_util.ext

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Snackbar 扩展函数
 */
/*
  ---------- from anko ----------
 */
/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
fun snackbar(view: View, @StringRes message: Int) = Snackbar
    .make(view, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
fun longSnackbar(view: View, @StringRes message: Int) = Snackbar
    .make(view, message, Snackbar.LENGTH_LONG)
    .apply { show() }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
fun snackbar(view: View, message: String) = Snackbar
    .make(view, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
fun longSnackbar(view: View, message: String) = Snackbar
    .make(view, message, Snackbar.LENGTH_LONG)
    .apply { show() }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text resource.
 */
inline fun snackbar(view: View, @StringRes message: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) = Snackbar
    .make(view, message, Snackbar.LENGTH_SHORT)
    .apply {
        setAction(actionText, action)
        show()
    }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text resource.
 */
inline fun longSnackbar(view: View, @StringRes message: Int, @StringRes actionText: Int, noinline action: (View) -> Unit) = Snackbar
    .make(view, message, Snackbar.LENGTH_LONG)
    .apply {
        setAction(actionText, action)
        show()
    }

/**
 * Display the Snackbar with the [Snackbar.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
inline fun snackbar(view: View, message: String, actionText: String, noinline action: (View) -> Unit) = Snackbar
    .make(view, message, Snackbar.LENGTH_SHORT)
    .apply {
        setAction(actionText, action)
        show()
    }

/**
 * Display Snackbar with the [Snackbar.LENGTH_LONG] duration.
 *
 * @param message the message text.
 */
inline fun longSnackbar(view: View, message: String, actionText: String, noinline action: (View) -> Unit) = Snackbar
    .make(view, message, Snackbar.LENGTH_LONG)
    .apply {
        setAction(actionText, action)
        show()
    }