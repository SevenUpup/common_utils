package com.fido.common.common_base_ui.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

/**
@author FiDo
@description:
@date :2023/5/17 15:39
 */

fun FragmentActivity.FragmentStateAdapter(
    vararg fragments: Fragment,
    isLazyLoading: Boolean = false
): FragmentStateAdapter =
    FragmentStateAdapter(fragments.size, isLazyLoading) { fragments[it] }

fun Fragment.FragmentStateAdapter(
    vararg fragments: Fragment,
    isLazyLoading: Boolean = false
): FragmentStateAdapter = FragmentStateAdapter(fragments.size, isLazyLoading) { fragments[it] }


internal fun FragmentActivity.FragmentStateAdapter(
    itemCount: Int,
    isLazyLoading: Boolean = false,
    block: (Int) -> Fragment
) =
    CommonFragmentStateAdapter(supportFragmentManager, lifecycle, itemCount, isLazyLoading, block)

internal fun Fragment.FragmentStateAdapter(
    itemCount: Int,
    isLazyLoading: Boolean = false,
    block: (Int) -> Fragment
): FragmentStateAdapter =
    CommonFragmentStateAdapter(childFragmentManager, lifecycle, itemCount, isLazyLoading, block)

internal fun CommonFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    itemCount: Int,
    isLazyLoading: Boolean = false,
    block: (Int) -> Fragment,
) = object : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = itemCount

    override fun createFragment(position: Int) = block(position).apply {
        /**
         * 本来想 利用反射 set fragment 的 tag 主要 给[findFragment] 用的
         * 发现[FragmentStateAdapter.placeFragmentInViewHolder]  line: 338 会自己拼 f + itemId = "f0"
         * mFragmentManager.beginTransaction()
        .add(fragment, "f" + holder.getItemId())
        .setMaxLifecycle(fragment, STARTED)
        .commitNow();
         */

        /*try{
            Fragment::class.java.getDeclaredField("mTag").run {
                isAccessible = true
                set(this@apply,"f${position}")
            }
        }catch (e:Exception){ e.printStackTrace() }*/
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (isLazyLoading) recyclerView.setItemViewCacheSize(itemCount)
    }
}


fun <T : Fragment> ViewPager2.findFragment(fragmentManager: FragmentManager, position: Int) =
    fragmentManager.findFragmentByTag("f$position") as T?
