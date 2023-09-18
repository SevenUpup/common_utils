package com.fido.common.common_utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.CycleInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.dialog.createPop
import com.fido.common.common_base_ui.base.dialog.createVBPop
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.image_select.openImageSelect
import com.fido.common.common_base_ui.ext.image_select.selectImagesPath
import com.fido.common.common_base_ui.ext.imageview.downloadImage
import com.fido.common.common_base_ui.ext.imageview.load
import com.fido.common.common_base_ui.ext.imageview.savePic2Gallery
import com.fido.common.common_base_ui.ext.permission.extRequestPermission
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_ui.util.ImagePreviewUtil
import com.fido.common.common_base_ui.util.showNormalListDialog
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.*
import com.fido.common.common_base_util.channel.receiveTag
import com.fido.common.common_base_util.channel.sendStickEvent
import com.fido.common.common_base_util.channel.sendStickTag
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.util.AssetUtils
import com.fido.common.common_base_util.util.ImageCodeUtils
import com.fido.common.common_base_util.util.ShellUtils
import com.fido.common.common_base_util.util.emoji.EmojiChecker
import com.fido.common.common_base_util.util.timer.Interval
import com.fido.common.common_utils.accessibility.AdSkipAc
import com.fido.common.common_utils.anim.AnimUtils
import com.fido.common.common_utils.anim.ShakeAnim
import com.fido.common.common_utils.banner.BannerAc
import com.fido.common.common_utils.calendar.WriteCalendarAc
import com.fido.common.common_utils.constraintlayout.ConstraintLayoutAc
import com.fido.common.common_utils.databinding.ActivityMainBinding
import com.fido.common.common_utils.databinding.DialogTestBinding
import com.fido.common.common_utils.databinding.LayoutHeaderViewBinding
import com.fido.common.common_utils.edittext.CustomStyleActivity
import com.fido.common.common_utils.motionlayout.*
import com.fido.common.common_utils.naviga.CodenavigationAc
import com.fido.common.common_utils.picker.PickerViewAc
import com.fido.common.common_utils.room.RoomAc
import com.fido.common.common_utils.rv.RvAc
import com.fido.common.common_utils.sp.SPAc
import com.fido.common.common_utils.spannable.SpannableAc
import com.fido.common.common_utils.test.NavigationAc
import com.fido.common.common_utils.time.IntervalTimeAc
import com.fido.common.common_utils.viewmodel.ViewModelAc
import com.fido.common.common_utils.viewpager.ViewpageAc
import com.fido.common.flutter.FlutterInteractiveAc
import com.fido.common.surface.SurfaceAc
import com.fido.common.textview.TextViewAc
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.enums.PopupPosition
import com.lxj.xpopup.impl.BottomListPopupView
import com.lxj.xpopup.widget.SmartDragLayout
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredMemberFunctions

data class P(val name: String)
class MainActivity : AppCompatActivity() {

    private val mBinding: ActivityMainBinding by binding()
    var pop:BottomListPopupView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("FiDo", "appLanguage = ${appLanguage} packageName=${packageInfo.packageName} currentTime = ${currentTimeString().getYearFromTime()} ${currentTimeString().getMonthFromTime()} ${currentTimeString().getDayFromTime()}")
        val date = "2322-06-21"
        Log.e("FiDo", "$date 解析结果 year=${date.getYearFromTime()} month=${date.getMonthFromTime()} day=${date.getDayFromTime()}")
        Log.e("FiDo", "currentTimeString = ${currentTimeString()} compareresult = ${dateCompare(currentTimeString(),date)}")

        Handler().postDelayed({

            toast("exe cmd")
//            ShellUtils.execCmd("adb shell input keyevent 26",false)
            ShellUtils.execCmd("adb shell monkey -p <${packageInfo.packageName}> -v 500",false) },2000)
//        ShellUtils.execCmd("adb shell top",false)

        thread {
            sendStickEvent(P("xpz"))
            sendStickEvent(P("dpz"))
        }

        doOnAppStatusChanged (onForeGround = {
            toast("onForeground")
        }){
            toast("onBackground")
        }

        addButton()

        initView()

        initEvent()

        createCode()

        mBinding.tvRotationY2.throttleClick {
            showNormalListDialog(listOf("1", "2", "3", "4", "5"),"test",Gravity.BOTTOM){position, text ->
                toast(text)
            }
        }

        mBinding.tvRotationY.throttleClick {
            if (pop == null){
                pop = XPopup.Builder(this@MainActivity).run {
                    isDestroyOnDismiss(false)
                    autoOpenSoftInput(false)
                    isDarkTheme(false)
                    asBottomList(
                        title,
                        listOf("1", "2", "3", "4", "5").toTypedArray()
                    ) { position, text ->
                        toast(text)
                    }
                }
            }
            pop?.show()

            var dragLayout:SmartDragLayout?=null
            try {
                BottomPopupView::class.java.getDeclaredField("bottomPopupContainer").run {
                    isAccessible = true
                    dragLayout = get(pop) as SmartDragLayout
//                    dragLayout?.setDuration(400)
                }
            }catch (e:Exception){
                loge(e.message.toString())
                toast(e.message)
            }
            val childView =  SmartDragLayout::class.java.getDeclaredField("child").run {
                isAccessible = true
                get(dragLayout) as?  View
            }

            dragLayout?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (measuredWidth > 0 && measuredHeight > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)

                        } else {
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }
                        childView?.post {
                            val minH = SmartDragLayout::class.java.getDeclaredField("minY").run {
                                isAccessible = true
                                set(dragLayout, (((childView?.height?:0) * 0.9).toInt()))
                                get(dragLayout) as? Int
                            }
                            loge("minH=${minH}")
                        }
                    }
                }
            })

        }
        mBinding.tvRotationY.rotationY = 30f
        mBinding.tvRotationY2.rotationY = -30f
        mBinding.tvRotationX.rotationX = 30f
        mBinding.tvRotationX2.rotationX = -30f
        mBinding.tvTranslationZ.translationZ = 10f
        mBinding.tvTranslationZ2.translationZ = -10f

        receiveTag("tag_1") {
            mBinding.btRv.text = "go Rv tag value=$it"
        }

        sendStickTag("tag_FiDo")

        var string = "你好呀，👿"
        var string2 = "你好呀，😇"
        var string3 = "你好呀，😠"
        var string4 = "你好呀，呵呵呵呵"
        val string5 = "\uD83D\uDCA2这是个啥"
        val string6 = "这是个啥\uD83D\uDCA5"
        mutableListOf<String>().apply {
            add(string)
            add(string2)
            add(string3)
            add(string4)
            add(string5)
            add(string6)
        }.forEach {
            loge("$it 是否包含emoji = ${EmojiChecker.containsEmoji(it)}")
        }

    }

    private fun addButton() {
        addView<ViewModelAc>("go ViewModel")
        addView<BannerAc>("go Banner")
        addView<SurfaceAc>("Go Surface")
        addView<NavigationAc>("Go Nav")
        addView<CodenavigationAc>("Go Code Nav")
        addView<ViewpageAc>("Go ViewPage")
        addView<LiveDataAc>("Go LiveData")
        addView<MotionLayoutAc>("Go MotionLayout")
        addView<MotionLayoutAc2>("Go MotionLayout2")
        addView<MotionBall2Ac>("Go MotionBalls2")
        addView<MotionCollapsibleAc>("Go Collapsible")
        addView<MotionCarouselAc>("Go Carousel")
        addView<MotionCustomCarouselAc>("Go CustomCarousel")
        addView<MotionYouTubeAc>("Go FakeYouTube")
        addView<RoomAc>("Go Room")
        addView<FlutterInteractiveAc>("Go FlutterInteractive")
        addView<SPAc>("Go SP")
        addView<WriteCalendarAc>("WriteCalendar")
        addView<AdSkipAc>("无障碍服务-skip开屏广告")
        addView<TextViewAc>("textview 根据控件大小自动缩放")
        for (i in 0 until mBinding.container.childCount) {
            if (mBinding.container.getChildAt(i).id == R.id.tv) {
                mBinding.container.getChildAt(i).run {
                    margin(verticalMargin = 3.dp)
                }
            }
        }
    }

    private inline fun <reified T : Activity> addView(title: String) {
        mBinding.container.addView(
            Button(this).apply {
                id = R.id.tv
                text = title
                setTextColor(R.color.white.getColor)
                setBackgroundColor(R.color.teal_700.getColor)
                isAllCaps = false
                setOnClickListener {
                    startActivity<T>()
                }
                roundCorners = 10f.dp.toFloat()
            },
            mBinding.container.childCount - 4,ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        )
    }

    fun ConstraintLayout(v: View) {
        startActivity<ConstraintLayoutAc>()
    }

    fun snackBar(v: View) {
        snackbar(v, "i am snackbar", "click me") {
            toast("snackbar")
        }
//        AssetUtils.saveFileToAssets(this,"https://gd-hbimg.huaban.com/2f6abf65707a397b034a05771783761d4787c59384008-O6OJKL_fw658webp")
        downloadImage(this,"https://gd-hbimg.huaban.com/2f6abf65707a397b034a05771783761d4787c59384008-O6OJKL_fw658webp"){
            loge("absolutePath=${it.absolutePath} path=${it.path}")
            mBinding.iv.load(it)
        }
    }

    fun installApk(v: View) {
        AssetUtils.copyFileFromAssets(
            this,
            "debugkit.apk",
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/temp.apk"
        )
        installApp(File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + "/temp.apk"))
    }

    fun onSpannable(v: View) {
        startActivity<SpannableAc>()
    }

    fun onAnimShark(v: View) {
        val oa1: ObjectAnimator =
            ObjectAnimator.ofFloat(mBinding.iv3, View.TRANSLATION_X.name, 0f, 8f) //抖动幅度0到8
        oa1.duration = 300 //持续时间
        oa1.interpolator = CycleInterpolator(4f) //抖动次数
        oa1.start() //开始动画
        vibrateShot(500)
    }

    fun onAnimShark2(v: View) {
        ShakeAnim.start(mBinding.iv3)
        vibrateShot(500)
    }

    fun codeEditText(v: View) {
        startActivity<CustomStyleActivity>()
    }

    fun onAnim(view: View) {
        val locationArray = intArrayOf(0, 0)
        mBinding.iv.getLocationOnScreen(locationArray)
//            val xLocation = locationArray[0].toFloat()
        val xLocation = getScreenWidthPx().toFloat() - mBinding.iv3.width.toFloat()
        val yLocation =
            (getScreenHeightPx() - mBinding.iv3.bottom - mBinding.iv3.height / 2).toFloat()
        Log.d(
            "FiDo",
            "initEvent: xLocation = $xLocation  yLocation = $yLocation mBinding.iv3.bottom=${mBinding.iv3.bottom}"
        )

        /*AnimatorSet().apply {
            val xDuration = 1000L
            val xOffset = 100f
            playSequentially(
                ObjectAnimator.ofFloat(mBinding.iv3, "translationX", -xOffset)
                    .setDuration((xDuration / 2)),
                ObjectAnimator.ofFloat(mBinding.iv3, "translationX", -xOffset, xOffset).apply {
                    duration = xDuration
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = 2
                },
                ObjectAnimator.ofFloat(mBinding.iv3, "translationX", 0f).setDuration((xDuration / 2))
            )
        }.start()*/

        val animSet = AnimatorSet()
        animSet
            .play(ObjectAnimator.ofFloat(mBinding.iv3, View.TRANSLATION_X.name, 0f, xLocation))
            .with(ObjectAnimator.ofFloat(mBinding.iv3, View.TRANSLATION_Y.name, 0f, yLocation))
        animSet.duration = 2000
//        animSet.start()
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
        mBinding.btPickerView.click {
            startActivity<PickerViewAc>()
        }
        mBinding.tvStatus
            .addStatusableDrawableBg(
                R.color.teal_700,
                20f,
                isTopCorner = false,
                status = DrawableStatus.SELECTED
            )
            .addStatusableDrawableBg(R.color.teal_200, 5f, DrawableStatus.PRESSED)
            .addStatusableDrawableBg(R.color.purple_700, 50f, isDefultDrawable = true)

        mBinding.tvStatus.setOnClickListener {
            it.isSelected = !it.isSelected
            createVBPop<DialogTestBinding>(
                R.layout.dialog_test,
                atView = mBinding.iv,
                popWidth = getScreenWidthPx()
            ) {
                tvCancle.setOnClickListener {
                    toast("click")
                }
            }.show()

            createPop(
                R.layout.layout_header_view,
                atView = mBinding.anim,
                popPosition = PopupPosition.Top
            ) {
                findViewById<TextView>(R.id.tv_header_title).setOnClickListener {
                    toast("click header")
                }
                findViewById<RecyclerView>(R.id.mRv)
                    .vertical()
                    .bindData(
                        listOf("1", "2", "3", "4"),
                        R.layout.item_rv_text
                    ) { holder, position, item ->
                        holder.setText(R.id.mTitle, item)
                    }
                    .isGone = false
            }.show()

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

                set.play(ObjectAnimator.ofFloat(mBinding.iv2, "translationY", mFrom, mTo))
                    .with(
                        ObjectAnimator.ofFloat(
                            mBinding.iv2,
                            "alpha",
                            if (index0 % 2 == 0) 1f else 0f
                        )
                    )
                set.duration = 500
                set.addListener(object : AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {}

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationRepeat(animation: Animator?) {}

                })
                set.start()
                index0++
            }

        }

        mBinding.btInterval.setOnClickListener {
            startActivity<IntervalTimeAc>()
        }
        mBinding.permission.setOnClickListener {

            val pop = createPop(
                R.layout.layout_header_view,
                offsetY = 200,
                isCenterHorizontal = true,
//                hasShadowBg = true,
//                hasStatusBarShadow = true,
                isDestroyOnDismiss = true
            ){
                findViewById<TextView>(R.id.tv_header_title).apply {
                    height(100.dp)
                    width(getScreenWidthPx()-200)
                    text = "hello kotlin"
                    rectangleCornerBg(R.color.purple_500,10f)
                    throttleClick {
                        toast("level 1")
                    }
                }
            }
            pop.show()

            val pop2 = createVBPop<LayoutHeaderViewBinding>(R.layout.layout_header_view, offsetY = 500, popAnima = PopupAnimation.TranslateFromTop, isCenterHorizontal = true){
                this.tvHeaderTitle.apply {
                    widthAndHeight(getScreenWidthPx()-300,50.dp)
                    rectangleCornerBg(R.color.colorAccent,20f)
                    text = "i am databinding"
                }
            }
            pop2.show()

            extRequestPermission(
                listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), ""
            ) {
                pop.dismiss()
                pop2.dismiss()
            }
        }
        mBinding.btOpenImage.setOnClickListener {
            extRequestPermission(
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ) {
                openImageSelect(
                    null,
                    3,
                    canCrop = false,
                    call = object : OnResultCallbackListener<LocalMedia> {
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
                ImagePreviewUtil.singleImagePreview(
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
                ImagePreviewUtil.multipleImagePreview(
                    this, mBinding.iv, imgs, 0,
                    srcViewUpdateBlock = { popupView, position ->

                    },
                    longPressBlock = { popupView, position ->
                        toast("long click muilt ${position}")
                        savePic2Gallery(imgs[position],this,"image$position")
                    }
                )
            }
            index++
        }
    }

    private fun initView() {
        mBinding.iv.setImageBitmap(AssetUtils.loadBitmapAsset(this, "dog.jpg"))
        mBinding.iv3.setImageBitmap(AssetUtils.loadBitmapAsset(this, "dog.jpg"))

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
//                clazz.kotlin.companionObject?.declaredMemberFunctions?.find { it.name == "test" }
//                    ?.call(clazz.kotlin.companionObjectInstance, this)

                clazz.kotlin.companionObject?.declaredMemberFunctions?.find { it.name == "test" }
                    ?.call(clazz.kotlin.companionObjectInstance,this)

//                val companionClazz = clazz.declaredClasses.find { it.simpleName == "Companion" }
//                companionClazz?.getDeclaredMethod("test", Context::class.java)?.invoke(clazz.kotlin.companionObjectInstance,this)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

//        mBinding.container.addView(CodeEditText(this).textMode(codeTextSize = 20),mBinding.container.childCount-1,
//            LayoutParams(getScreenWidthPx() ,50.dp))
//        mBinding.container.addView(CodeEditText(this).passwordMode(),mBinding.container.childCount-1,
//            LayoutParams(getScreenWidthPx(),40.dp))
    }

}

