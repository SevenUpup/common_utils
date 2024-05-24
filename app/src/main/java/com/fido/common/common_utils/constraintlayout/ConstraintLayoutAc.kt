package com.fido.common.common_utils.constraintlayout

import android.graphics.Color
import android.widget.TextView
import com.fido.common.base.BaseVBActivity
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.addFragment
import com.fido.common.common_base_util.ext.startCountDown
import com.fido.common.common_base_util.ext.toast
import com.fido.common.R
import com.fido.common.databinding.AcConstraintlayoutBinding

class ConstraintLayoutAc:BaseVBActivity<AcConstraintlayoutBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.ac_constraintlayout
    }

    override fun initView() {
        addFragment(android.R.id.content,DebugFragment())

        with(binding.tvConstraintWidth){
            setTextColor(Color.BLACK)
            startCountdown(this)
            throttleClick {
                toast("click me")
                startCountdown(this@with)
            }
        }
    }

    private fun startCountdown(textView: TextView) {
        textView.startCountDown(this@ConstraintLayoutAc, sencondInFuture = 10, onTick = {
            text = "i am a countdown now residue time = $it s"
        }){
            text = " i am finish "
        }
    }

    override fun initData() {

    }

    override fun initEvent() {
    }
}