package com.fido.common.common_utils.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcMotionLayout2Binding

/**
@author FiDo
@description:
@date :2023/6/7 9:16
 */
class MotionLayoutAc2:AppCompatActivity() {

    val binding:AcMotionLayout2Binding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMotion()
    }

    private var toggle= false
    private fun initMotion() {
        val start = ConstraintSet().apply { clone(binding.clContainer) }

        val end= ConstraintSet().apply { clone(this@MotionLayoutAc2,R.layout.ac_motion_layout2_code) }

        binding.clContainer.click {
            val constraintSet = if (toggle) start else end
            TransitionManager.beginDelayedTransition(binding.clContainer)
            constraintSet.applyTo(binding.clContainer)
            toggle = !toggle
        }


    }

}