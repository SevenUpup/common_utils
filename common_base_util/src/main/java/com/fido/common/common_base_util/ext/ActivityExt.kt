/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package com.fido.common.common_base_util.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PermissionResult
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.toBundle
import java.util.*

internal val activityCache = LinkedList<Activity>()

fun startActivity(intent: Intent) = topActivity.startActivity(intent)

inline fun <reified T : Activity> startActivity(
    vararg pairs: Pair<String, Any?>,
    crossinline block: Intent.() -> Unit = {}
) =
    topActivity.startActivity<T>(pairs = pairs, block = block)

inline fun <reified T : Activity> Context.startActivity(
    vararg pairs: Pair<String, Any?>,
    crossinline block: Intent.() -> Unit = {}
) =
    startActivity(intentOf<T>(*pairs).apply(block))

fun Activity.finishWithResult(vararg pairs: Pair<String, *>) {
    setResult(Activity.RESULT_OK, Intent().putExtras(bundleOf(*pairs)))
    finish()
}

val activityList: List<Activity> get() = activityCache.toList()

val topActivity: Activity get() = activityCache.last()

val topActivityOrNull: Activity? get() = activityCache.lastOrNull()

val topActivityOrApplication: Context get() = topActivityOrNull ?: app

inline fun <reified T : Activity> isActivityExistsInStack(): Boolean =
    isActivityExistsInStack(T::class.java)

fun <T : Activity> isActivityExistsInStack(clazz: Class<T>): Boolean =
    activityCache.any { it.javaClass.name == clazz.name }

inline fun <reified T : Activity> finishActivity(): Boolean = finishActivity(T::class.java)

fun <T : Activity> finishActivity(clazz: Class<T>): Boolean =
    activityCache.removeAll {
        if (it.javaClass.name == clazz.name) it.finish()
        it.javaClass.name == clazz.name
    }

inline fun <reified T : Activity> finishToActivity(): Boolean = finishToActivity(T::class.java)

fun <T : Activity> finishToActivity(clazz: Class<T>): Boolean {
    for (i in activityCache.count() - 1 downTo 0) {
        if (clazz.name == activityCache[i].javaClass.name) {
            return true
        }
        activityCache.removeAt(i).finish()
    }
    return false
}

fun finishAllActivities(): Boolean =
    activityCache.removeAll {
        it.finish()
        true
    }

inline fun <reified T : Activity> finishAllActivitiesExcept(): Boolean =
    finishAllActivitiesExcept(T::class.java)

fun <T : Activity> finishAllActivitiesExcept(clazz: Class<T>): Boolean =
    activityCache.removeAll {
        if (it.javaClass.name != clazz.name) it.finish()
        it.javaClass.name != clazz.name
    }

fun finishAllActivitiesExceptNewest(): Boolean =
    finishAllActivitiesExcept(topActivity.javaClass)

fun ComponentActivity.pressBackTwiceToExitApp(
    toastText: String,
    delayMillis: Long = 2000,
    owner: LifecycleOwner = this
) =
    pressBackTwiceToExitApp(delayMillis, owner) { toast(toastText) }

fun ComponentActivity.pressBackTwiceToExitApp(
    @StringRes toastText: Int,
    delayMillis: Long = 2000,
    owner: LifecycleOwner = this
) =
    pressBackTwiceToExitApp(delayMillis, owner) { toast(toastText) }

fun ComponentActivity.pressBackTwiceToExitApp(
    delayMillis: Long = 2000,
    owner: LifecycleOwner = this,
    onFirstBackPressed: () -> Unit
) =
    onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        private var lastBackTime: Long = 0

        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackTime > delayMillis) {
                onFirstBackPressed()
                lastBackTime = currentTime
            } else {
                finishAllActivities()
            }
        }
    })

fun ComponentActivity.pressBackToNotExitApp(owner: LifecycleOwner = this) =
    doOnBackPressed(owner) { moveTaskToBack(false) }

fun ComponentActivity.doOnBackPressed(owner: LifecycleOwner = this, onBackPressed: () -> Unit) =
    onBackPressedDispatcher.addCallback(owner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = onBackPressed()
    })

fun Context.isPermissionGranted(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.arePermissionsGranted(vararg permissions: String): Boolean =
    permissions.all { isPermissionGranted(it) }

fun Context.asActivity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?.asActivity()

var Activity.decorFitsSystemWindows: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(value) = WindowCompat.setDecorFitsSystemWindows(window, value)

inline val Activity.contentView: View
    get() = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

@Deprecated("Use `Context.asActivity()` instead.", ReplaceWith("asActivity()"))
val Context.activity: Activity? get() = asActivity()

inline val Context.context: Context get() = this

inline val Activity.activity: Activity get() = this

inline val FragmentActivity.fragmentActivity: FragmentActivity get() = this

inline val ComponentActivity.lifecycleOwner: LifecycleOwner get() = this

//内联函数+标注泛型 = 泛型实例化
inline fun <reified T> Fragment.gotoActivity(
    flag: Int = -1,
    bundle: Array<out Pair<String, Any?>>? = null
) {
    activity?.gotoActivity<T>(flag, bundle)
}

inline fun <reified T> View.gotoActivity(
    flag: Int = -1,
    bundle: Array<out Pair<String, Any?>>? = null
) {
    context.gotoActivity<T>(flag, bundle)
}

inline fun <reified T> Context.gotoActivity(
    flag: Int = -1,
    bundle: Array<out Pair<String, Any?>>? = null
) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (this@gotoActivity !is Activity) {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle != null) {
            putExtras(bundle.toBundle()!!)
        }
    }
    startActivity(intent)
}

/**
 * @param packageName 根据包名跳转指定App
 * Android11 及以上
 * 方式一：
 * <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
 * 方式二：
 * AndroidManifest.xml文件里面配置上跳转APP的包名
 * <queries >
 *     <package android:name="com.tencent.mm"/>
 * </queries>
 */
fun Context.startTargetAppForName(packageName: String) {
    kotlin.runCatching {
        val intent = packageManager?.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        }
    }.onFailure {
      loge(it.message.toString())
    }

}