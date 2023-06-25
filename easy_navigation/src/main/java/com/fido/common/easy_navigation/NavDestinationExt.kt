package com.fido.common.easy_navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import java.util.*

/**
@author FiDo
@description:
@date :2023/4/19 10:18
 */

//internal fun NavDestination.removeFromParent() {
//    this.parent = null
//}

fun NavHostFragment.showLog() {

    val res = StringBuilder("NavGraph")

    navController.myBackStack.forEachIndexed { index, entry ->
        entry.destination.let { des ->
            if (des is FragmentNavigator.Destination) {
                val frag = childFragmentManager.fragments[index - 1]
                res.append("\n +- ${des.className}\n   [tag]${frag.tag}  [hash]${frag.hashCode()}")
            }
        }
    }
    Log.e("NavDestination", "res:${res.toString()}")
}

val NavController.myBackStack: Deque<NavBackStackEntry>
    @SuppressLint("RestrictedApi")
    get() {
        return backStack
    }
//    get() {
//        try {
//            val stackField = NavController::class.java.getDeclaredField("mBackStack")
//            stackField.isAccessible = true
//            return stackField.get(this) as ArrayDeque<NavBackStackEntry>
//        }catch (e:Exception){
//            e.printStackTrace()
//        }
//        return ArrayDeque()
//    }

//获取当前的Fragment
fun NavHostFragment.getCurFragment(): Fragment? {
    if (navController.myBackStack.isNullOrEmpty() || navController.myBackStack.size < 2) return null
    val entry: NavBackStackEntry = navController.myBackStack.last()
    val des = entry.destination

    return if (des is FragmentNavigator.Destination) {
        val frag = childFragmentManager.fragments[navController.myBackStack.size - 2]
        frag
    } else {
        null
    }
}

//获取全部的Fragment
fun NavHostFragment.getAllFragments(): List<Fragment> {
    if (navController.myBackStack.isNullOrEmpty() || navController.myBackStack.size < 1) return emptyList()
    val list = arrayListOf<Fragment>()
    navController.myBackStack.forEachIndexed { index, entry ->
        entry.destination.let { des ->
            if (des is FragmentNavigator.Destination) {
                val frag = childFragmentManager.fragments[index - 1]
                list.add(frag)
            }
        }
    }
    return list
}

//根据Activity获取到Fragments
fun FragmentActivity.getAllNavFragments(navHostRes: Int): List<Fragment> {
    val navHostFragment = supportFragmentManager.findFragmentById(navHostRes) as NavHostFragment
    return navHostFragment.getAllFragments()
}
