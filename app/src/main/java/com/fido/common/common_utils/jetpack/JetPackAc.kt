package com.fido.common.common_utils.jetpack

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
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
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_utils.eventbus.flow_bus.FlowBus
import com.fido.common.common_utils.jetpack.coroutine.KotlinCoroutineAc
import com.fido.common.databinding.AcJetpackBinding
import com.fido.common.databinding.FragmentCoroutineTestBinding
import com.fido.common.databinding.FragmentJetpackageFlowBinding
import kotlinx.coroutines.Dispatchers
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
class JetPackAc : AppCompatActivity() {

    private val binding: AcJetpackBinding by binding()
    private val mViewModel: JetpackageViewModel by viewModels<JetpackageViewModel>()

    private var flowFragment: EasyBindingFragment<FragmentJetpackageFlowBinding>? = null
    private var coroutineFragment: EasyBindingFragment<FragmentCoroutineTestBinding>? = null

    companion object {
        val fragmentList = mutableListOf<Fragment>()
    }

    private val _tag = 66
    private val _tagStr = mutableListOf(1, 2, 3)
    var tag = _tag
    var tagStr = _tagStr

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btKtCoroutine.click {
            startActivity<KotlinCoroutineAc>()
        }

        observeValue()

        flowFragment =
            EasyBindingFragment.getInstance<FragmentJetpackageFlowBinding>(R.layout.fragment_jetpackage_flow) {
                btSend.click {
                    //值传递，tag会拷贝一份_tag的值，所以不会影响_tag的值
                    tag--
                    //引用传递，修改tagStr 会修改 _tagStr
                    if (tagStr.size != 0) {
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

        val imgUrl =
            "https://gd-hbimg.huaban.com/5c94c28ad72061a418df7525079fecb2b42a296116b61-b7nVlM_fw658webp"
        coroutineFragment =
            EasyBindingFragment.getInstance<FragmentCoroutineTestBinding>(R.layout.fragment_coroutine_test) {

                logd(
                    """
                    coroutineFragment viewLifecycleOwner =${coroutineFragment?.viewLifecycleOwner}
                    parentAc lifecycleOwner = ${coroutineFragment?.activity?.lifecycleOwner}
                """.trimIndent()
                )

                btWithTimeout.click {
                    startTimeoutCoroutine()
                }
                btGlideWithTimeout.click {
                    CoroutineTest.getInstance().downloadImgMaybeTimeout(
                        coroutineFragment!!.viewLifecycleOwner,
                        imgUrl,
                        10 * 1000
                    ) {
                        ivGlideLoad.load(it, isCenterCrop = true)
                        tvImgInfo.text = it.absolutePath
                    }
                }
                btGlideWithTimeoutFail.click {
                    CoroutineTest.getInstance().downloadImgMaybeTimeout(
                        coroutineFragment!!.viewLifecycleOwner,
                        imgUrl,
                        300
                    ) {
                        ivGlideLoad.load(it, isCenterCrop = true)
                        tvImgInfo.text = it.absolutePath
                    }
                }

                suspendCoroutine.click {
                    SuspendCancellableCoroutineTest.getInstance()
                        .suspendCoroutine(coroutineFragment!!.viewLifecycleOwner)
                }
                resumeCoroutine.click {
                    SuspendCancellableCoroutineTest.getInstance().resumeCoroutine()
                }

            }
        fragmentList.add(coroutineFragment!!)

        val stateFlow = MutableStateFlow("")

        binding.apply {
            btFlow.click {
                showCurrentFragmentIndex(0)
            }
            btCoroutine.click {
                showCurrentFragmentIndex(1)
            }

            binding.etInput.doOnTextChanged { text, start, before, count ->
                loge("normal doOnTextChanged ${text}  ")
            }
            binding.etInput.doOnTextChanged { text, start, before, count ->
                lifecycleOwner.lifecycleScope.launch(context = Dispatchers.IO) {  // Dispatchers.IO这里只是为了测试，其实没必要，想看看具体在什么线程
//                    loge("emit running on thread: ${Thread.currentThread().name}")
                    stateFlow.emit(text.toString().trim())
                }
            }
        }

        //因为 StateFlow 只保留最新的状态值，所以尽管你在第一个协程中多次调用了 emit，只有最后一次调用（即 emit("88")）的结果被保留并传递给 collect。
        //最终结果显示 "88"，并且只打印一次，因为这是 stateFlow 的最终状态。
        lifecycleOwner.lifecycleScope.launch {
            stateFlow.emit("66")
            stateFlow.emit("66")
            stateFlow.emit("77")
            stateFlow.emit("88")
            stateFlow.emit("88")
        }
        lifecycleOwner.lifecycleScope.launch {
            stateFlow.collect {
                loge("利用StateFlow去重后的doOnTextChanged value=$it collect running on thread: ${Thread.currentThread().name}")
            }
        }
        //StateFlow 和 LiveData 一样数据会丢失数据。当 StateFlow 连续 emit 时，我们只会获取最新的数据 ，StateFlow 会抛弃旧的数据
        lifecycleOwner.lifecycleScope.launch {
            //stateFlow和LiveData一样数据会重放
            stateFlow.collect {
                loge("new collect value=${it} stateFlow和LiveData一样数据会重放，这里重放了最后一条数据${it}")
            }
        }

        // ================== 等效于 StateFlow ====================
        val shared = MutableSharedFlow<String>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
        shared.tryEmit("") // 设置初始值
        val stateFormSharedFlow = shared.distinctUntilChanged() // 过滤掉相同的数据
        //=================== 上面这么写法，其实等效于StateFlow,因为StateFlow本来也就是SharedFlow子类
    }

    private fun startTimeoutCoroutine() {
        CoroutineTest.getInstance()
            .startCoroutineWithTimeout(coroutineFragment!!.viewLifecycleOwner, 5000)
    }

    private fun showCurrentFragmentIndex(pos: Int) {
        com.fido.common.common_base_util.ext.startActivity<JetPackageContainerAc>(
            Pair(
                "index",
                pos
            )
        )
    }

    private fun registerFlowStickBus() {
        FlowBus.withStick<String>("flow_stick_bus").register(flowFragment!!.viewLifecycleOwner) {
            loge("----collect StickFlowBus${it}")
            toast(it)
        }
    }

    private fun registerFlowBus() {
//        FlowBus.with<String>("flowbus").register(flowFragment!!.viewLifecycleOwner){
//            loge("----collect FlowBus${it}")
//            toast(it)
//        }
        FlowBus.register<String>(flowFragment!!.viewLifecycleOwner, mFlowTag) {
            toast(it)
        }
    }

    private val mFlowTag = "666"
    private var isFirstPost = true
    private fun sendBus() {
        if (isFirstPost) {
            toast("发送失败了吧，再点一下试试")
        }
        FlowBus.post(
            flowFragment!!.lifecycleScope,
            "456",
            tag = if (isFirstPost) null else mFlowTag
        )
        isFirstPost = false
    }

    private fun sendStickBus() {
        FlowBus.withStick<String>("flow_stick_bus")
            .post(flowFragment!!.lifecycleScope, "i am flow stick bus")
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

        lifecycleScope.launch {
            mViewModel.newSharedFlow.collect {
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

class JetpackageViewModel : ViewModel() {

    private val _searchLD = MutableLiveData<String>()
    val searchLD: LiveData<String> = _searchLD

    private val _searchFlow = MutableStateFlow("")
    val searchFlow: StateFlow<String> = _searchFlow

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
