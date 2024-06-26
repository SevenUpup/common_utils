package com.fido.common.common_utils.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.fido.common.base.BaseVBFragment
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.R
import com.fido.common.databinding.FragmentNav3Binding
import com.fido.common.easy_navigation.LaunchMode
import com.fido.common.easy_navigation.applySlideInOut
import com.fido.common.easy_navigation.navigation


/**
@author FiDo
@description:
@date :2023/4/17 9:57
 */
class Nav3Fragment:BaseVBFragment<FragmentNav3Binding>(R.layout.fragment_nav3) {

    companion object{
        fun getInstance(str:String):Nav3Fragment{
            val f = Nav3Fragment()
            val bundle = Bundle()
            bundle.putString("S",str)
            f.arguments = bundle
            return f
        }

    }

    var s = ""
    override fun initView() {
//        s = arguments?.getString("S","0")?:""
//        binding.btTop.text = "Go Nav ${s}"
        binding.btTop.isAllCaps = false
//        binding.mRv.vertical()
//            .bindData(listOf("1","2","3"),R.layout.item_rv_text){holder, position, item ->
//                holder.setText(R.id.mTitle,"------$item")
//            }
    }

    override fun initData() {
        binding.btTop.click {
            navigation(R.id.action_nav2fragment_tp_Material){
                launchMode = LaunchMode.SINGLE_TOP
                applySlideInOut()
            }
        }
        binding.btNav1.click {
            navigation(R.id.action_nav3_to_nav1Fragment, destinationId = R.id.nav1Fragment, argument = bundleOf("S" to "Single Task")){
                launchMode = LaunchMode.SINGLE_TASK
                applySlideInOut()
            }
        }
    }

    val name = "Nav3Fragment"
    val TAG = "FiDo"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loge("-----------$name onViewCreated",TAG)
    }

    override fun onPause() {
        super.onPause()
        loge("-----------$name onPause",TAG)
    }

    override fun onResume() {
        super.onResume()
        loge("-----------$name onResume",TAG)
    }

    override fun onStop() {
        super.onStop()
        loge("-----------$name onStop",TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        loge("-----------$name onDestroy",TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loge("-----------$name onDestroyView",TAG)
    }

}