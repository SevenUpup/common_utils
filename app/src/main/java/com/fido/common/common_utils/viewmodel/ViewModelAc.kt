package com.fido.common.common_utils.viewmodel

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.util.ETMoneyValueFilter
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcViewModelBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ViewModelAc : BaseVBActivity<AcViewModelBinding>() {

    //    private val mViewModel:MViewModel by viewModels()
    private val mViewModel: MViewModel by lazy {
        ViewModelProvider(this)[MViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.ac_view_model
    }

    override fun initView() {
        binding.viewModel = mViewModel
        binding.clickProxy = ClickProxy()
    }

    override fun initData() {}

    var index = 1
    override fun initEvent() {
        binding.mInput.doAfterTextChanged {
            toast(it.toString())
            index++
            mViewModel.bool.postValue(index %2 == 0)
            mViewModel.mResult.postValue(it.toString())
            lifecycleScope.launch {
                mViewModel.data.emit(it.toString())
            }
        }
        lifecycleScope.launch {
            mViewModel.data.collect{
                binding.mTv.text = it
            }
        }
    }
}

class ClickProxy{
    fun MClick( ){
        toast("click is ready")
    }
}

class MViewModel : ViewModel() {
    var mResult = MutableLiveData<String>()
    var data = MutableSharedFlow<String>()
    val bool = MutableLiveData<Boolean>()
}
