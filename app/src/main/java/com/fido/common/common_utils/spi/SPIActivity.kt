package com.fido.common.common_utils.spi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.databinding.AcSpiBinding
import java.util.ServiceLoader

/**
 * @author: FiDo
 * @date: 2024/11/15
 * @des:  Service Provider Interface
 * 什么是 SPI（Service Provider Interface）？
 * SPI 是一种常见的设计模式，定义了一个接口或抽象类，并允许多个不同的实现类根据具体需求来提供实现。这种模式的主要目的是让开发者在应用运行时动态地加载这些实现类，而不需要硬编码具体的类名和实现细节。
 *
 * SPI 通常用于：
 *
 * 扩展性：通过 SPI，系统可以在运行时动态加载服务实现。
 * 插件架构：插件可以通过 SPI 机制加入到应用中，不需要修改现有代码。
 * 在 Android 中的应用场景
 * 在 Android 中，SPI 可以用于以下场景：
 *
 * 动态加载模块：例如，通过 SPI 可以实现 Android 应用中的动态模块加载，允许应用在运行时根据需要加载不同的功能模块。
 * 插件架构：Android 应用可以使用 SPI 作为插件机制，插件可以提供特定功能，而主应用负责发现和加载这些插件。
 * 多种服务提供者选择：对于一些通用的功能（例如图像处理、网络请求、数据库存储等），SPI 允许应用根据实际需求选择合适的服务实现，而不需要修改主应用代码。
 */
class SPIActivity:AppCompatActivity() {

    private val binding:AcSpiBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            loadService.click {
//                ServiceLoader.load()
            }
        }
    }

}