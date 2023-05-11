package com.fido.common.common_utils.naviga

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.fido.common.base.BaseVBFragment
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.FragmentCodeNavBinding
import com.fido.common.easy_navigation.*

/**
@author FiDo
@description:
@date :2023/4/19 10:56
 */
class CodeNavFragment:BaseVBFragment<FragmentCodeNavBinding>(R.layout.fragment_code_nav) {

    private val str by lazy {
        val str = arguments?.getString("S","0")
        binding.mTv.text= "Nav ${str?:"0"}"
        str
    }

    override fun initView() {
        binding.mTv2.isVisible = str?.toInt() == 4
        binding.mTv.click {
            navigator.start(clazz = CodeNavFragment::class,argument = bundleOf("S" to ((str?.toIntOrNull()?:0).plus(1).toString()))){
                //设置为 SINGLE_TOP 会一直复用 CodeNavFragment 因为 它是栈顶复用模式，所以一直在复用
//                launchMode = LaunchMode.SINGLE_TOP
                applySlideInOut()
            }
        }
        binding.mTv2.click {
            // SINGLE_TASK 栈内复用且会移除 在其栈顶上的所有Fragment
            navigator.start<CodeNav2Fragment> {
                launchMode = LaunchMode.SINGLE_TASK
                applyFadeInOut()
            }
        }
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        val allFragment = (requireActivity() as? CodenavigationAc)?.getAllFragment()
        if ((str?.toIntOrNull() ?: -1) % 5 == 0) {
            (requireActivity() as? CodenavigationAc)?.popAll()
        }
    }

}