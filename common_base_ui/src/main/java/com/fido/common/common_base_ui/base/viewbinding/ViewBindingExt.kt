package com.fido.common.common_base_ui.base.viewbinding

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
@author FiDo
@description: ViewBinding 快捷创建布局
@date :2023/5/18 17:39
 */

inline fun <reified VB:ViewBinding>ComponentActivity.binding(setContentView:Boolean = true) = lazy(LazyThreadSafetyMode.NONE) {
    inflateBinding<VB>(layoutInflater).also {binding->
        if(setContentView) setContentView(binding.root)
        if (binding is ViewDataBinding) binding.lifecycleOwner = this
    }
}

inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB

inline fun <reified VB : ViewBinding> inflateBinding(parent: ViewGroup) =
    inflateBinding<VB>(LayoutInflater.from(parent.context), parent, false)

inline fun <reified VB : ViewBinding> inflateBinding(
    layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        .invoke(null, layoutInflater, parent, attachToParent) as VB


// ==========================================================  这种也可以 ==================================================================

/**
 * 使用：private val binding: ActivityBinding by viewBinding(ActivityBinding::inflate)
 */
fun <T:ViewBinding>ComponentActivity.viewBinding(bindingInflater: (LayoutInflater)->T) = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    val binding = bindingInflater.invoke(layoutInflater)
    setContentView(binding.root)
    if (binding is ViewDataBinding) {
        binding.lifecycleOwner = this
    }
    binding
}

fun <T:ViewBinding> AppCompatActivity.viewBinding(inflater: (LayoutInflater)->T) = ViewBindingPropertyDelegate(this,inflater)

class ViewBindingPropertyDelegate<T:ViewBinding>(
    private val activity: AppCompatActivity,
    private val initializer:(LayoutInflater) -> T
) : ReadOnlyProperty<AppCompatActivity,T> , LifecycleObserver {

    private var _value:T?=null

    init {
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        if (_value == null) {
            _value = initializer.invoke(activity.layoutInflater)
        }
        activity.setContentView(_value?.root)
        activity.lifecycle.removeObserver(this)
        if (_value is ViewDataBinding){
            (_value as ViewDataBinding).lifecycleOwner = activity
        }
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        if (_value == null){
            // This must be on the main thread only
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw IllegalThreadStateException("This cannot be called from other threads. It should be on the main thread only.")
            }
            _value = initializer(thisRef.layoutInflater)
        }
        return _value!!
    }
}

// =================================== Fragment Binding ===========================================

/**
 * 使用：
 * class ProfileFragment : Fragment(R.layout.xxx) {
 *     private val binding by viewBinding(XxxBinding::bind)
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         // Use viewBinding
 *     }
 * }
 */
fun <T :ViewBinding> Fragment.viewBinding(
    viewBindingFactory:(View)->T
) = lazy {
    val viewLifecycleOwner = this.viewLifecycleOwner
    if (!viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
        throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
    }
    viewBindingFactory.invoke(requireView())
}

fun <T :ViewBinding> Fragment.viewbinding(viewBindingFactory: (View) -> T) = FragmentViewBindingDelegate(this,viewBindingFactory)

class FragmentViewBindingDelegate<T:ViewBinding>(
    private val fragment:Fragment,
    private val viewBindingFactory: (View) -> T
):ReadOnlyProperty<Fragment,T>{

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object :DefaultLifecycleObserver{

            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }
        return viewBindingFactory.invoke(thisRef.requireView()).also { this.binding = it }
    }

}
