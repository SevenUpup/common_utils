package com.fido.common.common_base_util.util.keyboard;

import static android.view.WindowInsetsAnimation.Callback.DISPATCH_MODE_STOP;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.List;

public final class KeyboardHeightUtils {

    public static int sDecorViewInvisibleHeightPre;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private static int mNavHeight;

    private KeyboardHeightUtils() {
    }

    private static int sDecorViewDelta = 0;

    private static int getDecorViewInvisibleHeight(final Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        if (decorView == null) return sDecorViewInvisibleHeightPre;
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);

        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= mNavHeight) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    public static void registerKeyboardHeightListener(final Activity activity, final KeyboardHeightListener listener) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            invokeAbove31(activity, listener);
        } else {
            invokeBelow31(activity, listener);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private static void invokeAbove31(Activity activity, KeyboardHeightListener listener) {

        activity.getWindow().getDecorView().setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            @NonNull
            @Override
            public WindowInsets onProgress(@NonNull WindowInsets windowInsets, @NonNull List<WindowInsetsAnimation> list) {

                int imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom;
                int navHeight = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
                boolean hasNavigationBar = windowInsets.isVisible(WindowInsets.Type.navigationBars()) &&
                        windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom > 0;

                listener.onKeyboardHeightChanged(hasNavigationBar ? Math.max(imeHeight - navHeight, 0) : imeHeight);

                return windowInsets;
            }
        });
    }

    private static void invokeBelow31(Activity activity, KeyboardHeightListener listener) {

        final int flags = activity.getWindow().getAttributes().flags;

        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        final FrameLayout contentView = activity.findViewById(android.R.id.content);
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity);

        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getDecorViewInvisibleHeight(activity);
                if (sDecorViewInvisibleHeightPre != height) {

                    listener.onKeyboardHeightChanged(height);

                    sDecorViewInvisibleHeightPre = height;
                }
            }
        };

        //获取到导航栏高度之后再添加布局监听
        getNavigationBarHeight(activity, (height, hasNav) -> {
            mNavHeight = height;
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        });

    }

    public static void unregisterKeyboardHeightListener(Activity activity) {
        try {
            onGlobalLayoutListener = null;
            View contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            if (contentView == null) return;
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }catch (Exception ignored){}
    }

    private static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static void getNavigationBarHeight(Activity activity, NavigationBarCallback callback) {

        View view = activity.getWindow().getDecorView();
        boolean attachedToWindow = view.isAttachedToWindow();

        if (attachedToWindow) {

            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(view);
            assert windowInsets != null;

            int height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

            boolean hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                    windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0;

            if (height > 0) {
                callback.onHeight(height, hasNavigationBar);
            } else {
                callback.onHeight(getNavBarHeight(), hasNavigationBar);
            }

        } else {

            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                    WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(v);
                    assert windowInsets != null;
                    int height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

                    boolean hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                            windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0;

                    if (height > 0) {
                        callback.onHeight(height, hasNavigationBar);
                    } else {
                        callback.onHeight(getNavBarHeight(), hasNavigationBar);
                    }

                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }

    public interface KeyboardHeightListener {
        void onKeyboardHeightChanged(int height);
    }
}