package com.fido.common.common_utils.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.toast
import com.fido.common.databinding.AcMotionBalls2Binding

/**
@author FiDo
@description:
@date :2023/6/7 11:20
 */
class MotionBallsAc:AppCompatActivity() {

    val binding: AcMotionBalls2Binding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSomething()
    }

    private fun initSomething() {
        toast("init")
    }

}