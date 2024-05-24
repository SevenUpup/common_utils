package com.fido.common.common_utils.motionlayout

import androidx.recyclerview.widget.RecyclerView
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.ext.bindData
import com.fido.common.common_base_ui.ext.canScrollDown
import com.fido.common.common_base_ui.ext.vertical
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.toast
import com.fido.common.R
import com.fido.common.databinding.AcMotionFakeBossListBinding

/**
@author FiDo
@description:
@date :2023/6/12 16:38
 */
class MotionFakeBossListAc:BaseVBActivity<AcMotionFakeBossListBinding>() {

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun getLayoutId(): Int {
        return R.layout.ac_motion_fake_boss_list
    }

    override fun initView() {
        binding.iv.click {
            toast("click bg")
        }

        val list  = mutableListOf<String>()
        for (i in (0 until 20)) {
            list.add("test${i}")
        }
        binding.mRv.vertical()
            .bindData(
                list,
                R.layout.item_rv_text
            ) { holder, position, item ->
                holder.setText(R.id.mTitle, item)
            }


        binding.mRv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                Log.e("FiDo", "onScrolled dy: $dy  binding.mRv.canScrollDown=${binding.mRv.canScrollDown}")
                if( dy > 0 ){
                    if( binding.motion.currentState == binding.motion.startState){
                        binding.motion.transitionToEnd()
                    }
                }else{
                    if (binding.mRv.canScrollDown) return
                    if( binding.motion.currentState == binding.motion.endState) {
                        binding.motion.transitionToStart()
                    }
                }
            }
        })

    }
    
    override fun initData() {
    }

    override fun initEvent() {
    }

}