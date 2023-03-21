package com.fido.common.common_base_util.ext

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.io.InputStream

/*
  ---------- Context ----------
 */
fun Context.loadColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.loadDrawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

fun Context.loadRaw(@RawRes id: Int): InputStream? = resources.openRawResource(id)

fun Context.loadRaw(@RawRes id: Int, value: TypedValue): InputStream? = resources.openRawResource(id, value)

fun Context.loadAsset(fileName: String, accessMode: Int = AssetManager.ACCESS_STREAMING): InputStream? {
    return try {
        assets.open(fileName, accessMode)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun Context.loadTypefaceFromAsset(fileName: String): Typeface = Typeface.createFromAsset(assets, fileName)

fun loadTypefaceFromFile(filePath: String): Typeface = Typeface.createFromFile(filePath)

fun loadTypefaceFromFile(file: File): Typeface = Typeface.createFromFile(file)


/*
  ---------- Fragment ----------
 */
fun Fragment.loadColor(@ColorRes id: Int): Int = activity?.loadColor(id) ?: 0

fun Fragment.loadDrawable(@DrawableRes id: Int): Drawable? = activity?.loadDrawable(id)

fun Fragment.loadRaw(@RawRes id: Int): InputStream? = activity?.loadRaw(id)

fun Fragment.loadRaw(@RawRes id: Int, value: TypedValue): InputStream? = activity?.loadRaw(id, value)

fun Fragment.loadAsset(fileName: String, accessMode: Int = AssetManager.ACCESS_STREAMING): InputStream? = activity?.loadAsset(fileName, accessMode)

fun Fragment.loadTypefaceFromAsset(fileName: String): Typeface? = activity?.loadTypefaceFromAsset(fileName)


/*
  ---------- View ----------
 */
fun View.loadColor(@ColorRes id: Int): Int = context.loadColor(id)

fun View.loadDrawable(@ColorRes id: Int): Drawable? = context.loadDrawable(id)

fun View.loadRaw(@RawRes id: Int): InputStream? = context.loadRaw(id)

fun View.loadRaw(@RawRes id: Int, value: TypedValue): InputStream? = context.loadRaw(id, value)

fun View.loadAsset(fileName: String, accessMode: Int = AssetManager.ACCESS_STREAMING): InputStream? = context.loadAsset(fileName, accessMode)

fun View.loadTypefaceFromAsset(fileName: String): Typeface = context.loadTypefaceFromAsset(fileName)
