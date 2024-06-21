package com.fido.common.common_utils.jetpack

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.logd
import com.fido.common.databinding.AcJetpackBinding
import com.fido.common.databinding.FragmentJetpackageFlowBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @author: FiDo
 * @date: 2024/6/17
 * @des:
 */
class JetPackAc:AppCompatActivity() {

    private val binding:AcJetpackBinding by binding()
    private val mViewModel:JetpackageViewModel by viewModels<JetpackageViewModel>()

    companion object{
        val fragmentList = mutableListOf<Fragment>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeValue()

        fragmentList.add(EasyFragment.getInstance(R.layout.fragment_jetpackage_flow){
            val tvInfo = it.findViewById<TextView>(R.id.tv_info)
            val btSend = it.findViewById<Button>(R.id.bt_send)
            btSend.click {
                mViewModel.changeSearch("666")
            }

        })
        fragmentList.add(EasyBindingFragment.getInstance<FragmentJetpackageFlowBinding>(R.layout.fragment_jetpackage_flow){

        })
        binding.apply {
            btFlow.click {
                com.fido.common.common_base_util.ext.startActivity<JetPackageContainerAc>(Pair("index",0))
            }
        }
    }

    private fun observeValue() {
//        mViewModel.searchLD.observe(this){
//            logd("LiveData-value $it")
//        }

        //        lifecycleScope.launch {
//            mViewModel.sharedFlow.collect{
//                logd("SharedFlow-value $it")
//            }
//        }
//
//        lifecycleScope.launch{
//            mViewModel.newSharedFlow.collect{
//                logd("NewSharedFlow -value $it")
//            }
//        }
//
//        lifecycleScope.launch {
//            mViewModel.searchFlow.collect{
//                logd("StateFlow -value $it")
//            }
//        }


        lifecycleScope.launch {
            mViewModel.searchFlow.collect {
                logd("StateFlow -value $it")
            }
        }

        mViewModel.searchLD.observeForever {
            logd("LiveData-value $it")
        }

        lifecycleScope.launch {
            mViewModel.sharedFlow.collect {
                logd("SharedFlow-value $it")
            }

        }

    }


}

class JetpackageViewModel :ViewModel(){

    private val _searchLD = MutableLiveData<String>()
    val searchLD:LiveData<String> = _searchLD

    private val _searchFlow = MutableStateFlow("")
    val searchFlow:StateFlow<String> = _searchFlow

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow: SharedFlow<String> = _sharedFlow

    /**
     * stateflow 默认防抖，发送相同数据时不打印
     * SharedFlow 可以通过自定义实现StateFlow的效果
     */
    private val _newSharedFlow = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val newSharedFlow by lazy {
        //效果类似StateFlow
        _newSharedFlow.distinctUntilChanged()
    }

    fun changeSearch(keyword: String) {
        _sharedFlow.tryEmit(keyword)
        _searchFlow.value = keyword
        _searchLD.value = keyword
        _newSharedFlow.tryEmit(keyword)
    }

}
