package com.fido.common.common_utils.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.fido.common.common_base_util.ext.loge

/**
@author FiDo
@description:
@date :2023/9/7 16:21
 */
class AdGunAccessibilityService : AccessibilityService() {

    companion object{
        var instance:AdGunAccessibilityService?=null
        val isServiceEnable get() = instance != null
    }

    /**
     * 获得当前视图根节点
     * */
    private fun getCurrentRootNode() = try {
        rootInActiveWindow
    } catch (e: Exception) {
        e.message?.let { Log.e("FiDo", it) }
        null
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            getCurrentRootNode()?.findAccessibilityNodeInfosByText("跳过").takeUnless { it.isNullOrEmpty() }?.get(0).let {
                loge("检测到跳过广告结点：$it")
                it?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance  = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

}