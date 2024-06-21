package com.fido.common.common_base_util.ext

import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fido.common.common_base_util.toBundle
import java.lang.reflect.ParameterizedType

/**
 * Fragment相关扩展
 */

/**
 * fragment批处理，自动commit
 */
fun FragmentActivity.fragmentManager(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction()
        .apply { action() }
        .commitAllowingStateLoss()
}

//自动替换fragment
fun FragmentActivity.replaceFragment(
    layoutId: Int,
    f: Fragment,
    bundle: Array<out Pair<String, Any?>>? = null
) {
    if (bundle != null) f.arguments = bundle.toBundle()
    supportFragmentManager.beginTransaction()
        .replace(layoutId, f)
        .commitAllowingStateLoss()
}

//添加fragment
fun FragmentActivity.addFragment(
    containerId: Int,
    f: Fragment,
    bundle: Array<out Pair<String, Any?>>? = null
) {
    if (bundle != null) f.arguments = bundle.toBundle()
    supportFragmentManager.beginTransaction()
        .add(containerId, f)
        .commitAllowingStateLoss()
}

//隐藏
fun FragmentActivity.hideFragment(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .hide(f)
        .commitAllowingStateLoss()
}

//展示
fun FragmentActivity.showFragment(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .show(f)
        .commitAllowingStateLoss()
}

//移除
fun FragmentActivity.removeFragment(f: Fragment) {
    supportFragmentManager.beginTransaction()
        .remove(f)
        .commitAllowingStateLoss()
}

//替换
fun Fragment.replaceFragment(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>? = null) {
    if (bundle != null) f.arguments = bundle.toBundle()
    childFragmentManager.beginTransaction()
        .replace(layoutId, f)
        .commitAllowingStateLoss()
}

fun Fragment.addFragment(layoutId: Int, f: Fragment, bundle: Array<out Pair<String, Any?>>? = null) {
    if (bundle != null) f.arguments = bundle.toBundle()
    childFragmentManager.beginTransaction()
        .add(layoutId, f)
        .commitAllowingStateLoss()
}

fun Fragment.hideFragment(f: Fragment) {
    childFragmentManager.beginTransaction()
        .hide(f)
        .commitAllowingStateLoss()
}

fun Fragment.showFragment(f: Fragment) {
    childFragmentManager.beginTransaction()
        .show(f)
        .commitAllowingStateLoss()
}

fun Fragment.removeFragment(f: Fragment) {
    childFragmentManager.beginTransaction()
        .remove(f)
        .commitAllowingStateLoss()
}

fun <T : Fragment> T.withArguments(vararg pairs: Pair<String, *>) = apply {
    arguments = bundleOf(*pairs)
}

fun <T> Fragment.arguments(key: String) = lazy<T?> {
    arguments?.get(key) as T
}

fun <T> Fragment.arguments(key: String, default: T) = lazy {
    arguments?.get(key) ?: default
}

fun <T> Fragment.safeArguments(name: String) = lazy<T> {
    checkNotNull(arguments?.get(name) as T) { "No intent value for key \"$name\"" }
}

fun Fragment.pressBackTwiceToExitApp(toastText: String, delayMillis: Long = 2000) =
    requireActivity().pressBackTwiceToExitApp(toastText, delayMillis, viewLifecycleOwner)

fun Fragment.pressBackTwiceToExitApp(@StringRes toastText: Int, delayMillis: Long = 2000) =
    requireActivity().pressBackTwiceToExitApp(toastText, delayMillis, viewLifecycleOwner)

fun Fragment.pressBackTwiceToExitApp(delayMillis: Long = 2000, onFirstBackPressed: () -> Unit) =
    requireActivity().pressBackTwiceToExitApp(delayMillis, viewLifecycleOwner, onFirstBackPressed)

fun Fragment.pressBackToNotExitApp() =
    requireActivity().pressBackToNotExitApp(viewLifecycleOwner)

fun Fragment.doOnBackPressed(onBackPressed: () -> Unit) =
    requireActivity().doOnBackPressed(viewLifecycleOwner, onBackPressed)

//view model
fun <T : ViewModel> Fragment.getVM(clazz: Class<T>) = ViewModelProvider(this).get(clazz)

fun <T : ViewModel> Fragment.getActivityVM(clazz: Class<T>) = ViewModelProvider(requireActivity()).get(clazz)


//获取泛型的实例
fun <VM> getVMCls(cls: Any): VM {
    return (cls.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}