package com.fido.common.common_utils.animation;

/**
 * @author: HuTao
 * @date: 2025/6/19
 * @des:
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fido.common.R;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_CARDS = 0;
    private static final int ITEM_TYPE_SWITCH_MODE = 1;

    private List<ItemData> items;

    public MultiTypeViewPagerAdapter() {
        items = new ArrayList<>();
        items.add(new ItemData(ITEM_TYPE_CARDS));
        items.add(new ItemData(ITEM_TYPE_SWITCH_MODE));
    }

    public MultiTypeViewPagerAdapter(List<ItemData> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_TYPE_CARDS:
                View view1 = inflater.inflate(R.layout.ac_asm_test, parent, false);
                return new Type1ViewHolder(view1);
            case ITEM_TYPE_SWITCH_MODE:
                View view2 = inflater.inflate(R.layout.ac_asm_hook, parent, false);
                return new Type2ViewHolder(view2);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemData item = items.get(position);
        switch (holder.getItemViewType()) {
            case ITEM_TYPE_CARDS:
                // 这里进行 Type1ViewHolder 的数据设置
                ((Type1ViewHolder) holder).bind(item);
                break;
            case ITEM_TYPE_SWITCH_MODE:
                // 这里进行 Type2ViewHolder 的数据设置
                ((Type2ViewHolder) holder).bind(item);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class Type1ViewHolder extends RecyclerView.ViewHolder {
        public Type1ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bind(ItemData item) {

        }
    }

    public static class Type2ViewHolder extends RecyclerView.ViewHolder {
        public Type2ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bind(ItemData item) {

        }
    }

    public class ItemData {

        public ItemData(int viewType) {
            this.viewType = viewType;
        }

        private int viewType;

        public int getViewType() {
            return viewType;
        }

    }

}



