package com.fido.common.common_base_util.util.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.ext.logD

/**
 * @author: FiDo
 * @date: 2024/4/19
 * @des:
 */
object NetWorkChangeUtils {

    private val onNetworkChangedList = mutableSetOf<(Boolean)->Unit>()
    private val networkChangeReceiverMap = mutableMapOf<Context?,NetworkChangeReceiver?>()

    @JvmStatic
    fun registerNetworkChangeReceiver(context: Context?,onNetChange:(available:Boolean)->Unit){
        if (context == null) return
        logD("registerNetworkChangeReceiver context=${context}")
        onNetworkChangedList.add(onNetChange)
        var networkChangeReceiver = networkChangeReceiverMap[context]
        if (networkChangeReceiver == null) {
            networkChangeReceiver = NetworkChangeReceiver()
            networkChangeReceiverMap[context] = networkChangeReceiver
        }
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(networkChangeReceiver, intentFilter)
        when (context) {
            is Fragment ->{
                context.lifecycle.addObserver(object :LifecycleEventObserver{
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            unregisterNetworkChangeReceiver(context)
                        }
                    }
                })
            }
            is AppCompatActivity ->{
                context.lifecycle.addObserver(object :LifecycleEventObserver{
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            unregisterNetworkChangeReceiver(context)
                        }
                    }
                })
            }
        }
    }

    private class NetworkChangeReceiver : BroadcastReceiver() {
        private var currentNetworkAvailable = isNetworkAvailable
        override fun onReceive(context: Context, intent: Intent) {
            val networkAvailable = isNetworkAvailable
            if (currentNetworkAvailable xor networkAvailable) {
                currentNetworkAvailable = networkAvailable
                if (onNetworkChangedList.isNotEmpty()) {
                    onNetworkChangedList.forEach {
                        it?.invoke(currentNetworkAvailable)
                    }
                }
            }
        }

        private val isNetworkAvailable: Boolean
            get() {
                val connectivityManager = app
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isAvailable
            }
    }

    @JvmStatic
    fun unregisterNetworkChangeReceiver(context: Context?){
        logD("unregisterNetworkChangeReceiver context=${context}")
        if (context == null) return
        val receiver = networkChangeReceiverMap[context]
        if (receiver != null) {
            context.unregisterReceiver(receiver)
            networkChangeReceiverMap[context] = null
            networkChangeReceiverMap.remove(context)
        }
    }

}