package com.fido.common.common_utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity
import com.fido.common.common_base_ui.ext.*
import com.fido.common.common_base_util.BaseUtils.getColorCompat
import com.fido.common.common_base_util.BaseUtils.startActivity
import com.fido.common.common_utils.databinding.ActivityMainBinding
import com.fido.common.common_utils.databinding.ItemRvImgBinding
import com.fido.common.common_utils.databinding.ItemRvTextBinding
import com.fido.common.common_utils.rv.RvAc
import com.google.gson.Gson
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var mBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        initView()

    }

    private fun initView() {

        mBinding.btRv.setOnClickListener {
            startActivity<RvAc>()
        }

    }

}

