package com.fido.common.common_utils.motionlayout

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.imageview.loadDrawable
import com.fido.common.common_base_util.ext.addFragment
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.util.timer.interval
import com.fido.common.common_utils.R
import com.fido.common.common_utils.constraintlayout.DebugFragment
import com.fido.common.common_utils.databinding.AcMotionCarouselBinding

/**
@author FiDo
@description:
@date :2023/6/7 17:18
 */
class MotionCarouselAc:AppCompatActivity() {

    val binding:AcMotionCarouselBinding by binding()

    private val images = listOf(
        "https://gd-hbimg.huaban.com/de9fef626b39f675e9de9981dc36b98563c9584ab8f76-NcRJt6",
        "https://gd-hbimg.huaban.com/51ead533e5d7220adf92cff3efe672ab39cc736bd20bc-NrcQNX_fw658webp",
        "https://gd-hbimg.huaban.com/c97d99aa346c64ec028b4af5a057a14281241501ea56-UQNKqp_fw658webp",
        "https://gd-hbimg.huaban.com/17f1f0b6ec554fb96b1b4ed39a66ccdfcaab29874ab98-fpwokc_fw658webp",
        "https://gd-hbimg.huaban.com/7304189a28364633ae5786b0a947b39c7895921d46a22-No3P4r_fw658webp",
        "https://gd-hbimg.huaban.com/4d2f046844547ddaffedc4a1fc090d21a4acfc2518838-T0yuqG_fw658webp",
        "https://gd-hbimg.huaban.com/e9d6cdaf8ba4c08c28e10133c2cb6d4186f44cfa22dfa-NK8JN9_fw658webp",
        "https://gd-hbimg.huaban.com/fb7e85af1da8100e9011d9a61e4c192ab0fefc5f8196a-RGGQBE_fw658webp"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    val drawables = arrayOfNulls<Drawable>(images.size)

    private var isNext = true
    private fun initView() {
        binding.carousel.setAdapter(object :Carousel.Adapter{
            override fun count(): Int = images.size

            override fun populate(view: View?, index: Int) {
                logd("populate view=$view index=$index")
                if(view is ImageView){
//                    view.load(images[index])
                    val targetDrawable= drawables.getOrNull(index)
                    if (targetDrawable == null) {
                        view.loadDrawable(this@MotionCarouselAc,images[index]){
                            drawables[index] = it
                            view.setImageDrawable(it)
                        }
                    }else{
                        view.setImageDrawable(targetDrawable)
                    }
                }
            }

            override fun onNewItem(index: Int) {
                logd("onNewItem=>${index}")
            }

        })

        val fragment = DebugFragment()
        addFragment(android.R.id.content, fragment)
        Handler().postDelayed({
            fragment.apply {
            findViewById<View>(R.id.v1)?.click {
                isNext = !isNext
                startInterval()
            }
        }
        },2000)

    }

    private fun startInterval(){
        interval {
            loge("interval->${it}")
            if (binding.carousel.count > 0) {
                if (isNext) {
                    binding.motionLayout.transitionToEnd {}
                } else {
                    binding.motionLayout.transitionToStart()
                }
            }
        }
    }

}