package com.fido.common.common_utils.spannable

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.request.RequestOptions
import com.drake.spannable.addSpan
import com.drake.spannable.movement.ClickableMovementMethod
import com.drake.spannable.replaceSpan
import com.drake.spannable.setSpan
import com.drake.spannable.span.CenterImageSpan
import com.drake.spannable.span.ColorSpan
import com.drake.spannable.span.GlideImageSpan
import com.drake.spannable.span.HighlightSpan
import com.fido.common.common_base_ui.widget.edittext.CodeEditText
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.getColor
import com.fido.common.common_base_util.getDrawable
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcSpannableBinding

class SpannableAc:AppCompatActivity() {

    private lateinit var _binding:AcSpannableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.ac_spannable)

        init()

        _binding.etTest.setOnTextChangedListener(object :CodeEditText.OnTextChangedListener{
            override fun onCodeChanged(changeText: CharSequence?) {
                Log.e(
                    "SimpleActivity",
                    String.format("onCodeChanged -- %s", changeText.toString() + "")
                )
            }

            override fun onInputCompleted(text: CharSequence?) {
                Log.e(
                    "SimpleActivity",
                    String.format("onInputCompleted -- %s", text.toString() + "")
                )
            }
        })
    }

    private fun init(){
        _binding.tv.text = "123456".setSpan(CenterImageSpan(R.drawable.ic_zelda.getDrawable))
            .addSpan("colorAndSizeSpan", listOf(
                ColorSpan("#479fd1"),
                AbsoluteSizeSpan(15,true)
            ))
            .addSpan("\n嘿嘿",ColorSpan(R.color.purple_500.getColor))
            .addSpan("\n播放gif[晕]表情").replaceSpan("[晕]"){
                GlideImageSpan(_binding.tv,R.drawable.ic_gif)
                    .setRequestOption(RequestOptions.centerCropTransform())
                    .setDrawableSize(50.dp)
                    .setAlign(GlideImageSpan.Align.CENTER)
            }

        _binding.tv2.text = " ".addSpan("自适应点9图", listOf(
            CenterImageSpan(this,R.drawable.bg_date_label)
                .setDrawableSize(-1)
                .setTextVisibility(true),
            ColorSpan(this,R.color.teal_700),
            AbsoluteSizeSpan(19,true),
            StyleSpan(Typeface.BOLD)
        ))

        _binding.tv3.text = "头像".addSpan("网络头像",GlideImageSpan(_binding.tv3,"https://avatars.githubusercontent.com/u/21078112?v=4")
            .setRequestOption(RequestOptions.circleCropTransform())
            .setDrawableSize(60.dp)
            .setTextVisibility(true)
            .setAlign(GlideImageSpan.Align.CENTER)
        )
            .addSpan("使用shape,自适应标签", listOf(
                CenterImageSpan(this,R.drawable.bg_label)
                    .setDrawableSize(-1)
                    .setPaddingHorizontal(15.dp)
                    .setPaddingVertical(15.dp)
                    .setTextVisibility(true),
                ColorSpan(R.color.teal_200.getColor),
                StyleSpan(Typeface.BOLD),
                AbsoluteSizeSpan(22,true)
            ))addSpan "构建自适应文字宽高标签"

        _binding.tv4.text = "塞尔达公主是任天堂游戏塞尔达传说系列的主要角色。她由宫本茂创造，最早于1986年游戏《塞尔达传说》中登场%s 。 根据宫本茂所述，塞尔达的名字受到美国小说家泽尔达·菲茨杰拉德所影响。宫本茂解释到：“菲茨杰拉德是一个著名的且漂亮的女性，我喜欢她名字读出的声音”。 塞尔达公主几乎都在全部塞尔达传说作品中出现。".replaceSpan("%s") {
            GlideImageSpan(_binding.tv4, R.drawable.ic_zelda)
                .setRequestOption(RequestOptions.centerCropTransform())
                .setAlign(GlideImageSpan.Align.BOTTOM)
                .setDrawableSize(100.dp)
        }

        _binding.tv5.movementMethod = ClickableMovementMethod.getInstance()
        _binding.tv5.text = "xxxxx《隐私政策》与《协议》".replaceSpan("《隐私政策》"){matchResult->
            listOf(
                HighlightSpan(this,R.color.teal_700, Typeface.defaultFromStyle(Typeface.BOLD)){
                    toast("click ${matchResult.value}")
                }
            )
        }.replaceSpan("《协议》"){
            listOf(ColorSpan(R.color.teal_200.getColor),object : ClickableSpan() {
                override fun onClick(widget: View) {
                    toast("click ${it.value}")
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }
            })
        }

    }
}