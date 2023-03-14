package com.fido.common.common_base_util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_util.ext.toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * author: FiDo
 *  date :  2021/4/17 0017.10:05
 *  desc :
 */
@SuppressLint("StaticFieldLeak")
lateinit var app: Context

/*converts dp value into px*/
val Number.dp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/*converts sp value into px*/
val Number.sp
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.scaledDensity).toInt()

//</editor-fold>

//<editor-fold desc="Memory">

val Number.KB: Long
    get() = this.toLong() * 1024L

val Number.MB: Long
    get() = this.KB * 1024

val Number.GB: Long
    get() = this.MB * 1024

/** dp和px转换 **/
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    return (spValue * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Fragment.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun Fragment.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun Fragment.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}

fun View.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun View.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun View.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Int {
    return itemView.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.sp2px(dpValue: Float): Int {
    return itemView.sp2px(dpValue)
}

fun RecyclerView.ViewHolder.px2sp(pxValue: Float): Int {
    return itemView.px2sp(pxValue)
}

fun ViewModel.px2dp(pxValue: Float): Int {
    return app.px2dp(pxValue)
}

fun ViewModel.dp2px(dpValue: Float): Int {
    return app.dp2px(dpValue)
}

fun ViewModel.sp2px(dpValue: Float): Int {
    return app.sp2px(dpValue)
}

fun ViewModel.px2sp(pxValue: Float): Int {
    return app.px2sp(pxValue)
}


/** 动态创建Drawable **/
fun Context.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    val content = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
        setStroke(strokeWidth, strokeColor)
    }
    if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
        return RippleDrawable(ColorStateList.valueOf(rippleColor), content, null)
    }
    return content
}

fun Fragment.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun View.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun RecyclerView.ViewHolder.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return itemView.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

//内联函数+标注泛型 = 泛型实例化
inline fun <reified T> String.toBean() = Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)


/** Window相关 **/
fun Context.windowWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun Context.windowHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}

fun Fragment.windowWidth(): Int {
    return context!!.windowWidth()
}

fun Fragment.windowHeight(): Int {
    return context!!.windowHeight()
}

fun View.windowWidth(): Int {
    return context!!.windowWidth()
}

fun View.windowHeight(): Int {
    return context!!.windowHeight()
}

fun RecyclerView.ViewHolder.windowWidth(): Int {
    return itemView.windowWidth()
}

fun RecyclerView.ViewHolder.windowHeight(): Int {
    return itemView.windowHeight()
}


fun <T> Collection<T>.print() =
    println(StringBuilder("\n[").also { sb ->
        this.forEach { e -> sb.append("\n\t${e.toString()},") }
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

val Int.getColor: Int
    get() = ContextCompat.getColor(app, this)

val String.getColor: Int
    get() = Color.parseColor(this)

val Int.getDrawable:Drawable
    get() = ContextCompat.getDrawable(app,this)?:app.resources.getDrawable(this,null)

fun Context.getDrawable(drawableId:Int) = ContextCompat.getDrawable(app,drawableId)

//获取屏幕 宽 / 高（px）
fun Context.getScreenWidthPx() = resources.displayMetrics.widthPixels

fun Context.getScreenHeightPx(): Int = this.resources.displayMetrics.heightPixels

//获取屏幕 宽 / 高（dp）
fun Context.getScreenWidthDp(): Int = px2dp(getScreenWidthPx())

fun Context.getScreenHeightDp(): Int = px2dp(getScreenHeightPx())

fun Float.dp2px() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    Resources.getSystem().displayMetrics
)

fun Int.dp2px() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).toInt()

//根据手机的分辨率从 px(像素) 的单位 转成为 dp
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5F).toInt()
}

inline fun <reified T> Resources.dpTopx(value: Int): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(), displayMetrics
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("type is not supported")
    }
}

// inflate
fun Context.inflateLayout(
    @LayoutRes layoutId: Int,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
) =
    LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

// startActivity/startService
inline fun <reified T : Activity> Context?.startActivity() =
    this?.startActivity(Intent(this, T::class.java))

inline fun <reified T : Service> Context?.startService() =
    this?.startService(Intent(this, T::class.java))

/**
 * 数组转bundle
 */
fun Array<out Pair<String, Any?>>.toBundle(): Bundle? {
    return Bundle().apply {
        forEach { it ->
            val value = it.second
            when (value) {
                null -> putSerializable(it.first, null as Serializable?)
                is Int -> putInt(it.first, value)
                is Long -> putLong(it.first, value)
                is CharSequence -> putCharSequence(it.first, value)
                is String -> putString(it.first, value)
                is Float -> putFloat(it.first, value)
                is Double -> putDouble(it.first, value)
                is Char -> putChar(it.first, value)
                is Short -> putShort(it.first, value)
                is Boolean -> putBoolean(it.first, value)
                is Serializable -> putSerializable(it.first, value)
                is Parcelable -> putParcelable(it.first, value)

                is IntArray -> putIntArray(it.first, value)
                is LongArray -> putLongArray(it.first, value)
                is FloatArray -> putFloatArray(it.first, value)
                is DoubleArray -> putDoubleArray(it.first, value)
                is CharArray -> putCharArray(it.first, value)
                is ShortArray -> putShortArray(it.first, value)
                is BooleanArray -> putBooleanArray(it.first, value)

                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(
                        it.first,
                        value as Array<CharSequence>
                    )
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(
                        it.first,
                        value as Array<Parcelable>
                    )
                }
            }
        }
    }
}

//data class 对象的深拷贝
fun <T : Any> T.deepCopy(): T {
    //如果不是数据类，直接返回
    if (!this::class.isData) {
        return this
    }

    //拿到构造函数
    return this::class.primaryConstructor!!.let { primaryConstructor ->
        primaryConstructor.parameters.map { parameter ->
            //转换类型

            //最终value=第一个参数类型的对象
            val value = (this::class as KClass<T>).memberProperties.first {
                it.name == parameter.name
            }.get(this)

            //如果当前类(这里的当前类指的是参数对应的类型，比如说这里如果非基本类型时)是数据类
            if ((parameter.type.classifier as? KClass<*>)?.isData == true) {
                parameter to value?.deepCopy()
            } else {
                parameter to value
            }

            //最终返回一个新的映射map,即返回一个属性值重新组合的map，并调用callBy返回指定的对象
        }.toMap().let(primaryConstructor::callBy)
    }
}

/**
 * 主线程运行
 */
fun Any.runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}

/**
 * 检查是否有网络-直接检查全局内存
 */
fun Any.checkNet(
    block: () -> Unit,
    msg: String = "Network connection error, please check the network connection"
) {
    if (isNetworking()) {
        block()
    } else {
        runMain {
            toast(msg)
        }
    }
}

/**
 * 是否处于联网中
 */
fun isNetworking(): Boolean {
    if (::app.isInitialized) {
        val connectivityManager =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(network) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.isConnected == true
            }
        } catch (e: Exception) {
            true
        }
    }
    return true
}