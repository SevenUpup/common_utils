package com.fido.common.common_base_util.ext

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fido.common.common_base_util.dp2px
import java.lang.IllegalArgumentException
import java.util.*

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
 * @param behavior behavior 参数，当为1的时候就不会回调 setUserVisibleHint 方法了，我们直接监听 OnResume 即可
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

/**
 * 动态更新 数据源 后
 * 调用viewPager.adapter?.notifyDataSetChanged() 刷新页面
 */
//https://mp.weixin.qq.com/s/iO5FD4Cqqr0rnY-byrDdAQ
class ViewPagerFragmentAdapter(
    private val fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val pageTitles: List<String>? = null,
    behavior: Int = 0,
) : FragmentStatePagerAdapter(fm, behavior) {

    private val fragmentMap = mutableMapOf<Int, Fragment>()
    private val fragmentPositions = hashMapOf<Int, Int>()

    init {
        for ((index, fragment) in fragments.withIndex()) {
            fragmentMap[index] = fragment
        }
    }

    override fun getCount(): Int {
        return if (fragments.isEmpty()) 0 else fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (pageTitles == null) "" else pageTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment

        val id = generateUniqueId()
        var args = fragment.arguments
        if (args == null) {
            args = Bundle()
        }

        args.putInt("_uuid", id)
        fragment.arguments = args

        // 存储 Fragment 的位置信息
        fragmentPositions[id] = position

        return fragment
    }

    private fun generateUniqueId(): Int {
        // 生成唯一的 ID
        return UUID.randomUUID().hashCode()
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        super.destroyItem(container, position, obj)
    }

    override fun getItemPosition(obj: Any): Int {

        val fragment = obj as Fragment

        // 从 Fragment 中获取唯一的 ID
        val args = fragment.arguments
        if (args != null && args.containsKey("_uuid")) {
            val id = args.getInt("_uuid")

            // 根据 ID 获取 Fragment 在 Adapter 中的位置
            val position = fragmentPositions[id]
            return if (position != null && position == fragments.indexOf(fragment)) {
                // Fragment 未发生变化，返回 POSITION_UNCHANGED
                POSITION_UNCHANGED
            } else {
                // Fragment 发生变化，返回 POSITION_NONE
                POSITION_NONE
            }
        }

        // 如果不是 Fragment，则返回默认值
        return super.getItemPosition(obj)
    }

}

// =========================================== Vp2 ============================================
/**
 * 给ViewPager2绑定 Fragment
 */
fun ViewPager2.bindFragment(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    fragments: List<Fragment>,
    offsetLimit: Int = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
): ViewPager2 {
//    offscreenPageLimit = if (offsetLimit > 0) offsetLimit else ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
//    adapter = object : FragmentStateAdapter(fm, lifecycle) {
//        override fun getItemCount(): Int = fragments.size
//        override fun createFragment(position: Int): Fragment = fragments[position]
//        //通过重写getItemId 实现动态添加数据时 执行 createFragment
//        override fun getItemId(position: Int): Long = fragments[position].hashCode().toLong()
//    }
    offscreenPageLimit = if (offsetLimit > 0) offsetLimit else ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
    adapter = CommonVp2FragmentStateAdapter(fm, lifecycle, fragments.toMutableList())
    return this
}

 class CommonVp2FragmentStateAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val fragments: MutableList<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
    override fun getItemId(position: Int): Long{
//        return fragments[position].hashCode().toLong()
        val name = fragments[position].javaClass.simpleName + position
        val toLong = name.hashCode().toLong()
        return toLong
    }
}

val ViewPager2.recyclerView get() = this.getChildAt(0) as RecyclerView

fun ViewPager2.addFragment(vararg fragments: Fragment, position: Int,isScrollCurrent:Boolean = true) =
    apply {
        if (adapter !is CommonVp2FragmentStateAdapter) {
            throw IllegalArgumentException("please use ViewPage2.bindFragment() first ! and check adapter is CommonVp2FragmentStateAdapter")
        } else {
            try {
                val mCommonAdapter = adapter as CommonVp2FragmentStateAdapter
                if (mCommonAdapter.fragments.isNullOrEmpty()) {
                    mCommonAdapter.fragments.addAll(fragments.toList())
                    mCommonAdapter.notifyDataSetChanged()
                } else {
                    if (position < 0) {
                        mCommonAdapter.fragments.addAll(fragments.toList())
                    } else {
                        mCommonAdapter.fragments.addAll(position, fragments.toList())
                    }
                    //自动定位到当前添加的位置
                    if (isScrollCurrent) {
                        // position < 0 说明是尾部添加，滚动到最后 pos == 0 说明前置位添加 直接滚动第一个  pos>0 说明中间插入，滚到到插入的下边
                        val targetPos = when{
                            position > 0 -> position
                            position < 0 -> mCommonAdapter.fragments.size - 1
                            else -> currentItem + fragments.size
                        }
                        setCurrentItem(targetPos ,false)
                    }
                }
            } catch (_: Exception) { }
        }
    }

/**
 * 给 ViewPager2 添加前置Fragment
 */
fun ViewPager2.addFragment2Front(vararg fragments: Fragment,isScrollCurrent: Boolean=true) = apply { addFragment(*fragments, position = 0, isScrollCurrent = isScrollCurrent) }
/**
 * 给 ViewPager2 添加Fragment
 */
fun ViewPager2.addFragment(vararg fragments: Fragment,isScrollCurrent: Boolean = true) = apply { addFragment(*fragments,position = -1,isScrollCurrent = isScrollCurrent) }

fun ViewPager2.removeFragment(position: Int) = apply {
    if (adapter is CommonVp2FragmentStateAdapter) {
        (adapter as CommonVp2FragmentStateAdapter).let {
            it.fragments.removeAt(position)
            it.notifyItemRemoved(position)
        }
    }
}

fun ViewPager2.clearFragments() = apply {
    if (adapter is CommonVp2FragmentStateAdapter) {
        (adapter as CommonVp2FragmentStateAdapter).let {
            it.fragments.clear()
            it.notifyDataSetChanged()
        }
    }
}

/**
 * @param viewLayoutRes ViewPage2 需要填充的 layout id
 * @param bindView      callback onBindViewHolder() you can do some init
 */
fun ViewPager2.bindView(
    itemCount:Int,
    @LayoutRes viewLayoutRes:Int,
    offsetLimit:Int = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT,
    bindView:(View.(position:Int)->Unit)?=null,
) = apply {
    if (offsetLimit > 0) {
        offscreenPageLimit = offsetLimit
    }
    adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = VH(LayoutInflater.from(parent.context).inflate(viewLayoutRes,parent,false))
        override fun getItemCount(): Int = itemCount
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            bindView?.invoke(holder.itemView,position)
        }
        inner class VH(itemView: View) :RecyclerView.ViewHolder(itemView)
    }

}
