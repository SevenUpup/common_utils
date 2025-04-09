package com.fido.common.common_utils.js

import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.throttleClick
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_base_util.toJson
import com.fido.common.databinding.AcJsInteractionBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author: FiDo
 * @date: 2025/4/1
 * @des:  参考: https://juejin.cn/post/6844903479727620104?searchId=202504011033289D5D2E35782E1ACB62EF
 */
class JsAc:AppCompatActivity() {

//    private val vm :BaseViewModel by viewModels()

    private val binding:AcJsInteractionBinding by binding()
    private var job:Job?=null

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        pressBackTwiceToExitApp("再按一次退出")

        job = lifecycleScope.launch {

        }

        binding.apply {
            mWeb.loadUrl("file:///android_asset/js_test.html")

            with(mWeb.settings) {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }

            btAlert.throttleClick {
                post {
                    mWeb.loadUrl("javascript:callJS()")
                }
            }

            btEvaluate.throttleClick {
                post {
                    mWeb.evaluateJavascript("callJS()") {
                            value -> loge("onReceiveValue=${value}")
                    }
                }
            }

            initJSCallAndroid()

            mWeb.webChromeClient = object : WebChromeClient() {

                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    toast("onJsAlert 覆写后的自定义弹窗")
//                    showNormalConfirmDialog(title = "Android调通JS方法","这是 WebChromeClient onJsAlert()方法中的自定义弹窗")
//                    return true
                    return super.onJsAlert(view, url, message, result)
                }

                // 参数 message:代表 prompt（）的内容（不是url）
                override fun onJsPrompt(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    defaultValue: String?,
                    result: JsPromptResult?
                ): Boolean {
                    // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                    // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                    //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                    val uri = Uri.parse(message)
                    if (uri.scheme == "js") {
                        if (uri.authority == "demo") {
                            val values = uri.queryParameterNames
                            toast("js 通过WebChromeClient onJsPrompt()拦截成功，参数为：${values}")
                            return true
                        }
                    }
                    return super.onJsPrompt(view, url, message, defaultValue, result)
                }

            }
        }

    }

    private fun initJSCallAndroid() {

        binding.apply {
            btAddJSInterface.throttleClick {
                mWeb.addJavascriptInterface(AndroidToJs(),"test")
                mWeb.loadUrl("file:///android_asset/js_test.html")
            }
            btShouldOverrideUrlLoading.throttleClick {
                val result = 0
                mWeb.loadUrl("javascript:returnResult(" + result + ")")
            }

            btWebChromeClient.throttleClick {

            }
            // 复写WebViewClient类的shouldOverrideUrlLoading方法
            mWeb.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    loge("url => ${request?.url?.toJson()}")
                    if (request?.url?.scheme == "js"){
                        // 如果url的协议 = 预先约定的 js 协议
                        if (request?.url?.authority == "webview"){
                            val parameter = request.url.queryParameterNames
                            toast("js 通过shouldOverrideUrlLoading 拦截成功，参数为：${parameter}")
                            tvParameters.text = parameter.toString()
                        }
                        return true
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

        }

    }

}


class AndroidToJs  {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    fun hello(msg: String?) {
        toast(msg)
    }
}