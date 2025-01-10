package com.fido.common.common_utils.rv

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.common_base_ui.base.entity.BaseMuiltEntity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.ext.image_select.clearImageSelectCache
import com.fido.common.common_base_ui.util.ui.RVScrollUtils
import com.fido.common.common_base_ui.util.ui.SmoothLinearLayoutManager
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_base_util.util.timer.Interval
import com.fido.common.databinding.AcRvBinding
import java.util.concurrent.TimeUnit

class RvAc:AppCompatActivity() {

    private val mBinding:AcRvBinding by binding()
    private lateinit var mRv: RecyclerView
    private lateinit var mRv2: RecyclerView

    private lateinit var layoutManager:SmoothLinearLayoutManager
    private lateinit var layoutManager2:SmoothLinearLayoutManager
    companion object{
        fun test(context:Context){
            Log.d("FiDo", "test: ---")
            Toast.makeText(context, "调用了test", Toast.LENGTH_SHORT).show()
        }
    }

    fun goEmpty(v:View){
        startActivity<RvEmptyViewAc>()
    }

    fun goScroll(v: View){
        startActivity<RvScrollWithWindownAc>()
    }

    fun goDrag(v: View){
        startActivity<RvDragSwipeAc>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRv = mBinding.mRv
        mRv2 = mBinding.mRv2

//        mBinding = DataBindingUtil.setContentView(this, R.layout.ac_rv)

        initView()

        Interval(10,2,TimeUnit.SECONDS,1,5).life(this)
            .subscribe {
                Log.d("FiDo", "s==>${it}")
            }
            .finish {
                Log.d("FiDo", "finish==>$it")
            }.start()

        initEvent()

    }

    private fun initEvent() {

        mBinding.btScroll.click {
            runCatching {
                RVScrollUtils.rvSmoothScrollToPosition(mRv, layoutManager, 5)
                RVScrollUtils.rvSmoothScrollToPosition(mRv2, layoutManager2, 5)

                RVScrollUtils.rvSmoothScrollToPosition(mBinding.mHorizRv,mBinding.mHorizRv.layoutManager as LinearLayoutManager,5)
            }.onFailure {
                loge("=====>${it.message}")
            }
        }
    }

    private fun initView() {


        layoutManager = SmoothLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRv.layoutManager = layoutManager
        layoutManager2 = SmoothLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRv2.layoutManager = layoutManager2
    }

    override fun onDestroy() {
        super.onDestroy()
        clearImageSelectCache()
    }

}

class MEntity : BaseMuiltEntity() {
    var content = ""
    var mType: Int = 0
        get() = field
        set(value) {
            itemType = value
            field = value
        }

}

