package com.fido.common.common_base_ui.base.listener;

import android.view.View;

public abstract class BlockQuickClickListener implements View.OnClickListener {
    private static final long MULTI_CLICK_INTERVAL = 800L;
    private long lastClickTime;

    public abstract void onBlockQuickClick(View v);

    @Override
    public void onClick(View v) {
        if (isFastClick()) return;
        onBlockQuickClick(v);
    }

    public boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime < MULTI_CLICK_INTERVAL) {
            return true;
        } else {
            lastClickTime = currentTime;
            return false;
        }
    }
}

