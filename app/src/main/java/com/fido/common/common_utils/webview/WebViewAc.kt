package com.fido.common.common_utils.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.loge
import com.fido.common.databinding.AcWebViewBinding

/**
 * @author: FiDo
 * @date: 2024/11/13
 * @des:
 */
class WebViewAc:AppCompatActivity() {

    private val binding:AcWebViewBinding by binding()

    private var _localHtmlName = ""  //存放在asset目录下的文件名

    companion object{
        const val INTENT_LOAD_LOCAL = "INTENT_LOAD_LOCAL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _localHtmlName = intent.getStringExtra(INTENT_LOAD_LOCAL)?:""
        binding.apply {
            if (_localHtmlName.isNotEmpty()) {
                mWeb.loadUrl("file:///android_asset/${_localHtmlName}")
                with(mWeb.settings){
                    javaScriptEnabled = true
                }
            }else{
                initWeb(mWeb)
            }
        }
    }

    private fun initWeb(mWeb: WebView) {
        with(mWeb){
            loadUrl("https://github.com/SevenUpup")

            webViewClient = object :WebViewClient(){
                //onPageCommitVisible在页面内容即将显示时调用，如果在这个阶段页面内容为空或不完整，可能是一个白屏的迹象
                //一种可能的方法是在onPageCommitVisible回调中使用evaluateJavascript来检查页面的DOM结构
                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)

                    evaluateJavascript("(function() { return document.body.innerHTML; })();"
                    ) { value ->
                        if (value.isNullOrEmpty()) {
                            // 处理白屏情况
                            Toast.makeText(
                                context,
                                "Page load failed or is empty",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 例如：重新加载网页
                            reload()
                            loge("HTML: $value 开始reload")
                        } else {
                            loge("HTML: $value")
                        }
                    }
                }
            }
        }
    }

}