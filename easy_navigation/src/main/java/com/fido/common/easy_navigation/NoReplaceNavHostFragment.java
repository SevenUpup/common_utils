package com.fido.common.easy_navigation;

import androidx.annotation.NonNull;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import java.lang.reflect.Method;

/**
 * @author FiDo
 * @description:
 * @date :2023/4/18 14:01
 */
public class NoReplaceNavHostFragment extends NavHostFragment {

    @NonNull
    @Override
    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
        int containerId = 0;
        try {
            Method getContainerId = NavHostFragment.class.getDeclaredMethod("getContainerId");
            getContainerId.setAccessible(true);
            Object mContainerId = getContainerId.invoke(this);
            if (mContainerId != null) {
                containerId = (int) mContainerId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FixFragmentNavigator(requireContext(), getChildFragmentManager(),
                containerId);
    }


}
