package com.fido.common.common_base_ui.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.fido.common.common_base_ui.R

abstract class BaseDialogFragment<VB:ViewDataBinding>:DialogFragment() {

    protected lateinit var binding:VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        dialog?.window?.attributes?.let {
            it.dimAmount = 0.6f
            it.gravity= Gravity.BOTTOM
            it.windowAnimations = R.style.Common_DialogAnim
        }

        init(view)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,initLayoutId(),container,false)
        return binding.root
    }

    abstract fun initLayoutId():Int

    abstract fun init(rootView: View)
}