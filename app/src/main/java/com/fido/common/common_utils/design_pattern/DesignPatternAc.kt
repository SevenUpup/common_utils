package com.fido.common.common_utils.design_pattern

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_ui.util.showNormalConfirmDialog
import com.fido.common.common_base_util.ext.click
import com.fido.common.common_base_util.ext.logd
import com.fido.common.common_base_util.ext.toast
import com.fido.common.common_utils.design_pattern.strategy.RuleExecute
import com.fido.common.databinding.AcDesignPatternBinding
import java.io.File

/**
 * @author: FiDo
 * @date: 2024/6/14
 * @des:
 */
class DesignPatternAc:AppCompatActivity() {

    private val binding:AcDesignPatternBinding by binding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jobCheck = JobCheck(
            "S9876543A", "Singapore", "Singapore", 16f,
            3.5F, false, false,
            "Chinese", 1, "25", true,
            false
        )

        binding.apply {
            btStrategyMode.click {
                //疫苗检查单
                val covidRule = COVIDRule {
                    showNormalConfirmDialog("提示","你没有新冠表单,马上去做？", onConfirmBlock = {
                        jobCheck.hasCovidTest = true
                        pushButtonAgain()
                    })
                }
                // 大于18 true
                val ageRule = AgeRule{
                    showNormalConfirmDialog("提示","年轻人，你还是太年轻了,想要立刻变老吗？", onConfirmBlock = {
                        jobCheck.age = 19f
                        pushButtonAgain()
                    })
                }

                val genderRule = GenderRule()   //true

                //信息是否完善
                val fillProfileRule = FillProfileRule{
                    showNormalConfirmDialog("提示","你没有完善用户信息,去完善吗？", onConfirmBlock = {
                        jobCheck.isFillProfile = true
                        pushButtonAgain()
                    })

                }  //false

                val languageRule = LanguageRule(listOf("Chinese", "English"))   //true
                val nationalityRule = NationalityRule(listOf("China", "Malaysia"))   //true
                val visaRule = VisaRule()   //true


                val build = RuleExecute
                    .create(jobCheck)
                    .and(listOf(covidRule))
                    .or(listOf(languageRule, nationalityRule))
//                    .and(listOf(ageRule, genderRule, fillProfileRule))
                    .or(listOf(ageRule, genderRule, fillProfileRule))
                    .and(listOf(visaRule))
                    .build()
                val ruleResult = build.execute()
                if (ruleResult.success) {
                    toast("恭喜你完成了全部考验，现在前往下一关吧")
                } else {
                    toast("执行规则器的结果：$ruleResult")
                }
            }

            btLoadApk.click {
                startDownLoad()
            }
        }
    }

    private fun pushButtonAgain() = toast("再点一次刚才的按钮")

    private fun startDownLoad() {
        //下载链接 这里下载手机B站为示例
        val downloadUrl = "https://dl.hdslb.com/mobile/latest/iBiliPlayer-html5_app_bili.apk"

        val fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)
        //这里下载到指定的目录，我们存在公共目录下的download文件夹下
        val fileUri = Uri.fromFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                System.currentTimeMillis().toString() + "-" + fileName
            )
        )
        //开始构建 DownloadRequest 对象
        val request = DownloadManager.Request(Uri.parse(downloadUrl))

        //构建通知栏样式
        request.setTitle("测试下载标题")
        request.setDescription("测试下载的内容文本")

        //下载或下载完成的时候显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        //指定下载的文件类型为APK
        request.setMimeType("application/vnd.android.package-archive")
//            request.addRequestHeader()   //还能加入请求头
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)   //能指定下载的网络

        //指定下载到本地的路径(可以指定URI)
        request.setDestinationUri(fileUri)

        //开始构建 DownloadManager 对象
        val downloadManager = this.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        //加入Request到系统下载队列，在条件满足时会自动开始下载。返回的为下载任务的唯一ID
        val requestID = downloadManager.enqueue(request)

        //注册下载任务完成的监听
        this.registerReceiver(object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                //已经完成
                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                    //获取下载ID
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    val uri = downloadManager.getUriForDownloadedFile(id)
                    logd("下载完成了- uri:$uri")

                    installApk(uri)

                } else if (intent.action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

                    //如果还未完成下载，跳转到下载中心
                    logd("跳转到下载中心")
                    val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(viewDownloadIntent)

                }

            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    /*
    * 需要  <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    * */
    private fun installApk(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(intent)
    }


}