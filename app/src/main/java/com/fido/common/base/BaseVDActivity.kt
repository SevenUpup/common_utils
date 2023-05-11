package com.fido.common.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.fido.common.common_base_util.ext.getVMCls

/**
@author FiDo
@description:
@date :2023/4/13 14:56
 */
abstract class BaseVDActivity<VM:BaseViewModel,DB:ViewDataBinding>:BaseVBActivity<DB>() {

    protected lateinit var viewModel:VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = createVM()

    }

    private fun createVM(): VM = ViewModelProvider(this)[getVMCls(this)]


}