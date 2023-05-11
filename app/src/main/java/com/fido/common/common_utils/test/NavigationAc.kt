package com.fido.common.common_utils.test

import android.view.Gravity
import androidx.navigation.findNavController
import com.fido.common.base.BaseVDActivity
import com.fido.common.base.BaseViewModel
import com.fido.common.common_base_ui.util.showNormalListDialog
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcTestBinding
import com.fido.common.easy_navigation.navigateUp
import com.fido.common.easy_navigation.popBackStack

/**
@author FiDo
@description:
@date :2023/4/13 11:03
 */
class NavigationAc: BaseVDActivity<MyViewModel,AcTestBinding>() {

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val binding = binding<AcTestBinding>(R.layout.ac_test).value
//        binding.mBt.text ="Test"
//        binding.mBt.isAllCaps = false
//
//    }
    override fun getLayoutId(): Int {
        return R.layout.ac_test
    }

    override fun initView() {
        binding.mBt.text = "Test Navigation"

        binding.navigateUp.click {
            navigateUp(R.id.nav_host)
        }

        binding.popBackStack.click {
            popBackStack(R.id.nav_host)
        }

        binding.pop.click {
            val navController = findNavController(R.id.nav_host)
            showNormalListDialog(listOf("nav2Fragment","nav2Fragment"),orientation = Gravity.BOTTOM){position, text ->
                when(position){
                    0->{
                        navController.popBackStack(R.id.nav2Fragment, true)
                    }
                    1->{
                        navController.popBackStack(R.id.nav1Fragment, true)
                    }
                }
            }
        }
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

}

class MyViewModel:BaseViewModel(){

    init {
        launchOnUI {

        }
    }
}