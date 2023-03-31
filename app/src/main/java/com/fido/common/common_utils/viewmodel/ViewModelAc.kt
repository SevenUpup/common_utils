package com.fido.common.common_utils.viewmodel

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.longToast
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.toJson
import com.fido.common.common_base_util.util.ShellUtils
import com.fido.common.common_utils.BuildConfig
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcViewModelBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

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
        val clickProxy = ClickProxy()
        binding.clickProxy = clickProxy

        toast(applicationInfo.processName)
        loge("APPLICATION_ID=${BuildConfig.APPLICATION_ID} packageName=${applicationContext.packageName} applicationInfo.packageName=${applicationInfo.packageName}")
    }

    override fun initData() {
        showNormalInputDialog {
            thread {

//                val externalFilePath: String = Environment.getExternalStorageDirectory().getPath() + "/" + fileName
//                val command = "ls -l $externalFilePath"
//                val process: Process = Runtime.getRuntime().exec("shell", "/bin/sh", "-c", command)
//                val reader = BufferedReader(InputStreamReader(process.inputStream))

                val execCmd = ShellUtils.execCmd("adb devices", false)
                loge(execCmd.toJson())
                longToast(execCmd.toJson())
            }
        }

    }

    private var index = 1
    override fun initEvent() {

        lifecycleScope.launch {

        }

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

    var action: ((String) -> Unit)? = {
        loge("action==> $it","FiDo")
    }

}

class MViewModel : ViewModel() {
    var mResult = MutableLiveData<String>()
    var data = MutableSharedFlow<String>()
    val bool = MutableLiveData<Boolean>()
}
