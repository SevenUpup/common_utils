package com.fido.common.common_utils.naviga

import com.fido.common.base.BaseVBFragment
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.FragmentCodeNav2Binding
import com.fido.common.easy_navigation.LaunchMode
import com.fido.common.easy_navigation.applyFadeInOut
import com.fido.common.easy_navigation.navigator
import com.fido.common.easy_navigation.start

/**
@author FiDo
@description:
@date :2023/4/19 16:15
 */
class CodeNav2Fragment:BaseVBFragment<FragmentCodeNav2Binding>(R.layout.fragment_code_nav2) {
    override fun initView() {
        binding.mTv.click {
            navigator.start<CodeNavFragment> {
                launchMode = LaunchMode.SINGLE_TASK
                applyFadeInOut()
            }
        }
    }

    override fun initData() {

    }

}