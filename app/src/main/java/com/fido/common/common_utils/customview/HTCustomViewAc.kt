package com.fido.common.common_utils.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.click
import com.fido.common.databinding.AcHtCustomViewBinding

/**
 * @author: FiDo
 * @date: 2025/3/4
 * @des:  自定义View测试
 */
class HTCustomViewAc:AppCompatActivity() {

    private val binding:AcHtCustomViewBinding by binding()

    private val mText = """
        Hello, this is a typewriter effect using Kotlin Coroutines and Flow!
        Hello, this is a typewriter effect using Kotlin Coroutines and Flow!
        Hello, this is a typewriter effect using Kotlin Coroutines and Flow!
    """.trimIndent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            btStart.click {
                printTextHandler.setTextWithAnim(mText)
                printerTextFlow.setTextWithAnimation(mText)
            }

            btStop.click {
                printTextHandler.printTextBack()
                printerTextFlow.pauseAnimation()
            }

            printTextHandler.apply {
                setTextWithAnim(mText)
            }

        }
    }

}