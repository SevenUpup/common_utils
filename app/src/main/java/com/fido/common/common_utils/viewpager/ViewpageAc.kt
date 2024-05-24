package com.fido.common.common_utils.viewpager

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.ext.*
import com.fido.common.common_base_ui.util.dp
import com.fido.common.common_base_ui.util.sp
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.startActivity
import com.fido.common.R
import com.fido.common.databinding.AcViewpageBinding
import com.fido.common.common_utils.fragment.Nav1Fragment
import com.fido.common.common_utils.fragment.Nav2Fragment
import com.fido.common.common_utils.fragment.Nav3Fragment
import com.fido.common.common_utils.test.FunTest.Companion.also
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.reflect.full.declaredMemberFunctions

/**
@author FiDo
@description:
@date :2023/5/17 16:03
 */
class ViewpageAc : BaseVBActivity<AcViewpageBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.ac_viewpage
    }

    private var index = 1

    override fun initView() {
        appLanguage = Locale.ENGLISH

        binding.btVp2.click {
            startActivity<Vp2Ac>()
        }

        binding.tvVpNotify.click {
            startActivity<NotifyViewPagerAc>()
        }

        binding.tvClick.apply {
            setDrawable(leftDrawable = R.drawable.ic_zelda, rightDrawable = R.drawable.bg_date_label)
            expandClickArea(25.dp)
            click {
                toast("click area")
                try {
//                    fList.add(0,TestFragment.newInstance())
//                    binding.mVp2.setCurrentItem(1,false)
                }catch (e:Exception){
                    logd(e.toString())
                }
            }
        }
        binding.tabLayout.addOnTabSelectedListener(onTabSelected = {
            when (it.position) {
                0->{

                }
                1-> showKeyboard()
                2-> hideKeyboard()
            }
        })
        binding.mVp.bind(fList.size, bindView = {container,position->
            ImageView(this).apply {
                setImageResource(R.mipmap.flower)
                scaleType = ImageView.ScaleType.CENTER_CROP
                widthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,200.dp)

                click {
                    index ++
                    bindVp2Adapter()

                    binding.mVp2.post {
                        val fragment = binding.mVp2.findFragment<Nav1Fragment>(supportFragmentManager,0)
                        Nav1Fragment::class.java.kotlin.run {
                            this.declaredMemberFunctions.find { it.name == "test" }.also {
                                it?.call(fragment)
                            }
                        }
                    }
                }
            }
        })
        binding.mVp.asCard(50.dp, 0.dp)

        bindVp2Adapter()

        binding.tabLayout.setupWithViewPager(binding.mVp,true) { tab, i ->
            tab.setCustomView(R.layout.item_rv_text) {
                findViewById<TextView>(R.id.bt_up).isVisible = false
                findViewById<TextView>(R.id.bt_down).isVisible = false
                findViewById<TextView>(R.id.mTitle).also {
                    it.text = "Tab ${i}"
                    it.textSize = 10.sp.toFloat()
                }
            }
        }

        binding.tabLayout.setupWithViewPager2(binding.mVp2,false,false) { tab: TabLayout.Tab, i: Int ->
            tab.setCustomView(R.layout.layout_header_view){
                findViewById<TextView>(R.id.tv_header_title).apply {
                    text = "new Tab$i"
                }
            }
        }
    }

    private val fList = mutableListOf<Fragment>(TestFragment.newInstance(),Nav1Fragment.getInstance(""),Nav2Fragment.getInstance(""),Nav3Fragment.getInstance(""))
    private fun bindVp2Adapter() {
        if (index % 2 == 0) {
            binding.mVp2.adapter =
                FragmentStateAdapter(*fList.toTypedArray(), isLazyLoading = false)
        } else {
            binding.mVp2.bindFragment(supportFragmentManager,lifecycle,fList)
        }
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}