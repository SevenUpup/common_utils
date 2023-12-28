package com.fido.common.common_utils.annotation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.annotation.view.BindView
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.R

/**
@author FiDo
@description:
@date :2023/12/21 17:01
 */
class AnnotationAc:AppCompatActivity(R.layout.ac_annotation) {

    @BindView(R.id.mBt)
    lateinit var mBt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindView()
        mBt.setOnClickListener{
            toast("hahahhahah")
        }
    }

}