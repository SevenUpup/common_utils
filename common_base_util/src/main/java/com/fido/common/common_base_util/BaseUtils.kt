package com.fido.common.common_base_util

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import java.lang.IllegalStateException

/**
 * author: FiDo
 *  date :  2021/4/17 0017.10:05
 *  desc :
 */
object BaseUtils {

    fun <T>Collection<T>.print() =
            println(StringBuilder("\n[").also { sb->
                this.forEach { e-> sb.append("\n\t${e.toString()},") }
                sb.append("\n]")
            }.toString())

    fun <K, V> Map<K, V?>.print(map: (V?) -> String): String =
            StringBuilder("\n{").also { sb ->
                this.iterator().forEach { entry ->
                    sb.append("\n\t[${entry.key}] = ${map(entry.value)}")
                }
                sb.append("\n}")
            }.toString()

    //  ================================== Context ===================================

    // 获取颜色
    fun Context.getColorCompat(colorInt: Int) = ContextCompat.getColor(this, colorInt)

    //获取屏幕 宽 / 高（px）
    fun Context.getScreenWidthPx() = resources.displayMetrics.widthPixels

    fun Context.getScreenHeightPx(): Int = this.resources.displayMetrics.heightPixels

    //获取屏幕 宽 / 高（dp）
    fun Context.getScreenWidthDp(): Int = px2dp(getScreenWidthPx())

    fun Context.getScreenHeightDp(): Int = px2dp(getScreenHeightPx())

    fun Float.dp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

    fun Int.dp2px() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

    //根据手机的分辨率从 px(像素) 的单位 转成为 dp
    fun Context.px2dp(px:Int): Int {
        val scale = resources.displayMetrics.density
        return (px / scale + 0.5F).toInt()
    }

    // 使用 reified，可以实现不同的返回类型函数重载
//    val intValue: Int = resource.dpToPx(64)
//    val floatValue: Float = resource.dpToPx(64)
    inline fun <reified T> Resources.dpTopx(value:Int):T{
        val result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(), displayMetrics)

        return when (T::class) {
            Float::class -> result as T
            Int::class -> result.toInt() as T
            else -> throw IllegalStateException("type is not supported")
        }
    }

    // inflate
    fun Context.inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false) =
            LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

    // startActivity/startService
    inline fun <reified T : Activity> Context?.startActivity() = this?.startActivity(Intent(this, T::class.java))

    inline fun <reified T : Service> Context?.startService() = this?.startService(Intent(this, T::class.java))


}