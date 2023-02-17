package com.fido.common.common_base_util.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fido.common.common_base_util.dp2px

/**
ViewPager相关

绑定数据适配器 绑定Fragment适配器
viewPager.bind(10, bindView = {container, position ->
return@bind TextView(this)
})

pager.asCard() //通过参数可以调节卡片的距离和大小
 */

/**
 * 给ViewPager绑定数据
 */
fun ViewPager.bind(count: Int, bindView: (container: ViewGroup, position: Int) -> View): ViewPager {
    adapter = object : PagerAdapter() {
        override fun isViewFromObject(v: View, p: Any) = v == p
        override fun getCount() = count
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = bindView(container, position)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
    offscreenPageLimit = count - 1
    return this
}

/**
 * 给ViewPager绑定Fragment
 */
fun ViewPager.bindFragment(
    fm: FragmentManager,
    fragments: List<Fragment>,
    pageTitles: List<String>? = null,
    behavior: Int = 0
): ViewPager {
    offscreenPageLimit = fragments.size - 1
    adapter = object : FragmentStatePagerAdapter(fm, behavior) {
        override fun getItem(p: Int) = fragments[p]
        override fun getCount() = fragments.size
        override fun getPageTitle(p: Int) = if (pageTitles == null) null else pageTitles[p]
    }
    return this
}

/**
 * 给ViewPager2绑定Fragment
 */
fun ViewPager2.bindFragment(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>
): ViewPager2 {
    offscreenPageLimit = fragments.size - 1

    adapter = object : FragmentStateAdapter(fm, lifecycle) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    return this
}

/**
 * 让ViewPager展示卡片效果
 * @param pageMargin 用来调节卡片之间的距离
 * @param padding 用来调节ViewPager的padding
 */
fun ViewPager.asCard(
    pageMargin: Int = dp2px(30.toFloat()),
    padding: Int = dp2px(45.toFloat())
): ViewPager {
    setPageTransformer(false, CardPagerTransformer(context))
    setPageMargin(pageMargin)
    clipToPadding = false
    setPadding(padding, padding, padding, padding)
    return this
}

class CardPagerTransformer(context: Context) : ViewPager.PageTransformer {
    private val maxTranslateOffsetX: Int = context.dp2px(180f)
    private var viewPager: ViewPager? = null

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }
        val leftInScreen = view.left - viewPager!!.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.38f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)
        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }
}
