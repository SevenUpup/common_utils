package com.fido.common.common_utils.viewmodel

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fido.common.BuildConfig
import com.fido.common.R
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.longToast
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.toJson
import com.fido.common.common_base_util.util.ShellUtils
import com.fido.common.databinding.AcViewModelBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask

/**
 * 双向绑定
 */
class ViewModelAc : BaseVBActivity<AcViewModelBinding>(),DefaultLifecycleObserver {

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

        binding.tvTimerInfo.text = "mViewModel.timer=${mViewModel.timer}"
        mViewModel.setOnTimeChangeListener(object : MViewModel.OnTimeChangeLister {
            override fun onTimeChanged(second: Int) {
                runOnUiThread {
                    binding.tvTimer.text = second.toString()
                }
            }
        })
        mViewModel.start()

    }

    override fun initData() {
        showNormalInputDialog(content = "点击确认执行 adb devices 并吐司结果") {
            thread {

//                val externalFilePath: String = Environment.getExternalStorageDirectory().getPath() + "/" + fileName
//                val command = "ls -l $externalFilePath"
//                val process: Process = Runtime.getRuntime().exec("shell", "/bin/sh", "-c", command)
//                val reader = BufferedReader(InputStreamReader(process.inputStream))

                val execCmd = ShellUtils.execCmd("devices", false)
                loge(execCmd.toJson())
                longToast("success:${execCmd.successMsg} error:${execCmd.errorMsg}")
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


//    private val timer: Timer = Timer()
    val timer: Timer = Timer()
    private var timeChangeListener: OnTimeChangeLister? = null
    private var currentSecond: Int = 0
    fun start() {
        loge("start 执行了")
        timer.schedule(timerTask {
            currentSecond++
            timeChangeListener?.onTimeChanged(currentSecond)
        }, 1000, 1000)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    fun setOnTimeChangeListener(listener: OnTimeChangeLister) {
        timeChangeListener = listener
    }

    interface OnTimeChangeLister {
        fun onTimeChanged(second: Int)
    }

}
