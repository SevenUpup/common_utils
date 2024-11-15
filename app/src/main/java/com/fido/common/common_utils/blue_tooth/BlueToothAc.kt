package com.fido.common.common_utils.blue_tooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.toJson
import com.fido.common.databinding.AcBlueToothBinding
import java.util.UUID

/**
 * @author: FiDo
 * @date: 2024/11/13
 * @des:
 */
class BlueToothAc:AppCompatActivity() {

    private val binding:AcBlueToothBinding by binding()

    private val REQUEST_ENABLE_BT = 22
    private val REQUEST_CODE_BLUETOOTH_PERMISSION = 20

    // ======= classic BlueTooth ========
    private var classicBtReceiver:BroadcastReceiver?=null
    private var _socket:BluetoothSocket?=null
    private val M_UUID = UUID.randomUUID()
    // ======= classic BlueTooth ========

    // Bluetooth Low Energy 低功耗蓝牙
    // ============ BLE =============
    private var _blueToothDevice:BluetoothDevice?=null

    private val blueToothAdapter by lazy {
        (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            btClassic.click {
                //1.启用蓝牙
                checkBlueToothEnable()
                //2.扫描周围的蓝牙设备
                blueToothAdapter.startDiscovery()
                //3.监听设备发现广播：你需要注册一个广播接收器来监听蓝牙设备的发现事件
                registerBroadcastReceiver()
                //5. 蓝牙数据通信
                //使用 BluetoothSocket 来进行数据的输入输出。你可以通过 InputStream 和 OutputStream 来读取和发送数据：
                val inputStream = _socket?.inputStream
                val outputStream = _socket?.outputStream
            }

            btBle.click {
                checkBlueToothEnable()
                //1.BLE扫描
                blueToothAdapter.bluetoothLeScanner.startScan(object : ScanCallback() {
                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                        loge("onScanFailed errorCode=${errorCode}")
                    }

                    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                        super.onBatchScanResults(results)
                        loge("onBatchScanResults results=${results?.toJson()}")
                    }

                    override fun onScanResult(callbackType: Int, result: ScanResult?) {
                        super.onScanResult(callbackType, result)
//                        _blueToothDevice = result?.device
                        loge("onScanResult result=${result?.toJson()} callbackType=${callbackType}")
                    }
                })
                //2.连接 BLE 设备：对于 BLE 设备，你需要使用 BluetoothGatt 来进行连接和数据通信
                _blueToothDevice?.connectGatt(this@BlueToothAc,false,object : BluetoothGattCallback() {

                    override fun onConnectionStateChange(
                        gatt: BluetoothGatt?,
                        status: Int,
                        newState: Int
                    ) {
                        super.onConnectionStateChange(gatt, status, newState)
                        when (status) {
                            BluetoothProfile.STATE_CONNECTED->{

                            }
                            BluetoothProfile.STATE_CONNECTING->{

                            }
                            BluetoothProfile.STATE_DISCONNECTED->{

                            }
                        }
                    }

                    override fun onCharacteristicChanged(
                        gatt: BluetoothGatt?,
                        characteristic: BluetoothGattCharacteristic?
                    ) {
                        super.onCharacteristicChanged(gatt, characteristic)
                        loge("onCharacteristicChanged gatt=${gatt?.toJson()}  characteristic=${characteristic}")
                    }

                })
            }
        }
    }

    private fun registerBroadcastReceiver() {
        if (classicBtReceiver == null) {
            classicBtReceiver = object :BroadcastReceiver(){
                @SuppressLint("MissingPermission")
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action
                    if (BluetoothDevice.ACTION_FOUND == action) {
                        val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        loge("device info =${device?.toJson()}")
                        //4. 获取设备信息并进行连接 连接到设备：你可以使用 BluetoothSocket 来建立蓝牙连接
                        _socket = device?.createRfcommSocketToServiceRecord(M_UUID)
                        _socket?.connect()
                    }
                }
            }
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(classicBtReceiver,filter)
        }
    }

    private fun checkBlueToothEnable() {
        if (blueToothAdapter == null) {
            // 设备不支持蓝牙
        }else if (!blueToothAdapter.isEnabled) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 如果没有授予 BLUETOOTH_CONNECT 权限，请求权限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    REQUEST_CODE_BLUETOOTH_PERMISSION
                )
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(classicBtReceiver)
        releaseBt()
    }

    @SuppressLint("MissingPermission")
    private fun releaseBt() {
        with(blueToothAdapter){
            disable()
            cancelDiscovery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            loge("onActivityResult=>${data?.toJson()}")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_BLUETOOTH_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予，继续执行蓝牙启用的请求
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                // 权限被拒绝，提示用户
                Toast.makeText(this, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

}