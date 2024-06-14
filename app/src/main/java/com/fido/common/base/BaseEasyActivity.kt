package com.fido.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @author: FiDo
 * @date: 2024/6/12
 * @des:
 */
abstract class BaseEasyActivity<VM:ViewModel,VB:ViewDataBinding> :AppCompatActivity(){

    protected lateinit var viewModel:VM
    protected lateinit var binding:VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //反射创建 VM & VB
        viewModel = getViewModelInstance()
        binding = getViewBindingInstance(layoutInflater)

        setContentView(binding.root)
    }

    private fun getViewBindingInstance(inflater: LayoutInflater): VB {
        val superClass = javaClass.genericSuperclass as ParameterizedType
        val vbClass = superClass.actualTypeArguments[1] as Class<VB>
        val method = vbClass.getMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, inflater) as VB
    }

    private fun getViewModelInstance(): VM {
        val superClass  = javaClass.genericSuperclass as ParameterizedType
        val vmClass = (superClass.actualTypeArguments[0] as Class<VM>).kotlin
        return ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[vmClass.java]
    }



}