package com.fido.common.common_utils.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.imageview.load
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.result.InnerException
import com.fido.common.common_base_util.ext.result.andThen
import com.fido.common.common_base_util.ext.result.dispatch
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcKtResultBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author: FiDo
 * @date: 2024/9/14
 * @des:
 */
class KtResultAc : AppCompatActivity() {

    private val binding: AcKtResultBinding by binding()
    private var isSuccess = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btResultTest.apply {
                click {
                    this.text = "RESULT-${isSuccess}"
                    resultTest()
                        .onSuccess {
                            toast("$it")
                            isSuccess = !isSuccess
                        }
                        .onFailure {
                            toast("$it")
                            isSuccess = !isSuccess
                        }
                }
            }

            btResultCombo.click {
                uploadFile().onSuccess {
                    loge("onSuccess==>${it}")
                    binding.mIV.load(it)
                    updateProfile(it)
                        .onSuccess {
                            updateProfileUI()
                        }.onFailure {
                            showErrorPage()
                        }
                }.onFailure {
                    showErrorPage()
                }
            }

            btResultComboEasy.click {
                uploadFile()
                    .andThen { updateProfile(it) }
                    .onSuccess { updateProfileUI() }
                    .onFailure { showErrorPage() }
            }

            btResultDispatch.click {
                getUserPhoneNumber().dispatch {
                    val biliNum = getBilbilVideoPlayNum(it).getOrThrow()
                    val tikNum = getTiktokVideoPlayNum(it).getOrThrow()
                    biliNum+tikNum
                }.onFailure {
                    println("onFailure $it")
                }.onSuccess {
                    println("onSuccess $it")
                }
            }

            btResultDispatch2.click {
                getUserPhoneNumber().dispatch {
                    val biliNum = getBilbilVideoPlayNum(it).getOrThrow()
                    val tikNum = getTiktokVideoPlayNum(it,true).getOrThrow()
                    biliNum+tikNum
                }.onFailure {
                    println("onFailure $it")
                }.onSuccess {
                    println("onSuccess $it")
                }
            }

            //多对一依赖

        }
    }

    private fun resultTest(): Result<Boolean> {
        return Result.success(isSuccess)
    }

    private fun uploadFile():Result<String>{
        runBlocking {
            delay(2000)
            return@runBlocking Result.success("https://gd-hbimg.huaban.com/75ee29f7f4d0621163b08930703ba5da4c2f5f2a963a1-SPvzNw_fw658webp")
        }
        //默认是一棵树，2s后更新
        return Result.success("https://gd-hbimg.huaban.com/2370df63cbc7c06d8550133e515047ef6cdfbce6ba109-KoBBVb_fw658webp")
    }

    private fun updateProfile(url:String):Result<Boolean>{
        //更新用户信息
        if (url.isNotEmpty()) {
            return Result.success(true)
        }else{
            return Result.failure(RuntimeException("图片信息为空"))
        }
    }

    private fun showErrorPage(){
        toast("显示失败UI")
    }

    //更新用户UI
    private fun updateProfileUI(){

    }


    // ==========================dispatch=============================
    private fun getUserPhoneNumber():Result<String>{
        return Result.success("666666")
    }

    // 获取B站视频播放数据
    fun getBilbilVideoPlayNum(phoneNumber: String): Result<Long> {
           return Result.success(2000)
    }

    // 获取抖音的视频播放数据
    fun getTiktokVideoPlayNum(phoneNumber: String,simulateFail:Boolean=false): Result<Long> {
        return if (simulateFail) Result.failure(InnerException()) else Result.success(1000)
    }

}





