package com.fido.common.common_base_util.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.fido.common.common_base_util.toBundle

/**
 * Activity相关
 */

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
