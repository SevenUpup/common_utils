package com.fido.common.common_utils.time

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fido.common.common_base_util.util.timer.Interval
import com.fido.common.common_base_util.util.timer.countDown
import com.fido.common.common_utils.databinding.AcIntervalTimeBinding
import com.fido.common.common_utils.R
import java.util.concurrent.TimeUnit

class IntervalTimeAc:AppCompatActivity() {

    lateinit var binding:AcIntervalTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.ac_interval_time)
        initView()

        countDown(5,0, onFinish = {
        }){
            Log.d("FiDo", "我是一个正计时 $it")
        }
        countDown(10,100, isOnlyResumed = true, onFinish = {
            binding.tvWhenResumed.text = "i am done"
        }){
            binding.tvWhenResumed.text = "$it"
        }
    }

    private fun initView() {
        val interval = Interval(50,1, TimeUnit.SECONDS, 5,2)
        binding.start.setOnClickListener {
            interval.life(this)
                .subscribe {
                    binding.tv.text = "now=> $it"
                }
                .subscribe {
                    binding.tv2.text = it.toString()
                }
                .finish {
                    binding.tv.setText("finish ==> $it")
                }
                .finish {
                    binding.tv2.text = "finish2"
                }
                .start()
        }

        binding.pause.setOnClickListener {
            interval.pause()
        }

        binding.resume.setOnClickListener {
            interval.resume()
        }

        binding.stop.setOnClickListener {
            interval.stop()
        }

        binding.btSwitch.setOnClickListener {
            interval.switch()
        }
        binding.cancle.setOnClickListener {
            interval.cancle()
        }

        binding.reset.setOnClickListener {
            interval.reset()
        }
    }

}