package com.fido.common.common_utils.widgets.tab

import android.content.Context
import android.util.AttributeSet
import com.fido.common.common_base_ui.widget.tab.HBaseTabLayout
import com.fido.common.common_base_ui.widget.tab.TabItemConfig
import com.fido.common.common_base_ui.widget.tab.TabItemConfig.ItemBuilder
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.getColor
import com.fido.common.R

/**
 * @author: FiDo
 * @date: 2024/5/21
 * @des:
 */
class CornerBlue2WhiteTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : HBaseTabLayout(context, attrs) {
    override fun tabs(): MutableList<String> {
        return mutableListOf()
    }

    override fun tabBackGroundColor(): Int {
        return R.color.colorBgBtnNormal.getColor
    }

    override fun tabRadiusPx(): Int {
        return 0
    }

    override fun tabItemBuilder(): TabItemConfig {
        return ItemBuilder()
            .setItemSelectBlod(false)
            .setItemSelectColor(resources.getColor(android.R.color.white))
            .setItemUnSelectColor(resources.getColor(android.R.color.black))
            .setItemSelectBgRes(com.luck.picture.lib.R.drawable.ps_select_complete_bg)
            .setItemUnSelectBgRes(com.luck.picture.lib.R.drawable.ps_select_complete_normal_bg)
            .setItemInnerHorPadding(10f.dp)
            .setItemInnerVerPadding(6.dp)
            .setItemMargin(8.dp)
            .build()
    }


}