package com.fido.common.common_utils.blue_tooth;

/**
 * @author: FiDo
 * @date: 2025/3/26
 * @des: UUID的含义
 * UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") 解析后得到的UUID是蓝牙标准中为**串行端口配置文件(SPP)**保留的固定值。
 * <p>
 * 为什么需要这个UUID？
 * 设备发现：蓝牙设备之间需要约定使用相同的"服务ID"才能建立连接
 * <p>
 * 协议标识：这个特定的UUID表示使用RFCOMM协议（蓝牙的串行模拟协议）
 * <p>
 * 标准规范：这是蓝牙SIG组织定义的标准化UUID，所有支持SPP的设备都使用它
 * <p>
 * 各部分含义
 * 复制
 * 00001101-0000-1000-8000-00805F9B34FB
 * 00001101 - 服务类标识（表示串行端口服务）
 * <p>
 * 0000 - 保留字段
 * <p>
 * 1000 - 蓝牙协议版本基准
 * <p>
 * 8000 - 厂商特定位
 * <p>
 * 00805F9B34FB - 蓝牙SIG组织的基础UUID后缀
 * <p>
 * 实际应用场景
 * 当你在Android代码中这样创建蓝牙Socket时：
 * <p>
 * java
 * 复制
 * BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
 * 实际上是在告诉系统："我要用SPP协议与目标设备建立一个串行通信通道"
 * <p>
 * 注意事项
 * 必须完全匹配：这个UUID字符串必须一字不差，否则无法建立标准SPP连接
 * <p>
 * 特殊场景：如果你开发的是自定义蓝牙服务（非标准SPP），需要生成自己的UUID
 * <p>
 * 兼容性：这个UUID在Windows/Linux/Android/iOS等系统上都通用
 * <p>
 * 替代方案：也可以使用BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord()的简化方法
 * <p>
 * 为什么不用随机UUID？
 * 标准SPP设备只认这个特定UUID
 * <p>
 * 随机UUID会导致设备无法识别这是串口服务请求
 * <p>
 * 这是蓝牙协议栈的硬性规定
 * <p>
 * 这个UUID就像蓝牙世界的"HTTP端口80"，是一个公认的标准标识。保持这个值不变，才能确保你的Android设备能成功与电脑或其他蓝牙设备建立文件传输所需的串行连接。
 */

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fido.common.R;
import com.fido.common.common_base_ui.util.XpopUtilsKt;
import com.fido.common.common_base_util.ext.ToastExtKt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class BlueToothSendFileComputerActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_SELECT_FILE = 2;
    private static final int REQUEST_PERMISSIONS = 3;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 标准SPP UUID

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private File selectedFile;
    private BluetoothSocket bluetoothSocket;

    private Button btnSelectFile, btnSelectDevice, btnSendFile;
    private TextView tvSelectedFile, tvSelectedDevice, tvStatus;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_blue_tooth_send_computer);

        // 初始化视图
        btnSelectFile = findViewById(R.id.btn_select_file);
        btnSelectDevice = findViewById(R.id.btn_select_device);
        btnSendFile = findViewById(R.id.btn_send_file);
        tvSelectedFile = findViewById(R.id.tv_selected_file);
        tvSelectedDevice = findViewById(R.id.tv_selected_device);
        tvStatus = findViewById(R.id.tv_status);
        progressBar = findViewById(R.id.progress_bar);

        // 检查并请求权限
        checkPermissions();

        // 初始化蓝牙适配器
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 如果蓝牙未开启，请求用户开启
        XpopUtilsKt.showNormalConfirmDialog(this, "提醒", "请打开蓝牙", "确认", "取消", false, false,
                true, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        return null;
                    }
                }, new Function0<Unit>() {
                    @Override
                    public Unit invoke() {
                        if (!bluetoothAdapter.isEnabled()) {
                            if (ActivityCompat.checkSelfPermission(BlueToothSendFileComputerActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                                bluetoothAdapter.enable();
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            }
                        }
                        return null;
                    }
                });

        // 设置按钮点击事件
        btnSelectFile.setOnClickListener(v -> selectFile());
        btnSelectDevice.setOnClickListener(v -> selectDevice());
        btnSendFile.setOnClickListener(v -> sendFile());
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_SELECT_FILE);
    }

    private void selectDevice() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
            return;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.isEmpty()) {
            Toast.makeText(this, "没有已配对的设备", Toast.LENGTH_SHORT).show();
            return;
        }

        // 简单实现：选择第一个已配对的设备
        // 实际应用中应该让用户选择
        selectedDevice = pairedDevices.iterator().next();
        tvSelectedDevice.setText("已选择设备: " + selectedDevice.getName());
        checkReadyToSend();
    }

    private void checkReadyToSend() {
        btnSendFile.setEnabled(selectedFile != null && selectedDevice != null);
    }

    private void sendFile() {
        if (selectedFile == null || selectedDevice == null) {
            Toast.makeText(this, "请先选择文件和设备", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvStatus.setText("正在连接设备...");

        new Thread(() -> {
            try {
                // 建立蓝牙连接
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ToastExtKt.toast("检查权限:android.permission.BLUETOOTH_CONNECT");
                    return;
                }
                bluetoothSocket = selectedDevice.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();

                // 发送文件
                sendFileOverBluetooth(selectedFile);

                // 关闭连接
                bluetoothSocket.close();

                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvStatus.setText("文件发送完成");
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    tvStatus.setText("发送失败: " + e.getMessage());
                });
            }
        }).start();
    }

    private void sendFileOverBluetooth(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = bluetoothSocket.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        long totalBytesRead = 0;
        long fileSize = file.length();

        runOnUiThread(() -> tvStatus.setText("正在发送文件..."));

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;

            final int progress = (int) ((totalBytesRead * 100) / fileSize);
            runOnUiThread(() -> tvStatus.setText("发送中: " + progress + "%"));
        }

        fileInputStream.close();
        outputStream.flush();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "需要启用蓝牙才能继续", Toast.LENGTH_SHORT).show();
            finish();
        } else if (requestCode == REQUEST_SELECT_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = FileUtils.getPath(this, uri);
                if (path != null) {
                    selectedFile = new File(path);
                    tvSelectedFile.setText("已选择文件: " + selectedFile.getName());
                    checkReadyToSend();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
