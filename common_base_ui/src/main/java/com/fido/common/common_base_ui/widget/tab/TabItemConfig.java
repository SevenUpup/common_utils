package com.fido.common.common_base_ui.widget.tab;

import static com.fido.common.common_base_ui.widget.tab.DrawablKt.getCustomDrawable;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * @author: FiDo
 * @date: 2024/5/10
 * @des:
 */
public class TabItemConfig{
    // 默认选中项
    public int mDefaultSelectPos;
    //选中 item color
    public int itemSelectColor;
    //未选中 item color
    public int itemUnSelectColor;
    //选中 item 背景
    public Drawable itemSelectBg;
    public int itemSelectBgRes;
    // 未选中 item 背景
    public Drawable itemUnSelectBg;
    public int itemUnSelectBgRes;
    // 选中是否加粗
    public boolean itemSelectBlod;
    // item距离 tab 容器的 间距
    public int itemMargin;
    // item 文字 横向 padding 用于将 item 文字 横向撑开
    public int itemInnerHorPadding;
    // item 文字 竖向 padding 用于将 item 文字 竖向撑开
    public int itemInnerVerPadding;
    public int itemTextSize;

    private TabItemConfig(ItemBuilder builder){
        this.mDefaultSelectPos = builder.mDefaultSelectPos;
        this.itemSelectColor = builder.itemSelectColor;
        this.itemUnSelectColor = builder.itemUnSelectColor;
        this.itemSelectBg = builder.itemSelectBg;
        this.itemUnSelectBg = builder.itemUnSelectBg;
        this.itemSelectBlod = builder.itemSelectBlod;
        this.itemMargin = builder.itemMargin;
        this.itemInnerHorPadding = builder.itemInnerHorPadding;
        this.itemInnerVerPadding = builder.itemInnerVerPadding;
        this.itemTextSize = builder.itemTextSize;
        this.itemSelectBgRes = builder.itemSelectBgRes;
        this.itemUnSelectBgRes = builder.itemUnSelectBgRes;
    }

    public static class ItemBuilder{
        // 默认选中项
        private int mDefaultSelectPos = 0;
        //选中 item color
        private int itemSelectColor = Color.parseColor("#333333");
        //未选中 item color
        private int itemUnSelectColor = Color.parseColor("#666666");
        //选中 item 背景
        private Drawable itemSelectBg = getCustomDrawable(Color.parseColor("#FFFFFF"), 0);
        // 未选中 item 背景
        private Drawable itemUnSelectBg = getCustomDrawable(Color.parseColor("#F6F6F6"),0);
        // 选中是否加粗
        private boolean itemSelectBlod = true;
        // item距离 tab 容器的 间距
        private int itemMargin = 0;
        // item 文字 横向 padding 用于将 item 文字 横向撑开
        private int itemInnerHorPadding = 28;
        // item 文字 竖向 padding 用于将 item 文字 竖向撑开
        private int itemInnerVerPadding = 16;
        private int itemTextSize = 12;
        public int itemUnSelectBgRes;
        public int itemSelectBgRes;

        public ItemBuilder setItemUnSelectBgRes(int itemUnSelectBgRes) {
            this.itemUnSelectBgRes = itemUnSelectBgRes;
            return this;
        }

        public ItemBuilder setItemSelectBgRes(int itemSelectBgRes) {
            this.itemSelectBgRes = itemSelectBgRes;
            return this;
        }

        public ItemBuilder setTabDefaultSelectPos(int pos){
            this.mDefaultSelectPos = pos;
            return this;
        }

        public ItemBuilder setItemSelectColor(int itemSelectColor) {
            this.itemSelectColor = itemSelectColor;
            return this;
        }

        public ItemBuilder setItemUnSelectColor(int itemUnSelectColor) {
            this.itemUnSelectColor = itemUnSelectColor;
            return this;
        }

        public ItemBuilder setItemSelectBg(Drawable itemSelectBg) {
            this.itemSelectBg = itemSelectBg;
            return this;
        }

        public ItemBuilder setItemUnSelectBg(Drawable itemUnSelectBg) {
            this.itemUnSelectBg = itemUnSelectBg;
            return this;
        }

        public ItemBuilder setItemSelectBlod(boolean itemSelectBlod) {
            this.itemSelectBlod = itemSelectBlod;
            return this;
        }

        public ItemBuilder setItemInnerHorPadding(int itemInnerHorPadding) {
            this.itemInnerHorPadding = itemInnerHorPadding;
            return this;
        }

        public ItemBuilder setItemInnerVerPadding(int itemInnerVerPadding) {
            this.itemInnerVerPadding = itemInnerVerPadding;
            return this;
        }

        public ItemBuilder setItemMargin(int itemMargin) {
            this.itemMargin = itemMargin;
            return this;
        }

        public ItemBuilder setItemTextSize(int itemTextSize) {
            this.itemTextSize = itemTextSize;
            return this;
        }

        public TabItemConfig build(){
            return new TabItemConfig(this);
        }

    }
}
