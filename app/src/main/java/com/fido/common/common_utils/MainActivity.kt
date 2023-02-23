package com.fido.common.common_utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.fido.common.common_base_ui.ext.image_select.extOpenImageSelect
import com.fido.common.common_base_ui.ext.permission.extRequestPermission
import com.fido.common.common_base_ui.util.ImagePreViewUtil
import com.fido.common.common_base_util.ext.longToast
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.getColorCompat
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_base_util.util.time.Interval
import com.fido.common.common_utils.databinding.ActivityMainBinding
import com.fido.common.common_utils.rv.RvAc
import com.fido.common.common_utils.test.reflect.AbsReflectTest
import com.fido.common.common_utils.time.IntervalTimeAc
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberFunctions

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()

        initEvent()
    }


    private var index = 0
    private val imgs = listOf(
        "https://gd-hbimg.huaban.com/c14e19ddad0c9c734565ab3a47fa5e2a7e9b3280fadd4-RGSDYm_fw658webp",
        "https://gd-hbimg.huaban.com/a93532e29f3ea71d4aa69ce956b528e21134b42d1e4c0-HcGBKP_fw658webp",
        "https://gd-hbimg.huaban.com/45836fc43d5593b619cb2f1e78ff3b4bf76ddb6b126ac-gh6NKq_fw658webp",
        "https://gd-hbimg.huaban.com/579d0aba91331b318df42810483556e025a760af11e81-fXxVjl_fw658webp",
        "https://gd-hbimg.huaban.com/67eb28da21255d1681880335f341c84258403dda1114b-XmliZB_fw658webp",
    )

    private fun initEvent() {
        mBinding.btInterval.setOnClickListener{
            startActivity<IntervalTimeAc>()
        }
        mBinding.permission.setOnClickListener {
            extRequestPermission(
                listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), "申请权限前弹窗Title"
            ) {

            }
        }
        mBinding.btOpenImage.setOnClickListener {
            extRequestPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ) {
                extOpenImageSelect(
                    null,
                    isSingleDirectReturn = false,
                    listener = object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: MutableList<LocalMedia>?) {
                            longToast(result.toString())
                        }

                        override fun onCancel() {

                        }
                    })
            }
        }
        mBinding.btPreview.setOnClickListener {
            Interval(1, TimeUnit.SECONDS, 1)
                .life(this)
                .subscribe {
                    Log.d("FiDo", "subscribe: ${it}")
                }.finish {
                    Log.d("FiDo", "finish: ${it}")
                }.start()

            if (index % 2 == 0) {
                ImagePreViewUtil.singleImagePreview(
                    this, mBinding.iv, imgs[0]
                ) { popupView, position ->
                    val spanned = buildSpannedString {
                        bold {
                            append("long ")
                        }
                        italic {
                            append("click ")
                        }
                        underline {
                            append("single ")
                        }
                        color(getColorCompat(R.color.purple_500)) {
                            append("pos=$position")
                        }
                    }
                    toast(spanned)
                }
            } else {
                ImagePreViewUtil.multipleImagePreview(
                    this, mBinding.iv, imgs, 0,
                    srcViewUpdateBlock = { popupView, position ->

                    },
                    longPressBlock = { popupView, position ->
                        toast("long click muilt ${position}")
                    }
                )
            }
            index++
        }
    }

    private fun initView() {
        mBinding.btRv.setOnClickListener {
//            startActivity<RvAc>()
            Log.d("FiDo", "RvAc : ${RvAc::class.java.name}")
            try {
                val clazz = Class.forName("com.fido.common.common_utils.rv.RvAc")
                startActivity(
                    Intent(this, clazz)
                )
                clazz.declaredMethods.forEach {
                    Log.d("FiDo", "declaredMethods: ${it.name}")
                }
                // kotlin 方式调用 test 方法
                clazz.kotlin.companionObject?.declaredMemberFunctions?.find { it.name == "test" }
                    ?.call(clazz.kotlin.companionObjectInstance, this)

//                val companionClazz = clazz.declaredClasses.find { it.simpleName == "Companion" }
//                companionClazz?.getDeclaredMethod("test",Context::class.java)?.invoke(clazz.kotlin.companionObjectInstance,this)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}

