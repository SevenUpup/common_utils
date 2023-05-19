package com.fido.common.common_base_util.interfaces

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.ext.OnAppStatusChangedListener
import com.fido.common.common_base_util.ext.activityCache
import com.fido.common.common_base_util.ext.doOnActivityLifeCycle

internal class BaseInitiallizer :Initializer<Unit>{

    private var started = 0
    override fun create(context: Context) {
        app = context as Application
        app.doOnActivityLifeCycle(
            onActivityCreated = { activity, _ ->
                activityCache.add(activity)
            },
            onActivityStarted = { activity ->
                started++
                if (started == 1) {
                    onAppStatusChangedListener?.onForeground(activity)
                }
            },
            onActivityStopped = { activity ->
                started--
                if (started == 0) {
                    onAppStatusChangedListener?.onBackground(activity)
                }
            },
            onActivityDestroyed = { activity ->
                activityCache.remove(activity)
            }
        )

    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

    companion object{
        internal var onAppStatusChangedListener:OnAppStatusChangedListener?=null
    }

}