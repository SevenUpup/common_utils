package com.fido.common.common_utils.customview.ninegrid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fido.common.R;
import com.fido.common.common_base_ui.widget.ninegrid.NineGridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: FiDo
 * @date: 2024/6/13
 * @des:
 */
public class ImageNineGridAdapter extends NineGridLayout.Adapter{

    private List<String> mDatas = new ArrayList<>();

    public ImageNineGridAdapter(List<String> data) {
        mDatas.addAll(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return 10;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public View onCreateItemView(Context context, ViewGroup parent, int itemType) {
        if (itemType == 0) {
            return LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        } else {
            return LayoutInflater.from(context).inflate(R.layout.item_rv_img, parent, false);
        }

    }

    @Override
    public void onBindItemView(View itemView, int itemType, int position) {

        if (itemType == 0) {
            itemView.findViewById(R.id.container).setBackgroundColor(position == 0 ? Color.RED : Color.YELLOW);
        }
    }

}
