package com.fido.common.common_utils.picker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fido.common.common_base_util.ext.logGson
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.log.LogUtils
import com.fido.common.common_base_util.toJson
import com.fido.common.common_utils.databinding.ActivityLinkagePickerBinding
import com.fido.common.common_utils.R
import com.fido.common.common_utils.picker.entities.City
import com.zyyoona7.picker.listener.OnDoubleLoadDataListener
import com.zyyoona7.picker.listener.OnTripleLoadDataListener
import com.zyyoona7.wheel.WheelView
import com.zyyoona7.wheel.formatter.SimpleTextFormatter

class LinkagePickerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLinkagePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_linkage_picker)

        initView()

        initEvent()
    }

    private fun initView() {
        binding.linkagePicker1.setTextFormatter(CityFormatter())
        binding.linkagePicker2.setTextFormatter(CityFormatter())
        binding.linkagePicker3.setTextFormatter(CityFormatter())
        binding.linkagePicker4.setTextFormatter(CityFormatter())
        binding.linkagePicker5.setTextFormatter(CityFormatter())
        binding.linkagePicker6.setTextFormatter(CityFormatter())

        val cityList = ParseHelper.parseTwoLevelCityList(this)
        val compatCityList = arrayListOf<City>()
        val compatTwoLevelList = arrayListOf<List<City>>()
        ParseHelper.initTwoLevelCityList(this, compatCityList, compatTwoLevelList)

        //两级联动
        binding.linkagePicker1.setData(cityList, DefaultDoubleLoadDataListener())
        binding.linkagePicker2.setData(cityList, DefaultDoubleLoadDataListener())
        binding.linkagePicker3.setData(cityList, DefaultDoubleLoadDataListener())
        //两级联动,兼容1.x的数据类型
        binding.linkagePicker4.setData(
            compatCityList,
            DoubleLoadDataListenerCompat(compatTwoLevelList)
        )

        val threeLevelList = ParseHelper.parseThreeLevelCityList(this)
        val compatThreeLevelList = arrayListOf<City>()
        val compatThreeLevelList1 = arrayListOf<List<City>>()
        val compatThreeLevelList2 = arrayListOf<List<List<City>>>()
        ParseHelper.initThreeLevelCityList(
            this, compatThreeLevelList,
            compatThreeLevelList1, compatThreeLevelList2
        )

        //三级联动
        binding.linkagePicker5.setData(threeLevelList, DefaultTripleLoadDataListener())
        //三级联动,兼容1.x的数据类型
        binding.linkagePicker6.setData(
            compatCityList,
            TripleLoadDataListenerCompat(compatThreeLevelList1, compatThreeLevelList2)
        )

        //设置默认选中项
        binding.linkagePicker2.setSelectedItem("福建省","福州市",isCompareFormatText = true)
//        binding.linkagePicker5.setSelectedItem(
//            "安徽省",
//            "淮南市", "八公山区", true
//        )
        binding.linkagePicker5.setSelectedPosition(2,3,5)

        val provinceNames = binding.linkagePicker2.getLinkage1WheelView().getAdapter()?.getDataList<Any>()
        val cityNames = binding.linkagePicker2.getLinkage2WheelView().getAdapter()?.getDataList<Any>()
        loge("proName===> ${provinceNames?.toJson()}")
        loge("cityNames==> ${cityNames?.toJson()}")

        val p = binding.linkagePicker5.getLinkage1WheelView().getAdapter()?.getDataList<Any>()
        val c = binding.linkagePicker5.getLinkage2WheelView().getAdapter()?.getDataList<Any>()
        val a = binding.linkagePicker5.getLinkage3WheelView().getAdapter()?.getDataList<Any>()
        loge("p==> ${p?.toJson()}")
        loge("c==> ${c?.toJson()}")
        loge("a==> ${a?.toJson()}")

    }

    private fun initEvent() {

    }

}

class CityFormatter : SimpleTextFormatter<City>() {
    override fun text(item: City): Any {
        return item.name
    }
}

class DefaultDoubleLoadDataListener : OnDoubleLoadDataListener {
    override fun onLoadData2(linkage1Wv: WheelView): List<Any> {
        return linkage1Wv.getSelectedItem<City>()?.districts ?: emptyList()
    }
}

class DoubleLoadDataListenerCompat(private val twoLevelList: List<List<City>>) :
    OnDoubleLoadDataListener {
    override fun onLoadData2(linkage1Wv: WheelView): List<Any> {
        return twoLevelList[linkage1Wv.getSelectedPosition()]
    }
}

class DefaultTripleLoadDataListener : OnTripleLoadDataListener {
    override fun onLoadData2(linkage1Wv: WheelView): List<Any> {
        return linkage1Wv.getSelectedItem<City>()?.districts ?: emptyList()
    }

    override fun onLoadData3(linkage1Wv: WheelView, linkage2Wv: WheelView): List<Any> {
        return linkage2Wv.getSelectedItem<City>()?.districts ?: emptyList()
    }

}

class TripleLoadDataListenerCompat(
    private val twoLevelList: List<List<City>>,
    private val threeLevelList: List<List<List<City>>>
) :
    OnTripleLoadDataListener {
    override fun onLoadData2(linkage1Wv: WheelView): List<Any> {
        return twoLevelList[linkage1Wv.getSelectedPosition()]
    }

    override fun onLoadData3(linkage1Wv: WheelView, linkage2Wv: WheelView): List<Any> {
        return threeLevelList[linkage1Wv.getSelectedPosition()][linkage2Wv.getSelectedPosition()]
    }

}