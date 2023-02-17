package com.fido.common.common_base_util.interfaces

import android.content.Context
import androidx.startup.Initializer
import com.fido.common.common_base_util.app

internal class BaseInitiallizer :Initializer<Unit>{
    override fun create(context: Context) {
        app = context
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}