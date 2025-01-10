package com.fido.common.common_base_ui.ext

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 设置分割线
 * @param color 分割线的颜色，默认是#DEDEDE
 * @param size 分割线的大小，默认是1px
 * @param isReplace 是否覆盖之前的ItemDecoration，默认是false
 */
fun RecyclerView.divider(
    color: Int = Color.parseColor("#DEDEDE"),
    size: Int = 1,
    isReplace: Boolean = false
): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        setSize(size, size)
    })
    if (isReplace && itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
    addItemDecoration(decoration)
    return this
}

/**
 * 设置垂直的Manager,默认不是瀑布流
 */
fun RecyclerView.vertical(
    spanCount: Int = 0,
    isStaggered: Boolean = false
): RecyclerView {
    layoutManager =
        if (spanCount > 0) GridLayoutManager(context, spanCount) else LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

/**
 * 设置水平的Manager,默认不是瀑布流
 */
fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = if (spanCount > 0) GridLayoutManager(
        context,
        spanCount,
        GridLayoutManager.HORIZONTAL,
        false
    ) else LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}


inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }

