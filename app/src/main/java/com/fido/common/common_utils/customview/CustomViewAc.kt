package com.fido.common.common_utils.customview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.dp
import com.fido.common.common_base_util.ext.activity
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.clipToRoundView
import com.fido.common.common_base_util.ext.height
import com.fido.common.common_base_util.ext.margin
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.getScreenHeightPx
import com.fido.common.common_base_util.getScreenWidthPx
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_utils.customview.bean.ClassInfoBean
import com.fido.common.common_utils.customview.fake_taobao.FakeTaobaoKingkongViewAc
import com.fido.common.common_utils.customview.ninegrid.ImageNineGridAdapter
import com.fido.common.common_utils.widgets.chat_bar.BarChartItem
import com.fido.common.databinding.AcCustomViewBinding
import kotlin.math.min


/**
 * @author: FiDo
 * @date: 2024/4/7
 * @des:
 */
class CustomViewAc:AppCompatActivity() {

    private val binding:AcCustomViewBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.view1.clipToRoundView(20.dp.toFloat(),false)
        binding.view2.clipToRoundView(20.dp.toFloat(),true)

        val tLiveData = MutableLiveData<Int>()
        val transformationData = Transformations.map(tLiveData){
            it.toString()
        }

        binding.btMyCustom.throttleClick {
            startActivity<HTCustomViewAc>()
        }
        binding.btSlidingConflict.throttleClick {
            startActivity<SlidingConflictAc>()
        }

        var mTranslationX = 200f
        binding.imageView.apply {
            click {
                toast("click img")
                mTranslationX +=200f
                binding.imageView.translationX = if (mTranslationX >= getScreenWidthPx()){
                    mTranslationX = 0f
                    0f
                } else mTranslationX
            }
            animate().apply {
                translationX(200f)
                duration = 2000
                start()
            }
//            ObjectAnimator.ofFloat(binding.imageView,View.TRANSLATION_X,0f,200f).start()
        }

        binding.btTestBannerVp.click {
            startActivity<FakeTaobaoKingkongViewAc>()
        }

        val view = binding.codeRainView
        view.height(getScreenHeightPx())
        view.click {}

        val imgs = listOf("1", "2", "3", "4", "5")
        binding.nineGridView.run {
            setSingleModeSize(400,800)
            setAdapter(ImageNineGridAdapter(imgs))
        }

        initChatRv()

        binding.button.click {
            //new一个animset,传入的是对象本身，apply用run好像也可以
            AnimatorSet().apply {
                ObjectAnimator.ofPropertyValuesHolder(
                    binding.textView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f)
                ).apply {
                    duration = 300L
                    interpolator = LinearInterpolator()
                }.let {
                    play(it).with(
                        ObjectAnimator.ofPropertyValuesHolder(
                            binding.button,
                            PropertyValuesHolder.ofFloat("translationX", 0f, 100f)
                        ).apply {
                            duration = 3000L
                            interpolator = LinearInterpolator()
                        }
                    )
                    play(it).before(
                        ValueAnimator.ofInt(0,getScreenWidthPx()).apply {
                            addUpdateListener { animation ->
//                                logd("value=>${animation.animatedValue}")
                                binding.imageView.margin(leftMargin = animation.animatedValue as Int)
//                                binding.imageView.left = animation.animatedValue as Int
                            }
                            duration = 4000L
                            interpolator = LinearInterpolator()
                        }
                    )
                }
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        Toast.makeText(activity,"animation end",Toast.LENGTH_SHORT).show()
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })
                start()
            }
        }

    }

    var testClassIndoList: ArrayList<ClassInfoBean> = ArrayList()
    // 测试BarChart
    private val sumRandomList = listOf(241,182,200,194)
    private val onGuardRandomList = listOf(230,180,100,66)
    private fun initBarChartView(){
        for (i in 0..10) {
            val classInfoBean = ClassInfoBean()
            classInfoBean.onJobClassName = "班组${i+1}"
            classInfoBean.guardSum = sumRandomList.random()
            classInfoBean.onGuardNum = min(classInfoBean.guardSum,onGuardRandomList.random())
            testClassIndoList.add(classInfoBean)
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val barChatLoop = Runnable {
        val adapter = binding.mChatRv.adapter
        val layoutManager = binding.mChatRv.layoutManager as LinearLayoutManager
        val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
        val index = (lastVisiblePos + 1) % (adapter?.itemCount?:0)
        if (0 == index) {
            binding.mChatRv.scrollToPosition(index)
        } else {
            binding.mChatRv.smoothScrollToPosition(index)
        }
        doLoop()


    }

    private fun doLoop() {
        handler.postDelayed(barChatLoop,2500)
    }

    override fun onResume() {
        super.onResume()
        val layoutManager = binding.mChatRv.layoutManager as LinearLayoutManager
        val lastVisiblePos = layoutManager.findLastVisibleItemPosition()
        if (lastVisiblePos < (binding.mChatRv.adapter?.itemCount?:0)) {
            handler.post(barChatLoop)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun initChatRv() {
        initBarChartView()
        val layoutManager = MyLinearLayoutManager(this)
        binding.mChatRv.layoutManager = layoutManager
        val mAdapter = RvBarChatAdapter(testClassIndoList)
        binding.mChatRv.adapter = mAdapter
    }

    private class MyLinearLayoutManager(val context: Context) : LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: RecyclerView.State?,
            position: Int
        ) {
            super.smoothScrollToPosition(recyclerView, state, position)
            val smoothScroller = object :LinearSmoothScroller(context){
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
//                    return super.calculateSpeedPerPixel(displayMetrics)
                    return 249f / displayMetrics.densityDpi;
                }
            }
            smoothScroller.targetPosition = position;
            startSmoothScroll(smoothScroller);

        }
    }


    class RvBarChatAdapter(val data:ArrayList<ClassInfoBean>?): RecyclerView.Adapter<RvBarChatAdapter.VH>(){
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): VH {
            return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_bar,parent,false))
        }

        override fun onBindViewHolder(
            holder: VH,
            position: Int
        ) {
            val barChartItem = holder.barChartItem
            if(data == null || data.isNullOrEmpty()){
                barChartItem.setRatio(0.0);
                barChartItem.setBarRatio(0.0);
            }else{
                val classInfoBean = data.get(position)
                holder.setGuardNum(""+classInfoBean.getOnGuardNum())
                holder.setGuardSum(""+classInfoBean.getGuardSum())
                holder.setClassType(classInfoBean.getOnJobClassName())
                barChartItem.setRatio(classInfoBean.getSumRatio()/100.0);
                barChartItem.setBarRatio(classInfoBean.getOnGuardNumRatio()/100.0);
            }
        }

        override fun getItemCount(): Int = data?.size?:0

        inner class VH(itemView: View): RecyclerView.ViewHolder(itemView) {

            val barChartItem = itemView.findViewById<BarChartItem>(R.id.sum_bar_chart)

            fun setGuardNum(text:String){
                itemView.findViewById<TextView>(R.id.on_guard_num).text = text
            }

            fun setGuardSum(text:String){
                itemView.findViewById<TextView>(R.id.guard_sum).text = text
            }

            fun setClassType(text:String){
                itemView.findViewById<TextView>(R.id.bar_chart_class_type).text = text
            }

        }

    }

}