package com.fido.common.easy_navigation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

/**
@author FiDo
@description:
@date :2023/4/14 18:18
 */

class NavOptions {

    var launchMode: LaunchMode = LaunchMode.STANDARD

    @AnimRes
    @AnimatorRes
    var enterAnim = -1

    @AnimRes
    @AnimatorRes
    var exitAnim = -1

    @AnimRes
    @AnimatorRes
    var popEnterAnim = -1

    @AnimRes
    @AnimatorRes
    var popExitAnim = -1
}

/**
 * <fragment
android:id="@+id/nav2Fragment"
 * @param destinationId :跳转的页面ID 及 nav2Fragment
 *
 */
@PublishedApi
internal fun convertNavOptions(
    destinationId:Int = -1,
    navOptions: NavOptions
): androidx.navigation.NavOptions {
    return androidx.navigation.NavOptions.Builder().apply {
        setEnterAnim(navOptions.enterAnim)
        setExitAnim(navOptions.exitAnim)
        setPopEnterAnim(navOptions.popEnterAnim)
        setPopExitAnim(navOptions.popExitAnim)
        //Single Top 调用setLaunchSingleTop Api实现
        setLaunchSingleTop(navOptions.launchMode == LaunchMode.SINGLE_TOP)
        //Single Task 调用setPopUpTo来实现
        if (navOptions.launchMode == LaunchMode.SINGLE_TASK && destinationId > 0) {
            setPopUpTo(destinationId, true)
        }
    }.build()
}


enum class LaunchMode {
    STANDARD, SINGLE_TOP, SINGLE_TASK
}

//默认的左右入场出场动画
fun NavOptions.applySlideInOut() {
    enterAnim = R.anim.open_enter
    exitAnim = R.anim.open_exit
    popEnterAnim = R.anim.close_enter
    popExitAnim = R.anim.close_exit
}

//默认的上下透明动画
fun NavOptions.applyFadeInOut() {
    enterAnim = androidx.navigation.ui.R.anim.nav_default_enter_anim
    exitAnim = androidx.navigation.ui.R.anim.nav_default_exit_anim
    popEnterAnim = androidx.navigation.ui.R.anim.nav_default_enter_anim
    popExitAnim = androidx.navigation.ui.R.anim.nav_default_exit_anim
}