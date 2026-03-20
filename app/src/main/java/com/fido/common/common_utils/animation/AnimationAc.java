package com.fido.common.common_utils.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.fido.common.R;
import com.fido.common.common_base_util.ext.ToastExtKt;
import com.fido.common.common_utils.widgets.CircleImageView;

/**
 * @author: HuTao
 * @date: 2025/6/18
 * @des:
 */
public class AnimationAc extends AppCompatActivity {

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_animation);

        initView();

    }

    private void initView() {
        viewPager2 = findViewById(R.id.mVp2);
        findViewById(R.id.bt_switch).setOnClickListener(v -> {
            int visibility = viewPager2.getVisibility();
            viewPager2.setVisibility(visibility == View.GONE?View.VISIBLE:View.GONE);
        });


        Button bt1 = findViewById(R.id.bt1);
        Button bt2 = findViewById(R.id.bt2);
        final View originalContainer = findViewById(R.id.originalContainer);
        final View rightContainer = findViewById(R.id.rightContainer);

        initVP2(bt1,bt2);

        originalContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastExtKt.toast("click original");
            }
        });

        rightContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastExtKt.toast("click right");
            }
        });

        // 初始设置使 originalContainer 显示
        originalContainer.setVisibility(View.VISIBLE);
        rightContainer.setVisibility(View.GONE);


        // 仪表按钮监听器 - originalContainer 从右进入
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightContainer.getVisibility() == View.VISIBLE) {
                    //animation time can't click
                    bt1.setClickable(false);
                    bt2.setClickable(false);

                    ObjectAnimator outAnimator = ObjectAnimator.ofFloat(rightContainer, View.TRANSLATION_X, 0, rightContainer.getWidth());
                    ObjectAnimator outAlpha = ObjectAnimator.ofFloat(rightContainer, View.ALPHA, 1, 0.8f);

                    originalContainer.setTranslationX(originalContainer.getWidth());
                    originalContainer.setVisibility(View.VISIBLE);
                    ObjectAnimator inAnimator = ObjectAnimator.ofFloat(originalContainer, View.TRANSLATION_X, -originalContainer.getWidth(), 0);
                    ObjectAnimator inAlpha = ObjectAnimator.ofFloat(originalContainer, View.ALPHA, 0.8f, 1);

                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(outAnimator, outAlpha, inAnimator, inAlpha);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.setDuration(300);
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rightContainer.setVisibility(View.GONE);
                            bt1.setClickable(true);
                            bt2.setClickable(true);
                        }
                    });
                    set.start();
                }
            }
        });

        // 模式按钮监听器 - originalContainer 从左移出, rightContainer 从右进入
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (originalContainer.getVisibility() == View.VISIBLE) {
                    bt1.setClickable(false);
                    bt2.setClickable(false);

                    ObjectAnimator outAnimator = ObjectAnimator.ofFloat(originalContainer, View.TRANSLATION_X, 0, -originalContainer.getWidth());
                    ObjectAnimator outAlpha = ObjectAnimator.ofFloat(originalContainer, View.ALPHA, 1, 0.8f);

                    rightContainer.setTranslationX(rightContainer.getWidth());
                    rightContainer.setVisibility(View.VISIBLE);
                    ObjectAnimator inAnimator = ObjectAnimator.ofFloat(rightContainer, View.TRANSLATION_X, rightContainer.getWidth(), 0);
                    ObjectAnimator inAlpha = ObjectAnimator.ofFloat(rightContainer, View.ALPHA, 0.8f, 1);

                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(outAnimator, outAlpha, inAnimator, inAlpha);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.setDuration(300);
                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            bt1.setClickable(true);
                            bt2.setClickable(true);
                            originalContainer.setVisibility(View.GONE);
                        }
                    });
                    set.start();
                }
            }
        });

    }

    private void initVP2(TextView tv1,TextView tv2) {
        MultiTypeViewPagerAdapter pagerAdapter = new MultiTypeViewPagerAdapter();
        viewPager2.setAdapter(pagerAdapter);
        initVp2Views(tv1,tv2);
    }

    private void initVp2Views(TextView tv1, TextView tv2){
        View view = viewPager2.getChildAt(0);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("FiDo", "onPageSelected: position="+position);
                if (view instanceof RecyclerView) {
                    RecyclerView.LayoutManager layoutManager = ((RecyclerView) view).getLayoutManager();
                    if (layoutManager != null) {
                        View firstRootView = layoutManager.findViewByPosition(0);
                        View sencondRootView = layoutManager.findViewByPosition(1);

                        if (firstRootView != null) {
                            Log.d("FiDo", "initVp2Views firstRootView: "+firstRootView);
                            CircleImageView iv = firstRootView.findViewById(R.id.iv_circle);
                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iv.setImageResource(R.drawable.ic_zelda);
                                    viewPager2.setCurrentItem(1);
                                }
                            });
                        }
                        if (sencondRootView != null) {
                            Log.d("FiDo", "initVp2Views sencondRootView: "+sencondRootView);
                            Button firstView = sencondRootView.findViewById(R.id.bt_showPop);
                            firstView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    firstView.setText("6666");
                                    ToastExtKt.toast("6666");
                                }
                            });
                        }
                    }
                }
            }
        });

    }

}
