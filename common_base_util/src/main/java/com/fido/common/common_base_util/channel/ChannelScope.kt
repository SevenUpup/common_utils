package com.fido.common.common_base_util.channel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.runMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

@PublishedApi
internal open class ChannelScope() :CoroutineScope{
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob()

    var cancelBlock:(()->Unit)?=null

    constructor(
        lifecycleOwner: LifecycleOwner,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) : this() {
        runMain {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (lifeEvent == event) {
                        cancelBlock?.invoke()
                        cancel()
                    }
                }
            })
        }
    }

}