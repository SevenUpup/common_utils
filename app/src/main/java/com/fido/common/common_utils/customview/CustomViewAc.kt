package com.fido.common.common_utils.customview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.activity
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.height
import com.fido.common.common_base_util.ext.margin
import com.fido.common.common_base_util.getScreenHeightPx
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.common_utils.customview.ninegrid.ImageNineGridAdapter
import com.fido.common.databinding.AcCustomViewBinding

/**
 * @author: FiDo
 * @date: 2024/4/7
 * @des:
 */
class CustomViewAc:AppCompatActivity() {

    private val binding:AcCustomViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = binding.codeRainView
        view.height(getScreenHeightPx())
        view.click {  }

        val imgs = listOf("1", "2", "3", "4", "5")
        binding.nineGridView.run {
            setSingleModeSize(400,800)
            setAdapter(ImageNineGridAdapter(imgs))
        }


        binding.button.click {
            //new一个animset,传入的是对象本身，apply用run好像也可以
            AnimatorSet().apply {
                ObjectAnimator.ofPropertyValuesHolder(
                    binding.textView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f)
                ).apply {
                    duration = 300L
                    interpolator = LinearInterpolator()
                }.let {
                    play(it).with(
                        ObjectAnimator.ofPropertyValuesHolder(
                            binding.button,
                            PropertyValuesHolder.ofFloat("translationX", 0f, 100f)
                        ).apply {
                            duration = 3000L
                            interpolator = LinearInterpolator()
                        }
                    )
                    play(it).before(
                        ValueAnimator.ofInt(0,getScreenWidthPx()).apply {
                            addUpdateListener { animation ->
//                                logd("value=>${animation.animatedValue}")
                                binding.imageView.margin(leftMargin = animation.animatedValue as Int)
//                                binding.imageView.left = animation.animatedValue as Int
                            }
                            duration = 4000L
                            interpolator = LinearInterpolator()
                        }
                    )
                }
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        Toast.makeText(activity,"animation end",Toast.LENGTH_SHORT).show()
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })
                start()
            }
        }


    }

}