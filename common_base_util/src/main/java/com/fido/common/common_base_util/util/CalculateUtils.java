package com.fido.common.common_base_util.util;

/**
 * 计算的一些工具类
 */
public class CalculateUtils {

    /**
     * 根据开始颜色和结束颜色计算出百分比的当前颜色值
     * myScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
     *             if (imgHeight > 0) {
     *                 val scrollDistance = min(scrollY, imgHeight)
     *                 val fraction: Float = scrollDistance.toFloat() / imgHeight.toFloat()
     *                 YYLogUtils.w("滚动：scrollY：$scrollY imgHeight:$imgHeight  scrollDistance:$scrollDistance fraction:$fraction")
     *                 setTitleStatusBarAlpha(CalculateUtils.evaluate(fraction, startColor, endColor))
     *             }
     *         })
     *     }
     *     private fun setTitleStatusBarAlpha(color: Int) {
     *         easyTitleBar.setBackgroundColor(color)
     *         hostLayout.setStatusBarBackground(color)
     *     }
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
