package com.fido.common.common_utils.picker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.databinding.DataBindingUtil
import com.fido.common.common_base_util.ext.*
import com.fido.common.common_base_util.startActivity
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcPickerViewBinding
import com.zyyoona7.picker.helper.TimePickerHelper
import com.zyyoona7.picker.listener.OnDateSelectedListener
import com.zyyoona7.wheel.WheelView
import com.zyyoona7.wheel.adapter.ArrayWheelAdapter
import com.zyyoona7.wheel.formatter.IntTextFormatter
import com.zyyoona7.wheel.listener.OnItemSelectedListener
import java.util.*
import kotlin.math.abs


class PickerViewAc: AppCompatActivity() {

    private lateinit var binding :AcPickerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.ac_picker_view)

        initView()

        initListeners()
    }

    fun goLink(v:View){
        startActivity<LinkagePickerActivity>()
    }

    private fun initView() {
        // Time Picker
        val calendar=Calendar.getInstance()
        binding.timePickerAuto.setTime(calendar, TimePickerHelper.is24HourMode(this))

        // Date Picker
        binding.wheelYear.setTextFormatter(IntTextFormatter("公元%d年"))
        binding.wheelMonth.setTextFormatter(IntTextFormatter("%d月"))
        binding.wheelDay.setTextFormatter(IntTextFormatter("%d日"))

        binding.datePicker1.setRightText("年","月","日")
        binding.datePicker1.setRightTextGravity(Gravity.CENTER)
        binding.datePicker1.setRightTextColor(Color.YELLOW)

        binding.datePicker2.setRightTextMarginLeft(10f)

        binding.datePicker5.setMaxSelectedDate(Calendar.getInstance(), WheelView.OverRangeMode.HIDE_ITEM)

        binding.datePicker6.setYearRange(2023,2023)
        binding.datePicker6.setOnDateSelectedListener(object :OnDateSelectedListener{
            override fun onDateSelected(year: Int, month: Int, day: Int, date: Date) {
                toast("$year-$month-$day  ${date.format2String()}")
            }
        })

        "123"{

        }
    }

    //operator 运算符重载
    operator fun <T:String> T.invoke(config:T.()->Unit={}){
        longToast(this)
    }

    private fun initListeners() {
        binding.nestedScroll.setOnScrollChangeListener(object :OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                val scale = if (scrollY <= binding.linkPicker.height) {
                    abs(scrollY.toFloat() / binding.linkPicker.height.toFloat())
                } else {
                    1f
                }
                loge("scale=$scale")
                binding.linkPicker.setBackgroundColor(evaluateColor(scale,R.color.purple_700,R.color.teal_700))
            }

        })
        binding.wheelYear.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(wheelView: WheelView, adapter: ArrayWheelAdapter<*>, position: Int) {
                binding.wheelDay.year = adapter.getSelectedItem<Int>() ?: 2019
            }
        })
        binding.wheelMonth.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(wheelView: WheelView, adapter: ArrayWheelAdapter<*>, position: Int) {
                binding.wheelDay.month = adapter.getSelectedItem<Int>() ?: 1
            }
        })

        binding.tvPacket.click {
//            val intent = Intent(Intent.ACTION_MAIN)
//            val componentName = ComponentName("com.zyyoona7.demo","com.zyyoona7.demo.MainActivity")
//            intent.component = componentName
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra("","") //传值
//            startActivity(intent)

//            startActivity(packageManager.getLaunchIntentForPackage("com.zyyoona7.demo"))

//            val packageManager = packageManager
//            val intent = packageManager.getLaunchIntentForPackage("com.zyyoona7.demo")
//            startActivity(intent)

            toast(packageName + " isNetConnect = ${isNetworkConnected}")

//            val intent = Intent()
//            intent.setPackage("com.zyyoona7.demo")
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivityForResult(intent, 0)

            Jump2App(this,"com.zyyoona7.demo")

//            val packageName = "com.yunxiang.dwuyu" // 指定应用的包名
//            val packageName = "cn.goodjobs.enterprise" // 指定应用的包名
//            toast(packageName)
//            val intent = packageManager.getLaunchIntentForPackage(packageName)
//            if (intent != null) {
//                startActivity(intent)
//            } else {
//                // 没有找到该应用
//            }

        }
    }


    fun Jump2App(context: Context, packagename: String) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = context.packageManager.getPackageInfo(packagename, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (packageinfo == null) {
            Toast.makeText(context, "还未集成该模块，敬请期待", Toast.LENGTH_SHORT).show()
            return
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)

        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList: List<ResolveInfo> = context.getPackageManager()
            .queryIntentActivities(resolveIntent, 0)
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packagename = 参数packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            val cn = ComponentName(packageName, className)
            intent.component = cn
            context.startActivity(intent)
        }
    }

}