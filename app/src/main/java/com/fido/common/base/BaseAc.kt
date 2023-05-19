package com.fido.common.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
@author FiDo
@description:
@date :2023/4/13 10:57
 */
abstract class BaseAc<VB:ViewDataBinding>:AppCompatActivity() {

    protected inline fun<reified T:ViewDataBinding> binding(@LayoutRes layoutId:Int) = lazy {
        DataBindingUtil.setContentView<T>(this,layoutId).apply {
            this.lifecycleOwner = this@BaseAc
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



}