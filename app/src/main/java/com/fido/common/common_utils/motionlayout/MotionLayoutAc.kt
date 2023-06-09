package com.fido.common.common_utils.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.dp
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcMotionLayoutBinding

/**
@author FiDo
@description:
@date :2023/6/6 11:19
 */
class MotionLayoutAc : AppCompatActivity() {

    val binding: AcMotionLayoutBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startConstrainSet()
    }

    private fun startConstrainSet() {
        //定义起始状态
        val startConstraintSet = ConstraintSet()
        startConstraintSet.clone(binding.clContainer)
        //定义结束状态的 ConstrainSet
        val endConstraintSet = ConstraintSet()
        endConstraintSet.clone(binding.clContainer)
        endConstraintSet.connect(
            R.id.button0,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END,
            8.dp
        )
        endConstraintSet.setHorizontalBias(R.id.button0, 1.0f)
        binding.clContainer.postDelayed({
            // 在需要执行动画的地方
            TransitionManager.beginDelayedTransition(binding.clContainer)
            // 设置结束状态的 ConstraintSet
            endConstraintSet.applyTo(binding.clContainer)
        }, 1000)

    }

}