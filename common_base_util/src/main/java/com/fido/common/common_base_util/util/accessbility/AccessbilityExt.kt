package com.fido.common.common_base_util.util.accessbility

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.fido.common.common_base_util.app

/**
 * @param cls  AccessbilityService
 * @param ctx  Context
 */
fun jumpAccessibilityServiceSettings(
    cls:Class<*>,
    ctx: Context
) {
    ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val cs = ComponentName(app.packageName,cls.name).flattenToString()
        putExtra(":settings:fragment_args_key", cs)
        putExtra(":settings:show_fragment_args", Bundle().apply { putString(":settings:fragment_args_key", cs) })
    })

}