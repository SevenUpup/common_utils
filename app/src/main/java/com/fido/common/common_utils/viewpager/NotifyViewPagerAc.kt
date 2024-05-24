package com.fido.common.common_utils.viewpager

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.addOnTabSelectedListener
import com.fido.common.common_base_ui.ext.setCustomView
import com.fido.common.common_base_ui.ext.setupWithViewPager
import com.fido.common.common_base_ui.ext.tabs
import com.fido.common.common_base_ui.util.sp
import com.fido.common.common_base_util.ext.*
import com.fido.common.R
import com.fido.common.databinding.AcNotityViewpagerBinding
import com.fido.common.common_utils.fragment.Nav1Fragment
import com.fido.common.common_utils.test.FunTest.Companion.also
import kotlin.properties.Delegates

/**
@author FiDo
@description:
@date :2023/8/7 15:55
 */

fun <T:View>Activity.id(id:Int) = lazy {
    findViewById<T>(id)
}

class NotifyViewPagerAc :AppCompatActivity(){

    val binding:AcNotityViewpagerBinding by binding()

    val plusBt by id<TextView>(R.id.plus)

    private val fragmentList = mutableListOf(
        Nav1Fragment.getInstance("Fragmeng1"),
        Nav1Fragment.getInstance("Fragmeng2"),
        Nav1Fragment.getInstance("Fragmeng3"),
    )

    private val pageTitles = mutableListOf(
        "tab0",
        "tab1",
        "tab2",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        initEvent()
    }

    private var lastClickTime by Delegates.observable(0L){ _, oldValue, newValue ->
        // 在lastClickTime属性值发生变化时执行点击事件
        if (newValue - oldValue > 1000) {
            plusFragment()
        }
    }

    private fun initEvent() {
        binding.plus.setOnClickListener {
            //每次点击都会触发Delegates.observable()
            lastClickTime = System.currentTimeMillis()
        }
//        binding.plus.click {
//            plusFragment()
//        }

        binding.remove.click {
            fragmentList.removeLast()
            pageTitles.removeLast()
            toast("删除了最后一个fragment")
            adapterNotify()
        }

        binding.edit.click {
            fragmentList[1] = Nav1Fragment.getInstance("我是改过之后的Fragment")
            adapterNotify()
            binding.mVp.currentItem = 1
            toast("更改了第二个fragment文案")
        }

        binding.change.click {
            if (fragmentList.isNullOrEmpty() || fragmentList.size<3){
                toast("不够三个，别点了")
                return@click
            }
//            Collections.swap(fragmentList,0,2)
            fragmentList.swap(0,2)
            pageTitles.swap(0,2)
            adapterNotify()
//            binding.mVp.offscreenPageLimit = fragmentList.size-1
            toast("调换了 第一和第三fragment的位置")
        }
    }

    private fun plusFragment() {
        fragmentList.add(Nav1Fragment.getInstance("Fragment${fragmentList.size+1}"))
        pageTitles.add("tab${pageTitles.size}")
        toast("添加了一个新的fragment")
        adapterNotify()
    }

    private fun adapterNotify() {
        binding.mVp.adapter?.notifyDataSetChanged()
        bindTabLayout()
    }

    private fun initView() {

        binding.mVp.adapter = ViewPagerFragmentAdapter(supportFragmentManager,fragmentList,pageTitles)

        bindTabLayout()

        binding.tabLayout.addOnTabSelectedListener(onTabSelected = {tab->
            binding.tabLayout.tabs.forEach {
                it?.view?.findViewById<TextView>(R.id.mTitle)?.isFakeBoldText = it?.view  == tab.view
            }
        })
    }

    private fun bindTabLayout() {
        binding.tabLayout.setupWithViewPager(binding.mVp,true) { tab, position ->
            tab.setCustomView(R.layout.item_rv_text) {
                findViewById<TextView>(R.id.bt_up).isVisible = false
                findViewById<TextView>(R.id.bt_down).isVisible = false
                findViewById<TextView>(R.id.mTitle).also {
                    it.text = pageTitles[position]
                    it.textSize = 8.sp.toFloat()
                }
            }
        }
    }

    fun <T> MutableList<T>.swap(index1: Int, index2: Int){
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

}