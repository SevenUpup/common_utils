package com.fido.common.common_utils.motionlayout

import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.startActivity
import com.fido.common.R
import com.fido.common.databinding.AcMotionYoutubeBinding

/**
@author FiDo
@description:
@date :2023/6/12 11:50
 */
class MotionYouTubeAc :BaseVBActivity<AcMotionYoutubeBinding>(){
    override fun getLayoutId(): Int {
        return R.layout.ac_motion_youtube
    }

    override fun initView() {
//        binding.topImageContainer.setOnClickListener {
//        }
        binding.motion.block = {
            startActivity<MotionFakeBossListAc>()
        }
    }

    override fun initData() {
        val list = mutableListOf<String>()
        list += "0"
        list += "0"
        list += "0"
        list += "0"
        toast(list.joinToString("-"))
    }

    override fun initEvent() {
    }

}