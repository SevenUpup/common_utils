package com.fido.common.common_utils.naviga

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_utils.databinding.AcCodeNavigationBinding
import com.fido.common.common_utils.R
import com.fido.common.easy_navigation.*

/**
@author FiDo
@description:
@date :2023/4/18 16:14
 */
class CodenavigationAc :BaseVBActivity<AcCodeNavigationBinding>(){


    override fun getLayoutId(): Int {
        return R.layout.ac_code_navigation
    }

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.loadRootFragment(
            startDestinationClazz = CodeNavFragment::class
        )
    }

    fun getAllFragment():List<Fragment>{
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val allF = getAllNavFragments(R.id.nav_host)
        val currentF = navHostFragment.getCurFragment()
        loge("allF==>${allF.size}","FiDo")
        loge("currentF?.tag==>${currentF}","FiDo")
        navHostFragment.showLog()
        return allF
    }

    fun popAll(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.popAllFragment(false)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }
}