package com.fido.common.common_base_ui.ext

import androidx.recyclerview.widget.RecyclerView

/**
@author FiDo
@description:
@date :2023/6/13 8:58
 */

/**
 * Rv是否滚动到底部
 */
fun RecyclerView.isSlideToBottom(): Boolean =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset() >= this.computeVerticalScrollRange())

/**
 * (1)的值表示是否能向上滚动，false表示已经滚动到底部
 */
val RecyclerView.canScrollUp
    get() = this.canScrollVertically(1)

/**
 * (-1)的值表示是否能向下滚动，false表示已经滚动到顶部
 */
val RecyclerView.canScrollDown
    get() = this.canScrollVertically(-1)