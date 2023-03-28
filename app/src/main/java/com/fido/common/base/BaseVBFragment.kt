package com.fido.common.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseVBFragment<VB:ViewDataBinding>(@LayoutRes layoutRes: Int = 0):Fragment(layoutRes) {

    protected lateinit var binding:VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        try{
            initView()
            initData()
        }catch (e:Exception){
            Log.e("BaseVBFragment", "Initializing failure ",e )
        }
    }

    abstract fun initView()
    abstract fun initData()

}