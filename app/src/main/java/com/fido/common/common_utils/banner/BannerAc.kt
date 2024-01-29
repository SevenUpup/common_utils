package com.fido.common.common_utils.banner

import com.fido.common.base.BaseVBActivity
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcBannerBinding
import com.fido.common.common_utils.widgets.DotIndicatorView
import com.fido.common.common_utils.widgets.ImageAdapter
import com.fido.common.common_utils.widgets.ImageAdapter2
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.listener.OnPageChangeListener
import com.youth.banner.transformer.AlphaPageTransformer
import com.youth.banner.transformer.ZoomOutPageTransformer

/**
@author FiDo
@description:
@date :2023/3/31 19:21
 */
class BannerAc : BaseVBActivity<AcBannerBinding>() {

    companion object {

        val imageUrls = listOf(
            "https://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png",
            "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
            "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
            "https://img.zcool.cn/community/01700557a7f42f0000018c1bd6eb23.jpg",
            "https://img.zcool.cn/community/01b72057a7e0790000018c1bf4fce0.png",
            "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg",
            "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
            "https://img.zcool.cn/community/01700557a7f42f0000018c1bd6eb23.jpg"
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.ac_banner
    }

    override fun initView() {

        val indicatorView = findViewById<DotIndicatorView>(R.id.indicatorView)
        indicatorView.setCount(imageUrls.size)
        val banner = findViewById<Banner<String, ImageAdapter>>(R.id.banner)
        banner.apply {
            addBannerLifecycleObserver(this@BannerAc)
            setBannerRound(20f)
            setAdapter(ImageAdapter(imageUrls))
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                }

                override fun onPageSelected(p0: Int) {
                    indicatorView.setSelectedIndex(p0)
                }

                override fun onPageScrollStateChanged(p0: Int) {
                }
            })
        }
        banner.setOnBannerListener(object :OnBannerListener<String>{
            override fun OnBannerClick(data: String?, position: Int) {

            }

        })

        val banner2 = findViewById<Banner<String, ImageAdapter2>>(R.id.banner2)
        val adapter = ImageAdapter2(imageUrls, this)

        banner2.apply {
            var isFirst = true
            addBannerLifecycleObserver(this@BannerAc)
//            setBannerGalleryEffect(50,50,0,0.5f)
            setBannerGalleryMZ(48, 0.8f)
            setAdapter(adapter)
//            addPageTransformer(DepthPageTransformer())
            addPageTransformer(AlphaPageTransformer())
//            addPageTransformer(RotateYTransformer())
//            addPageTransformer(ScaleInTransformer())
            addPageTransformer(ZoomOutPageTransformer())

            viewPager2.offscreenPageLimit = 5
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(p0: Int) {
//                    if(!isFirst) return
//                    isFirst = false

                    /*adapter.viewHolder.imageView.apply {
                        margin((-5).dp, 0, (-5).dp, 0)
//                        val bannerClazz = Class.forName("com.youth.banner.Banner")
//                        val method =
//                            bannerClazz.getDeclaredMethod("setRecyclerViewPadding", Int::class.java)
//                        method.isAccessible = true
//                        method.invoke(banner2, (-5).dp)
//                        banner2.viewPager2.margin(5.dp,0,5.dp,0)
                    }*/
                }

                override fun onPageScrollStateChanged(p0: Int) {}
            })
        }

    }

    override fun initData() {

    }

    override fun initEvent() {
    }




}