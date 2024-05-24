package com.fido.common.common_utils.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fido.common.common_base_util.ext.arguments
import com.fido.common.common_base_util.ext.withArguments
import com.fido.common.R

/**
 * @author: FiDo
 * @date: 2024/3/8
 * @des:
 */
class TestFragment:Fragment(R.layout.fragment_test) {

    companion object{
        fun newInstance(content:String="") = TestFragment().withArguments(Pair("S",content))
    }

    val value = arguments<String>("S")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<TextView>(R.id.mTv).text = value.value
        Log.d("FiDo", "onViewCreated: ----${value}")

    }

    override fun onResume() {
        super.onResume()
        Log.d("FiDo", "onResume: -----${value}")
    }

}