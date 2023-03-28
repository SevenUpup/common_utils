package com.fido.common.common_utils.constraintlayout

import android.util.Log
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.util.showNormalInputDialog
import com.fido.common.common_base_util.ext.addFragment
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.toJson
import com.fido.common.common_base_util.util.ShellUtils
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcConstraintlayoutBinding
import kotlin.concurrent.thread

class ConstraintLayoutAc:BaseVBActivity<AcConstraintlayoutBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.ac_constraintlayout
    }

    override fun initView() {
        addFragment(android.R.id.content,DebugFragment())
    }

    override fun initData() {

    }

    override fun initEvent() {
    }
}