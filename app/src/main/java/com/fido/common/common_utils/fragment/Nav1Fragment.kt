package com.fido.common.common_utils.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.fido.common.base.BaseVBFragment
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.R
import com.fido.common.databinding.FragmentNav1Binding
import com.fido.common.easy_navigation.*


/**
@author FiDo
@description:
@date :2023/4/17 9:57
 */
class Nav1Fragment:BaseVBFragment<FragmentNav1Binding>(R.layout.fragment_nav1) {

    companion object{
        fun getInstance(str:String):Nav1Fragment{
            val f = Nav1Fragment()
            val bundle = Bundle()
            bundle.putString("S",str)
            f.arguments = bundle
            return f
        }
    }

    var s = ""
    override fun initView() {
        s = arguments?.getString("S","0")?:""
        binding.btTop.text = "Go Nav ${s}"
        binding.btTop.isAllCaps = false
//        binding.mRv.vertical()
//            .bindData(listOf("1","2","3"),R.layout.item_rv_text){holder, position, item ->
//                holder.setText(R.id.mTitle,"------$item")
//            }
    }

    override fun initData() {
        binding.btGoNav2.click {
            navigation(R.id.action_nav1_to_nav2)
        }

        binding.btTop.click {
            if (s == "66") {
//                skipOtherAppActivity("com.yechaoa.materialdesign","com.yechaoa.materialdesign.activity.MainActivity")
                navigation(R.id.action_nav2_to_Fragment2){
                    launchMode = LaunchMode.SINGLE_TASK
                    applyFadeInOut()
                }
                return@click
            }
            navigation(R.id.action_nav1_to_nav2Fragment, bundleOf("S" to "66")){
                launchMode = LaunchMode.SINGLE_TASK
                applySlideInOut()
            }
        }

        binding.btGoNav2WithCode.click {
            navigator.start<Nav2Fragment> {
                launchMode = LaunchMode.SINGLE_TASK
                applyFadeInOut()
            }
        }
    }

    fun test(){
        toast("nav1 test fun")
    }

    val name = "Nav1Fragment"
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