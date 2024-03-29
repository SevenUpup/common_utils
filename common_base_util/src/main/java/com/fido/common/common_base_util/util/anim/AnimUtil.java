package com.fido.common.common_base_util.util.anim;

import static android.animation.ValueAnimator.INFINITE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.fido.common.common_base_util.BaseUtilsKt;
import com.fido.common.common_base_util.R;
import com.fido.common.common_base_util.util.toast.ThreadUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class AnimUtil {
    public static final long DEFAULT_TIME = 300;

    public static final float SWITCH_SCENE_BG_ANIM_START_ALPHA = 0f;
    public static final long SWITCH_SCENE_ANIM_DURA = 500;

    public static void animIn(View view) {
        animIn(view, null);
    }

    public static void animIn(View view, @Nullable final Runnable endAction) {
        animIn(endAction, view);
    }

    public static void animIn(@Nullable final Runnable endAction, View... vs) {
        if (vs == null || vs.length == 0) {
            return;
        }

        Animator.AnimatorListener listener = endAction == null ? null : new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                endAction.run();
            }
        };

        boolean listenerAttached = false;
        for (View view : vs) {
            if (view == null) {
                continue;
            }
            if (view.getAlpha() != SWITCH_SCENE_BG_ANIM_START_ALPHA) {
                view.setAlpha(SWITCH_SCENE_BG_ANIM_START_ALPHA);
            }
            view.animate().alpha(1).setDuration(SWITCH_SCENE_ANIM_DURA).setListener(!listenerAttached ? listener : null).start();
            setViewVisible(view,true);
            listenerAttached = true;
        }
        if (!listenerAttached) {
            run(endAction);
        }
    }

    public static void animOut(View view) {
        animOut(view, null);
    }

    public static void animOut(View view, final @Nullable Runnable endAction) {
        if (view == null) {
            return;
        }
        animOut(endAction, view);
    }

    public static void animOut(final @Nullable Runnable endAction, View... vs) {
        if (vs == null || vs.length == 0) {
            return;
        }

        Animator.AnimatorListener listener = endAction == null ? null : new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                endAction.run();
            }
        };

        boolean listenerAttached = false;
        for (View view : vs) {
            if (view == null) {
                continue;
            }
            view.animate().alpha(0).setDuration(SWITCH_SCENE_ANIM_DURA).setListener(!listenerAttached ? listener : null).start();
            listenerAttached = true;
        }
        if (!listenerAttached) {
            run(endAction);
        }
    }

    public static void animBottomUp(@Nullable View view) {
        animBottomUp(view, null);
    }

    public static void animBottomUp(@Nullable View view, final Runnable endAction) {
        animBottomUp(endAction, view);
    }

    public static void animBottomUp(@Nullable final Runnable endAction, @Nullable View... views) {
        if (views == null || views.length == 0) {
            return;
        }
        Context appContext = BaseUtilsKt.getApp();

        Animation.AnimationListener listener = endAction == null ? null : new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                endAction.run();
            }
        };
        boolean listenerAttached = false;
        for (View view : views) {
            if (view == null) {
                continue;
            }
            Animation anim = AnimationUtils.loadAnimation(appContext, R.anim.anim_bottom_up);
            view.startAnimation(anim);
            anim.setAnimationListener(!listenerAttached ? listener : null);
            setViewVisible(view,true);
            listenerAttached = true;
        }
        if (!listenerAttached) {
            run(endAction);
        }
    }

    public static void animBottomDown(@Nullable View view) {
        animBottomDown(view, null);
    }

    public static void animBottomDown(@Nullable View view, final Runnable endAction) {
        animBottomDown(endAction, view);
    }

    public static void animBottomDown(@Nullable final Runnable endAction, @Nullable View... views) {
        if (views == null || views.length == 0) {
            return;
        }

        Animation.AnimationListener listener = endAction == null ? null : new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                endAction.run();
            }
        };

        boolean listenerAttached = false;
        for (View view : views) {
            if (view == null) {
                continue;
            }

            Context appContext = BaseUtilsKt.getApp();
            Animation anim = AnimationUtils.loadAnimation(appContext, R.anim.anim_bottom_down);
            view.startAnimation(anim);
            anim.setAnimationListener(!listenerAttached ? listener : null);
            setViewVisible(view,false);
            listenerAttached = true;
        }
        if (!listenerAttached) {
            run(endAction);
        }
    }

    public static void slidDown(@Nullable View view) {
        slidDown(view, null);
    }

    public static void slidDown(@Nullable View view, final Runnable endAction) {
        slidDown(endAction, view);
    }

    public static void slidDown(@Nullable final Runnable endAction, View... vs) {
        if (vs == null || vs.length == 0) {
            return;
        }

        int screenHeight = BaseUtilsKt.getScreenHeightPx(BaseUtilsKt.getApp());
        boolean listenerAttached = false;
        for (View view : vs) {
            if (view == null) {
                continue;
            }
            view.animate().translationY(screenHeight).setDuration(SWITCH_SCENE_ANIM_DURA).withEndAction(!listenerAttached ? endAction : null).start();
            listenerAttached = true;
        }
        if (!listenerAttached) {
            run(endAction);
        }
    }

    public static void expandOrFoldViewViaHeightAnimation(@Nullable final View toExpandView, final int toHeight,
                                                          @Nullable final ValueAnimator.AnimatorUpdateListener updateListener,
                                                          @Nullable final SimpleAnimatorListener animatorListener) {
        if (toExpandView == null) {
            return;
        }
        final int fromHeight = toExpandView.getMeasuredHeight();
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams lp = toExpandView.getLayoutParams();
            lp.height = val;
            toExpandView.setLayoutParams(lp);
            if (updateListener != null) {
                updateListener.onAnimationUpdate(valueAnimator);
            }
        });
        if (animatorListener != null) {
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animatorListener.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animatorListener.onAnimationEnd(animation);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animatorListener.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    animatorListener.onAnimationRepeat(animation);
                }
            });
        }
        anim.setDuration(DEFAULT_TIME);
        anim.start();
    }

    public static void fadeIn(View view) {
        fadeIn(view, DEFAULT_TIME);
    }

    public static void fadeIn(View view, long time) {
        if (view == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f));
        set.setDuration(time);
        set.start();
    }

    public static void fadeIn(View view, long time, final AnimEndListener listener) {
        if (view == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
    }


    public static void fadeIn(View view, final AnimEndListener listener) {
        if (view == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f));
        set.setDuration(DEFAULT_TIME);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
    }

    public static void fadeOut(View view, final AnimEndListener listener) {
        if (view == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f));
        set.setDuration(DEFAULT_TIME);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
    }

    public static void fadeOut(View view, long time, final AnimEndListener listener) {
        if (view == null) {
            return;
        }
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
    }

    public static void animBackgroundColor(View view, int from, int to, long time) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", from, to);
        colorAnim.setDuration(time);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }

    public static AnimatorSet translationY(View view, float from, float to, AnimEndListener listener) {
        return translationY(view, from, to, DEFAULT_TIME, listener);
    }

    public static AnimatorSet scale(View view, float from, float to, long animDuration, AnimEndListener listener) {
        AnimatorSet set = genScale(view, from, to, animDuration, listener);
        set.start();
        return set;
    }

    public static AnimatorSet genScale(View view, float from, float to, long animDuration, final AnimEndListener listener) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "scaleX", from, to));
        set.play(ObjectAnimator.ofFloat(view, "scaleY", from, to));
        set.setDuration(animDuration);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        return set;
    }

    public static ObjectAnimator rotateView(final View view, int duration) {
        /*view.setPivotX(view.getMeasuredHeight() / 2);
        view.setPivotY(view.getMeasuredWidth() / 2);*/
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        anim.setRepeatCount(INFINITE);
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
        return anim;
    }

    public static AnimatorSet rotate(View view, float fromDegree, float toDegree, final AnimEndListener listener) {
        AnimatorSet set = new AnimatorSet();
        view.setPivotX(view.getMeasuredHeight() / 2);
        view.setPivotY(view.getMeasuredWidth() / 2);
        set.play(ObjectAnimator.ofFloat(view, "rotation", fromDegree, toDegree));
        set.setDuration(DEFAULT_TIME);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }


    public static AnimatorSet scale(View view, float from, float to, AnimEndListener listener) {
        return scale(view, from, to, DEFAULT_TIME, listener);
    }


    public static AnimatorSet translationY(View view, float from, float to, long time, final AnimEndListener listener) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "translationY", from, to));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }

    public static AnimatorSet translationYWithAlpha(View view, float from, float to, boolean isShow, long time, final AnimEndListener listener) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "translationY", from, to))
                .with(ObjectAnimator.ofFloat(view, "alpha", isShow ? 0f : 1f, isShow ? 1f : 0f));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }


    public static AnimatorSet translationX(View view, float from, float to, long time, final AnimEndListener listener) {
        if (view == null) return null;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "translationX", from, to));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }

    public static AnimatorSet translationXWithAlpha(View view, float from, float to, boolean isShow, Long time, final AnimEndListener listener) {
        if (view == null) return null;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "translationX", from, to))
                .with(ObjectAnimator.ofFloat(view, "alpha", isShow ? 0f : 1f, isShow ? 1f : 0f));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }

    public static AnimatorSet translationXWithAlpha(View view, float from, float to, float fromAlpha, float toAlpha, Long time, final AnimEndListener listener) {
        if (view == null) return null;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(view, "translationX", from, to))
                .with(ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha));
        set.setDuration(time);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }

    public static AnimatorSet translationXEndAlpha(View view, float from, float to,float viewWidth, Long trainsTime,Long trains2Time, final AnimEndListener listener) {
        if (view == null) return null;
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", from, to);
//        translationX.setInterpolator(new DecelerateInterpolator());  //减速
        translationX.setInterpolator(new AccelerateInterpolator());  //加速
        translationX.setDuration(trainsTime);

        ObjectAnimator translationX2 = ObjectAnimator.ofFloat(view, "translationX", to,to - viewWidth);
//        translationX2.setInterpolator(new AnticipateInterpolator());
        translationX2.setInterpolator(new AccelerateInterpolator());
        translationX2.setDuration(trains2Time);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f);
        alpha.setDuration(trains2Time);
//        set.play(translationX)
//                .before(translationX2);
        set.playSequentially(translationX,alpha,translationX2);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.animEnd();
                }
            }
        });
        set.start();
        return set;
    }

/*
    public static void lottieForAssetName(final LottieAnimationView view, String assetName, final boolean isLooper, final boolean isHide, final AnimListener listener) {
        if (view == null) {
            return;
        }
        view.setAnimation(assetName);
        view.loop(isLooper);
        view.setVisibility(View.VISIBLE);
        view.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (listener != null) {
                    listener.animUpdate(valueAnimator);
                }
            }
        });
        view.addAnimatorListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
                if (listener != null) {
                    listener.animStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(isHide ? View.GONE : View.VISIBLE);
                if (listener != null) {
                    listener.animEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        view.playAnimation();
    }
*/

    public interface AnimEndListener {
        void animEnd();
    }

    public abstract static class AnimListener {
        public void animEnd() {
        }

        public void animUpdate(ValueAnimator valueAnimator) {

        }

        public void animStart() {
        }
    }

    private static void run(Runnable task) {
        if (task != null) {
            task.run();
        }
    }

    private static void setViewVisible(View view,boolean isVisible) {
        ThreadUtils.INSTANCE.runMain(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                view.setVisibility(isVisible?View.VISIBLE:View.GONE);
                return null;
            }
        });
    }

    public static class SimpleAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    public static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}