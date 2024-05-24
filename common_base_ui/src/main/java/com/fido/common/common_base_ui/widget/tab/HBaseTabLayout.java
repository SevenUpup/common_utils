package com.fido.common.common_base_ui.widget.tab;

import static com.fido.common.common_base_ui.widget.tab.DrawablKt.getCustomDrawable;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.fido.common.common_base_ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: FiDo
 * @date: 2024/5/10
 * @des:  自定义背景/圆角/选中状态 的 TabLayout(内部Rv实现)
 * 如果出现 item背景显示不全问题，请查看itemSelectBg设置方式，改成设置itemSelectBgRes的方式试下
 */
public abstract class HBaseTabLayout extends LinearLayout {
    // 内部 Rv
    private RecyclerView mRv;
    private TabAdapter mAdapter;
    // 事件
    private OnTabSelectListener onTabSelectListener;
    private ViewPager bindVp;
    private ViewPager2 viewPager2;
    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.onTabSelectListener = onTabSelectListener;
    }

    public HBaseTabLayout(Context context) {
        this(context,null);
    }

    public HBaseTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HBaseTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @return 数据源
     */
    public abstract List<String> tabs();

    /**
     * @return tab 背景色
     */
    public abstract int tabBackGroundColor();

    /**
     *  tab圆角
     */
    public abstract int tabRadiusPx();

    /**
     * @return 配置 tab item 的一些属性
     */
    public abstract TabItemConfig tabItemBuilder();

    private void init() {
        if (tabs() != null) {
            mRv = new RecyclerView(getContext());
            mRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(mRv);
            initRv();
            this.setBackground(getCustomDrawable(tabBackGroundColor(),tabRadiusPx()));
        }
    }

    private void initRv() {
        if (mRv != null) {
            mRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            mAdapter = new TabAdapter(tabItemBuilder());
            mRv.setAdapter(mAdapter);
            mAdapter.appendToList(tabs());

            mAdapter.setAdapterClickListener((view, position) -> {
                if (mAdapter.config != null) {
                    int mDefaultSelectPos = mAdapter.config.mDefaultSelectPos;
                    if (position != mDefaultSelectPos) {
                        mAdapter.config.mDefaultSelectPos = position;
                        mAdapter.notifyDataSetChanged();

                        mRv.smoothScrollToPosition(position);
                        if (onTabSelectListener != null) {
                            onTabSelectListener.onTabSelect(position);
                        }
                        if (bindVp != null) {
                            bindVp.setCurrentItem(position);
                        }
                        if (this.viewPager2 != null) {
                            this.viewPager2.setCurrentItem(position);
                        }
                    }
                }
            });
        }
    }

    public void setTabs(List<String> items){
        setTabs(items,0);
    }

    public void setTabs(List<String> items,int defaultSelectPos){
        if (mAdapter != null) {
            mAdapter.clear();
            if (defaultSelectPos > 0) {
                mAdapter.config.mDefaultSelectPos = defaultSelectPos;
            }
            mAdapter.appendToList(items);
        }
    }

    public void bindViewPage(ViewPager vp){
        if (vp == null) return;
        this.bindVp = vp;
        bindVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageSelected(int position) {
                doOnPageSelect(position);
            }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    private void doOnPageSelect(int position) {
        if (mAdapter != null && mAdapter.config != null) {
            int defaultSelectPos = mAdapter.config.mDefaultSelectPos;
            if (defaultSelectPos != position) {
                mAdapter.config.mDefaultSelectPos = position;
                mAdapter.notifyDataSetChanged();
                if (onTabSelectListener != null) {
                    onTabSelectListener.onTabSelect(position);
                }
            }
        }
    }

    public void bindViewPage2(ViewPager2 viewPager2){
        if (viewPager2 == null) return;
        this.viewPager2 = viewPager2;
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                doOnPageSelect(position);
            }
        });
    }

    public void setSelectPosition(int position){
        int mDefaultSelectPos = mAdapter.config.mDefaultSelectPos;
        if (position != mDefaultSelectPos) {
            mAdapter.config.mDefaultSelectPos = position;
        }
        mAdapter.notifyDataSetChanged();
    }

    private class TabAdapter extends RecyclerView.Adapter<TabAdapter.ItemsHolder>{
        private OnAdapterClickListener adapterClickListener;

        public void setAdapterClickListener(OnAdapterClickListener adapterClickListener) {
            this.adapterClickListener = adapterClickListener;
        }

        private List<Object> data;
        public TabItemConfig config;
        public TabAdapter(TabItemConfig config) {
            this.config = config;
        }

        public void clear(){
            if (this.data != null) {
                this.data.clear();
            }
        }
        public void appendToList(List data){
            if (this.data == null) {
                this.data = new ArrayList<>();
            }
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_h_base_tab_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemsHolder holder, int position) {
            Object data = this.data.get(position);
            if (data != null) {
                TextView name = holder.name;
                ViewGroup container = holder.container;
                int mDefaultSelectPos = config.mDefaultSelectPos;
                boolean itemSelectBlod = config.itemSelectBlod;
                int itemSelectColor = config.itemSelectColor;
                int itemUnSelectColor = config.itemUnSelectColor;
                int itemMargin = config.itemMargin;
                Drawable itemSelectBg;
                if (config.itemSelectBgRes != 0) {
                    itemSelectBg = getResources().getDrawable(config.itemSelectBgRes);
                } else {
                    itemSelectBg = config.itemSelectBg;
                }
                Drawable itemUnSelectBg;
                if (config.itemUnSelectBgRes != 0) {
                    itemUnSelectBg = getResources().getDrawable(config.itemUnSelectBgRes);
                } else {
                    itemUnSelectBg = config.itemUnSelectBg;
                }
                name.setText(data.toString());
                if (itemMargin > 0) {
                    ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
                    if (layoutParams instanceof MarginLayoutParams) {
                        ((MarginLayoutParams) layoutParams).setMargins(itemMargin,itemMargin,itemMargin,itemMargin);
                    }
                }
                int itemTextHorPadding = config.itemInnerHorPadding;
                int itemTextVerPadding = config.itemInnerVerPadding;
                if (itemTextHorPadding >= 0 && itemTextVerPadding>=0) {
                    name.setPadding(itemTextHorPadding,itemTextVerPadding,itemTextHorPadding,itemTextVerPadding);
                }
                if (itemSelectBlod) {
                    name.setTypeface(Typeface.defaultFromStyle(position == mDefaultSelectPos ? Typeface.BOLD : Typeface.NORMAL));
                } else {
                    name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                int itemTextSize = config.itemTextSize;
                if (itemTextSize > 0) {
                    name.setTextSize(TypedValue.COMPLEX_UNIT_SP,itemTextSize);
                }
                name.setTextColor(position == mDefaultSelectPos ? itemSelectColor:itemUnSelectColor);

                container.setBackground(position == mDefaultSelectPos?itemSelectBg:itemUnSelectBg);
            }

        }

        @Override
        public int getItemCount() {
            return data==null?0:data.size();
        }

        private class ItemsHolder extends RecyclerView.ViewHolder{
            private ViewGroup container;
            private TextView name;
            public ItemsHolder(@NonNull View itemView) {
                super(itemView);
                container = itemView.findViewById(R.id.container);
                name = itemView.findViewById(R.id.tv_name);

                itemView.setOnClickListener(v -> {
                    if (adapterClickListener != null) {
                        int adapterPosition = getBindingAdapterPosition();
                        adapterClickListener.onItemClick(v,adapterPosition);
                    }
                });
            }
        }
    }

    public interface OnAdapterClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnTabSelectListener{
        void onTabSelect(int position);
    }

}
