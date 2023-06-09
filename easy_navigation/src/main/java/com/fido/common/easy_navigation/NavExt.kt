package com.fido.common.easy_navigation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlin.reflect.KClass

/**
@author FiDo
@description:Navgation 扩展
@date :2023/4/14 17:47
 */
@PublishedApi
internal val Fragment.navController
    get() = findNavController()

/**
 * @param actionId xml中的actionId
 * @param destinationId 仅用作 launchMode == SINGLE_TASK 时传入的页面ID
 * nav_grap.xml <fragment> 标签 需要改为 fixFragment，否则解析不了
*/
fun Fragment.navigation(
    actionId: Int,
    argument: Bundle = bundleOf(),
    destinationId: Int = -1,
    optionsBuilder: NavOptions.() -> Unit = {}
) = navController.apply {
    navigate(
        actionId,
        argument,
        convertNavOptions(destinationId, NavOptions().apply(optionsBuilder))
    )
}

fun Fragment.navigateup() = navController.navigateUp()

fun Fragment.popBackStack() = navController.popBackStack()

fun Activity.navigateUp(viewId: Int) = findNavController(viewId).navigateUp()

/**
 * 当fragment栈 为空时 调用该方法可能报错
 */
fun Activity.popBackStack(viewId: Int) = findNavController(viewId).popBackStack()

class MyNavHost(
    internal val context:Context,navHost: NavHost
):NavHost by navHost

/**
 * ============================ Code Setting Navigation ======================
 * 推荐使用，代码简洁 缺点：不能可视化nav_graph
 */
fun NavHostFragment.loadRootFragment(
    startDestinationClazz: KClass<out Fragment>,
) {
    //获取导航器提供者
    val navigatorProvider = navController.navigatorProvider
    val fragmentNavigator = FixFragmentNavigator(context ?: requireContext(), childFragmentManager, id)
    //把自定义的Fragment导航器添加进去
    navigatorProvider.addNavigator(fragmentNavigator)
    //设置导航图
    val navGraph = NavGraph(NavGraphNavigator(navigatorProvider))
    val destination = fragmentNavigator.createDestination()
    destination.id = startDestinationClazz.hashCode()
    destination.className = startDestinationClazz.qualifiedName ?: ""
    destination.label = startDestinationClazz.simpleName
    navGraph.addDestination(destination)

    navGraph.startDestination = startDestinationClazz.hashCode()
    navController.graph = navGraph
}

/**
 * 存入Fragment到导航图
 */
@PublishedApi
internal fun NavController.putFragment(
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
            navigatorProvider[FixFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }).build()
        graph.plusAssign(destination)
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}

val Fragment.navigator
    get() = MyNavHost(requireContext()){
        val clazz = this::class
        requireParentFragment().findNavController().apply {
            putFragment(clazz)
        }
    }

/**
 * 跳转指定Fragment
 */
fun MyNavHost.start(
    clazz: KClass<out Fragment>,
    argument: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit
)  = with(navController){
    apply {
        val node = putFragment(clazz)  //因为其他地方也用到putFragment，这里抽出来做为方法
        navigate(
            node.id,
            argument,
            convertNavOptions(clazz.hashCode(), NavOptions().apply(optionsBuilder)),
            extras
        )
    }
}

inline fun <reified T : Fragment> MyNavHost.start(
    argument: Bundle? = null,
    extras: Navigator.Extras? = null,
    noinline optionsBuilder: NavOptions.() -> Unit
) {
    start(T::class, argument, extras, optionsBuilder)
}

/**
 * Fragment的栈返回
 */
fun MyNavHost.pop() {
    navController.popBackStack()
}

/**
 * 返回到指定栈
 */
fun MyNavHost.popTo(clazz: KClass<out Fragment>, include: Boolean = false) {
    navController.popBackStack(clazz.hashCode(), include)
}

/**
 * 弹出所有子Fragment 除了root Fragment
 * @param includeRoot 是否弹出root Fragment
 */
fun NavHostFragment.popAllFragment(includeRoot: Boolean = false){
    repeat(getAllFragments().size) {
        navController.popBackStack()
    }
    if (includeRoot) finishActivity()
}

val View.navigator
    get() = MyNavHost(context) { findNavController() }


//让Activity直接finish
fun Fragment.finishActivity() {
    requireActivity().finish()
}

