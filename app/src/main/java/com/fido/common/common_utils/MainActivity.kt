package com.fido.common.common_utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.*
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.fido.common.common_base_ui.ext.image_select.openImageSelect
import com.fido.common.common_base_ui.ext.image_select.selectImagesPath
import com.fido.common.common_base_ui.ext.permission.extRequestPermission
import com.fido.common.common_base_ui.util.ImagePreViewUtil
import com.fido.common.common_base_util.ext.DrawableStatus
import com.fido.common.common_base_util.ext.addStatusableDrawableBg
import com.fido.common.common_base_util.ext.longToast
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.getColorCompat
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_base_util.toJson
import com.fido.common.common_base_util.util.ImageCodeUtils
import com.fido.common.common_base_util.util.time.Interval
import com.fido.common.common_utils.anim.AnimUtils
import com.fido.common.common_utils.databinding.ActivityMainBinding
import com.fido.common.common_utils.rv.RvAc
import com.fido.common.common_utils.spannable.SpannableAc
import com.fido.common.common_utils.time.IntervalTimeAc
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
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

        createCode()
    }

    fun onSpannable(v:View){
        startActivity<SpannableAc>()
    }

    private fun createCode() {
        mBinding.iv.setOnClickListener {
            val bitmap = ImageCodeUtils.instance.createBitmap()
            val code = ImageCodeUtils.instance.code
            mBinding.iv.setImageBitmap(bitmap)
            toast(code)
        }
    }

    private var index = 0
    private val imgs = listOf(
        "https://gd-hbimg.huaban.com/c14e19ddad0c9c734565ab3a47fa5e2a7e9b3280fadd4-RGSDYm_fw658webp",
        "https://gd-hbimg.huaban.com/a93532e29f3ea71d4aa69ce956b528e21134b42d1e4c0-HcGBKP_fw658webp",
        "https://gd-hbimg.huaban.com/45836fc43d5593b619cb2f1e78ff3b4bf76ddb6b126ac-gh6NKq_fw658webp",
        "https://gd-hbimg.huaban.com/579d0aba91331b318df42810483556e025a760af11e81-fXxVjl_fw658webp",
        "https://gd-hbimg.huaban.com/67eb28da21255d1681880335f341c84258403dda1114b-XmliZB_fw658webp",
    )

    var index0 = 0
    private fun initEvent() {
        mBinding.tvStatus
            .addStatusableDrawableBg(R.color.teal_700,20f, isTopCorner = false, status = DrawableStatus.SELECTED)
            .addStatusableDrawableBg(R.color.teal_200,5f,DrawableStatus.PRESSED)
            .addStatusableDrawableBg(R.color.purple_700,50f, isDefultDrawable = true)

        mBinding.tvStatus.setOnClickListener {
            it.isSelected = !it.isSelected
            toast("click isSelected=${it.isSelected}")
        }

        mBinding.anim.setOnClickListener {
            mBinding.iv2.isGone = false

            AnimUtils.beginBounceAnima(mBinding.iv)

            val set = AnimatorSet()
            mBinding.iv2.isGone = false

            mBinding.iv2.post {
                val height = mBinding.iv2.height.toFloat()
                val mFrom = if (index0 % 2 == 0) height else 0f
                val mTo = if (index0 % 2 == 0) 0f else height

                set.play(ObjectAnimator.ofFloat(mBinding.iv2,"translationY",mFrom,mTo))
                    .with(ObjectAnimator.ofFloat(mBinding.iv2,"alpha",if(index0%2==0) 1f else 0f))
                set.duration = 500
                set.addListener(object :AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {}

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                })
                set.start()
                index0++
            }

        }

        mBinding.btInterval.setOnClickListener{
            startActivity<IntervalTimeAc>()
        }
        mBinding.permission.setOnClickListener {
            extRequestPermission(
                listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), "不给权限怎么玩！"
            ) {

            }
        }
        mBinding.btOpenImage.setOnClickListener {
            extRequestPermission(
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ) {
                openImageSelect(null,3, canCrop = false,call = object :OnResultCallbackListener<LocalMedia>{
                    override fun onResult(result: ArrayList<LocalMedia>) {
                        Log.d("FiDo", "onResult: ${result.toJson()}")
                        longToast(result.selectImagesPath.toString())
                    }
                    override fun onCancel() {}
                })
//                openCamera(call = object :OnResultCallbackListener<LocalMedia> {
//                    override fun onResult(result: ArrayList<LocalMedia>) {
//                        Log.d("FiDo", "onResult: ${result.toJson()}")
//                        longToast(result.selectImagesPath.toString())
//                    }
//
//                    override fun onCancel() {
//                        longToast("cancle")
//                    }
//                })
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

