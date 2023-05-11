/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fido.common.easy_navigation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Map;

@Navigator.Name("fixFragment")  //fix 5: 需要指定1个名字，源码里自带的名字有navigation、activity、fragment、dialog ===> nav_grap.xml 改为 fixFragment
public class FixFragmentNavigator extends FragmentNavigator {
    private static final String TAG = "FixFragmentNavigator";
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;

    public FixFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
    }

    public ArrayDeque<Integer> mBackStack;

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        if (mFragmentManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                    + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }
        //fix 1: 把类名作为tag，寻找已存在的Fragment
//        final Fragment frag = instantiateFragment(mContext, mFragmentManager, className, args);
//        frag.setArguments(args);
        //（如果想只针对个别fragment进行保活复用，可以在tag上做些标记比如加个前缀）
        Fragment frag = mFragmentManager.findFragmentByTag(className);
        if (null == frag) {
            //不存在，则创建
            frag = instantiateFragment(mContext, mFragmentManager, className, args);
        }

        frag.setArguments(args);
        final FragmentTransaction ft = mFragmentManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

//        ft.replace(mContainerId, frag);
        //fix 2: replace换成show和hide
        /*List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        if (!frag.isAdded()) {
            ft.add(mContainerId, frag, className);
        }
        ft.show(frag);
        ft.setPrimaryNavigationFragment(frag);*/
        // Add的方式替换replace方式，并处理生命周期
        if (mFragmentManager.getFragments().size() > 0) {
            Fragment lastFragment = mFragmentManager.getFragments().get(mFragmentManager.getFragments().size() - 1);
            ft.hide(lastFragment);
            ft.setMaxLifecycle(lastFragment, Lifecycle.State.STARTED);
            if (frag.isAdded()) {
                ft.show(frag);
            } else {
                ft.add(mContainerId, frag);
            }
        } else {
            ft.replace(mContainerId, frag);
        }
        ft.setPrimaryNavigationFragment(frag);

        final @IdRes int destId = destination.getId();
        //fix 3: mBackStack是私有的，而且没有暴露出来，只能反射获取
//        ArrayDeque<Integer> mBackStack;
        try {
            Field field = FragmentNavigator.class.getDeclaredField("mBackStack");
            field.setAccessible(true);
            mBackStack = (ArrayDeque<Integer>) field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        final boolean initialNavigation = mBackStack.isEmpty();
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            if (mBackStack.size() > 1) {
                mFragmentManager.popBackStack(
                        generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
            isAdded = true;
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    //fix 4: 从父类那边copy过来即可
    @NonNull
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }
}