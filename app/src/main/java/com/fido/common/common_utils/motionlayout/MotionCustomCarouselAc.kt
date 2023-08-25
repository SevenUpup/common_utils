package com.fido.common.common_utils.motionlayout

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.imageview.load
import com.fido.common.common_base_ui.ext.imageview.loadDrawable
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.util.timer.interval
import com.fido.common.common_utils.databinding.AcMotionCustonCarouselBinding
import com.fido.common.common_utils.widgets.dp

/**
@author FiDo
@description:
@date :2023/6/8 9:40
 */
class MotionCustomCarouselAc:AppCompatActivity() {

    val binding:AcMotionCustonCarouselBinding by binding()

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

//    private val images = listOf(R.mipmap.flower, R.drawable.ic_zelda,R.mipmap.ic_launcher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val radius = 10f.dp.toFloat()

        binding.carousel.setAdapter(object :Carousel.Adapter{
            override fun count(): Int = images.size

            override fun populate(view: View?, index: Int) {
                if (view is ImageView) {
                    view.load(images[index], topLeftRadius = radius, topRightRadius = radius, bottomLeftRadius = radius, bottomRightRadius = radius, dontAnimate = true)
                }
            }

            override fun onNewItem(index: Int) {

            }

        })

        val imageDrawables = arrayOfNulls<Drawable>(images.size)
        binding.carousel2.setAdapter(object :Carousel.Adapter{
            override fun count(): Int = images.size

            override fun populate(view: View?, index: Int) {
                if (view is ImageView) {
                    val drawable = imageDrawables.getOrNull(index)
                    if (drawable == null) {
                        view.loadDrawable(this@MotionCustomCarouselAc, images[index]) {
                            view.setImageDrawable(it)
                            imageDrawables[index] = it
                        }
                    } else {
                        view.setImageDrawable(drawable)
                        imageDrawables[index] = drawable
                    }
//                    view.load(images[index], topLeftRadius = radius, topRightRadius = radius, bottomLeftRadius = radius, bottomRightRadius = radius)
                }
            }
            override fun onNewItem(index: Int) {
            }
        })

        interval(initialDelay = 2){
            binding.motionLayout.transitionToEnd()
            binding.motionLayout2.transitionToEnd()
        }
        bindClick()
    }

    private fun bindClick() {
        //使用自定义的 MotionLayout 重写OnTouchEvent
        binding.motionLayout2.block = {
            loge("url=${images[binding.carousel2.currentIndex]}")
            toast("pos=${binding.carousel2.currentIndex} url=${images[binding.carousel2.currentIndex]}")
        }
    }


}
