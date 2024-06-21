package com.fido.common.common_utils.jetpack

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.fido.common.R
import com.fido.common.base.BaseVBFragment
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.addFragment
import com.fido.common.databinding.AcJetpackageContainerBinding

/**
 * @author: FiDo
 * @date: 2024/6/17
 * @des:
 */
class JetPackageContainerAc : AppCompatActivity() {

    private val binding: AcJetpackageContainerBinding by binding()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.getIntExtra("index", 0)

        fillFragment(JetPackAc.fragmentList[index])
    }

    private fun fillFragment(fragment: Fragment) {
        binding.apply {
            addFragment(R.id.container, fragment)
        }
    }

}


class EasyFragment(private val layoutRes: Int, private val init: ((View) -> Unit)? = null) :
    Fragment(layoutRes) {

    companion object {
        fun getInstance(
            layoutRes: Int,
            init: ((View) -> Unit)? = null
        ): EasyFragment {
            return EasyFragment(layoutRes, init = init)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init?.invoke(view)
    }

}

class EasyBindingFragment<VB : ViewDataBinding>(
    private val layoutRes:Int,
    private val init: (VB.() -> Unit)? = null
) : BaseVBFragment<VB>(layoutRes) {

    companion object{
        fun <VB:ViewDataBinding> getInstance(layoutRes: Int,init: (VB.() -> Unit)? = null):EasyBindingFragment<VB>{
            return EasyBindingFragment(layoutRes,init)
        }
    }

    override fun initView() {
        init?.invoke(binding)
    }

    override fun initData() {

    }

}