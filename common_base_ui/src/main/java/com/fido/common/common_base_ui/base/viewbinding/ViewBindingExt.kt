package com.fido.common.common_base_ui.base.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

/**
@author FiDo
@description:
@date :2023/5/18 17:39
 */

inline fun <reified VB:ViewBinding>ComponentActivity.binding(setContentView:Boolean = true) = lazy(LazyThreadSafetyMode.NONE) {
    inflateBinding<VB>(layoutInflater).also {binding->
        if(setContentView) setContentView(binding.root)
        if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
    inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        .invoke(null, layoutInflater, parent, attachToParent) as VB