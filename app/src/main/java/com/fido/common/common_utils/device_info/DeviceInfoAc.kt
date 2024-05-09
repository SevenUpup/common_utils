package com.fido.common.common_utils.device_info

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.app
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.getColor
import com.fido.common.common_base_util.sp
import com.fido.common.common_utils.R
import com.fido.common.common_utils.databinding.AcDeviceInfoBinding

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:
 */
class DeviceInfoAc:AppCompatActivity() {

    private val binding:AcDeviceInfoBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val androidId = Settings.System.getString(app.contentResolver, Settings.Secure.ANDROID_ID)

        binding.apply {

            bt1.throttleClick {
                showCustomToast(bt1.text.toString())
            }

            addTextView(androidId)

            cbAgree.setOnCheckedChangeListener { compoundButton, b ->
                PrivacyUtil.isAgreePrivacy = b
                updateData()
            }

            cbUseCache.setOnCheckedChangeListener { compoundButton, b ->
                PrivacyUtil.isUseCache = b
                updateData()
            }
        }

        updateData()

    }

    fun showCustomToast(text: String) {
        toast("click $text")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateData() {
        binding.apply {
            val getRunningAppProcesses = getRunningAppProcesses(this@DeviceInfoAc)
            btnGetRunningAppProcesses.text =
                "getRunningAppProcesses size=${getRunningAppProcesses?.size}"

            val getRecentTasks = getRecentTasks(this@DeviceInfoAc)
            btnGetRecentTasks.text = ("getRecentTasks size=${getRecentTasks?.size}")

            val getRunningTasks = getRunningTasks(this@DeviceInfoAc)
            btnGetRunningTasks.text = ("getRunningTasks size=${getRunningTasks?.size}")

            val getAllCellInfo = getAllCellInfo(this@DeviceInfoAc)
            btnGetAllCellInfo.text = ("getAllCellInfo size=${getAllCellInfo?.size}")

            val getDeviceId = getDeviceId(this@DeviceInfoAc)
            btnGetDeviceId.text = ("getDeviceId=$getDeviceId")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getSimSerialNumber.text = ("getSimSerialNumber=${getSimSerialNumber(this@DeviceInfoAc)}")
            }

            val androidId = Settings.System.getString(this@DeviceInfoAc.contentResolver, Settings.Secure.ANDROID_ID)
            btnGetIdAndroid.text = ("androidId=$androidId")

            getSSID.text = ("getSSID=${getSSID(this@DeviceInfoAc)}")
            getBSSID.text = ("getBSSID=${getBSSID(this@DeviceInfoAc)}")
            getMacAddress.text = ("getMacAddress=${getMacAddress(this@DeviceInfoAc)}")
            getConfiguredNetworks.text = ("getConfiguredNetworks,size=${getConfiguredNetworks(this@DeviceInfoAc)?.size}")

            getSensorList.text = ("getSensorList size=${getSensorList(this@DeviceInfoAc)?.size}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getImei.text = ("getImei=${getImei(this@DeviceInfoAc)}")
            }

            getScanResults.text = "getScanResults size=${getScanResults(this@DeviceInfoAc)?.size}"
            getDhcpInfo.text = "getDhcpInfo=${getDhcpInfo(this@DeviceInfoAc)}"

            getLastKnownLocation.text = "getLastKnownLocation=${getLastKnownLocation(this@DeviceInfoAc)}"

            requestLocationUpdates(this@DeviceInfoAc)
            requestLocationUpdates.text = "requestLocationUpdates"
        }
    }


    private fun addTextView(text: String) {
        binding.llContainer.apply {
            addView(
                TextView(context).apply {
                    setText(text)
                    textSize = 15.sp.toFloat()
                    setTextColor(R.color.colorTextBlack.getColor)
                }
            )
        }
    }

}