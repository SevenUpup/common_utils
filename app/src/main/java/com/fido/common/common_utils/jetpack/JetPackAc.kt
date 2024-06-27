package com.fido.common.common_utils.jetpack

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.imageview.load
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.lifecycleOwner
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.eventbus.flow_bus.FlowBus
import com.fido.common.databinding.AcJetpackBinding
import com.fido.common.databinding.FragmentCoroutineTestBinding
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

    private var flowFragment:EasyBindingFragment<FragmentJetpackageFlowBinding>?=null
    private var coroutineFragment:EasyBindingFragment<FragmentCoroutineTestBinding>?=null

    companion object{
        val fragmentList = mutableListOf<Fragment>()
    }

    private val _tag = 66
    private val _tagStr = mutableListOf(1,2,3)
    var tag = _tag
    var tagStr = _tagStr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeValue()

        flowFragment = EasyBindingFragment.getInstance<FragmentJetpackageFlowBinding>(R.layout.fragment_jetpackage_flow){
            btSend.click {
                //值传递，tag会拷贝一份_tag的值，所以不会影响_tag的值
                tag --
                //引用传递，修改tagStr 会修改 _tagStr
                if (tagStr.size !=0) {
                    tagStr.removeAt(0)
                }
                loge("tag:${tag} _tag:${_tag} tagStr:${tagStr} _tagStr:${_tagStr}\ntagStr=${tagStr.hashCode()} _tagStr=${_tagStr.hashCode()}")
                mViewModel.changeSearch("666")
            }

            btSendFlowBus.click {
                sendBus()
            }
            btSendStickFlowBus.click {
                sendStickBus()
            }
            btRegisterFlowBus.click {
                registerFlowBus()
            }
            btRegisterStickFlowBus.click {
                registerFlowStickBus()
            }
        }
        fragmentList.add(flowFragment!!)

        val imgUrl = "https://gd-hbimg.huaban.com/5c94c28ad72061a418df7525079fecb2b42a296116b61-b7nVlM_fw658webp"
        coroutineFragment = EasyBindingFragment.getInstance<FragmentCoroutineTestBinding>(R.layout.fragment_coroutine_test){

            logd("""
                    coroutineFragment viewLifecycleOwner =${coroutineFragment?.viewLifecycleOwner}
                    parentAc lifecycleOwner = ${coroutineFragment?.activity?.lifecycleOwner}
                """.trimIndent())

            btWithTimeout.click {
                startTimeoutCoroutine()
            }
            btGlideWithTimeout.click {
                CoroutineTest.getInstance().downloadImgMaybeTimeout(coroutineFragment!!.viewLifecycleOwner,imgUrl,10*1000){
                    ivGlideLoad.load(it, isCenterCrop = true)
                    tvImgInfo.text = it.absolutePath
                }
            }
            btGlideWithTimeoutFail.click {
                CoroutineTest.getInstance().downloadImgMaybeTimeout(coroutineFragment!!.viewLifecycleOwner,imgUrl,300){
                    ivGlideLoad.load(it, isCenterCrop = true)
                    tvImgInfo.text = it.absolutePath
                }
            }

            suspendCoroutine.click {
                SuspendCancellableCoroutineTest.getInstance().suspendCoroutine(coroutineFragment!!.viewLifecycleOwner)
            }
            resumeCoroutine.click {
                SuspendCancellableCoroutineTest.getInstance().resumeCoroutine()
            }

        }
        fragmentList.add(coroutineFragment!!)

        binding.apply {
            btFlow.click {
                showCurrentFragmentIndex(0)
            }
            btCoroutine.click {
                showCurrentFragmentIndex(1)
            }
        }
    }

    private fun startTimeoutCoroutine() {
        CoroutineTest.getInstance().startCoroutineWithTimeout(coroutineFragment!!.viewLifecycleOwner,5000)
    }

    private fun showCurrentFragmentIndex(pos: Int) {
        com.fido.common.common_base_util.ext.startActivity<JetPackageContainerAc>(Pair("index",pos))
    }

    private fun registerFlowStickBus() {
        FlowBus.withStick<String>("flow_stick_bus").register(flowFragment!!.viewLifecycleOwner){
            loge("----collect StickFlowBus${it}")
            toast(it)
        }
    }

    private fun registerFlowBus() {
//        FlowBus.with<String>("flowbus").register(flowFragment!!.viewLifecycleOwner){
//            loge("----collect FlowBus${it}")
//            toast(it)
//        }
        FlowBus.register<String>(flowFragment!!.viewLifecycleOwner,mFlowTag){
            toast(it)
        }
    }
    private val mFlowTag = "666"
    private var isFirstPost = true
    private fun sendBus() {
//        try {
//            FlowBus.with<String>("flowbus").post(flowFragment!!.lifecycleScope,"i am flow bus")
//        }catch (e:Exception){
//            toast(e.message + e.toString())
//        }
        if (isFirstPost) {
            toast("发送失败了吧，再点一下试试")
        }
        FlowBus.post(flowFragment!!.lifecycleScope,"456",tag = if (isFirstPost) null else mFlowTag)
        isFirstPost = false
    }

    private fun sendStickBus() {
        FlowBus.withStick<String>("flow_stick_bus").post(flowFragment!!.lifecycleScope,"i am flow stick bus")
    }

    private fun observeValue() {

        lifecycleScope.launch {
            mViewModel.searchFlow.collect {
                logd("StateFlow -value $it")
            }
        }
        //StateFlow是热流（没有订阅者也会发送数据），多个订阅者接受的是同一份数据
        lifecycleScope.launch {
            mViewModel.searchFlow.collect {
                logd("StateFlow2 -value $it")
            }
        }

        mViewModel.searchLD.observeForever {
            logd("LiveData-value $it")
        }

        // 订阅 SharedFlow
        lifecycleScope.launch {
            mViewModel.sharedFlow.collect {
                logd("SharedFlow-value $it")
            }
        }

//        lifecycleScope.launch {
//            mViewModel.sharedFlow.collect {
//                logd("SharedFlow-value $it")
//            }
//        }

        lifecycleScope.launch{
            mViewModel.newSharedFlow.collect{
                logd("newSharedFlow-value $it")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        sendBus()
//        sendStickBus()
    }
}

class JetpackageViewModel :ViewModel(){

    private val _searchLD = MutableLiveData<String>()
    val searchLD:LiveData<String> = _searchLD

    private val _searchFlow = MutableStateFlow("")
    val searchFlow:StateFlow<String> = _searchFlow

//    private val _sharedFlow = MutableSharedFlow<String>()
    private val _sharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
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
        //效果类似StateFlow,防抖效果，相同数据不再接收
        _newSharedFlow.distinctUntilChanged()
    }

    fun changeSearch(keyword: String) {
//        _sharedFlow.tryEmit(keyword)
        _sharedFlow.tryEmit(keyword).also {
            if (!it) logd("Failed to emit to _sharedFlow")
        }
        _searchFlow.value = keyword
        _searchLD.value = keyword
        _newSharedFlow.tryEmit(keyword)
    }

}
