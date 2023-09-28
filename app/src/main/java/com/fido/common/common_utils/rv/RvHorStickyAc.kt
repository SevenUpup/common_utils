package com.fido.common.common_utils.rv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_utils.databinding.AcRvHorstickyBinding

/**
@author FiDo
@description:
@date :2023/9/28 10:37
*/
class RvHorStickyAc:AppCompatActivity() {

    val binding:AcRvHorstickyBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        
    }


}