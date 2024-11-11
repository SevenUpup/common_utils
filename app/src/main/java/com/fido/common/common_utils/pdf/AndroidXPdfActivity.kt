package com.fido.common.common_utils.pdf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.fido.common.R
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.databinding.AcAndroidxPdfBinding

/**
 * @author: FiDo
 * @date: 2024/9/26
 * @des:  minSdkVersion35 需要Android15 ？？？？
 */
class AndroidXPdfActivity:AppCompatActivity() {

    private val binding:AcAndroidxPdfBinding by binding()
//    private var pdfViewerFragment:PdfViewerFragment?=null
    private val PDF_VIEWER_FRAGMENT_TAG = "PDF_VIEWER_FRAGMENT_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setPdfView()

        binding.apply {
            tv.text = "implementation(\"androidx.graphics:graphics-shapes:1.0.0-beta01\")"
        }
        /*binding.apply {
            pdfViewerFragment?.documentUri = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf".toUri()
            //搜索功能
            pdfViewerFragment?.isTextSearchActive = true
        }*/

        initView()

    }

    private fun initView() {
        binding.btAndroidxShape
    }

//    private fun setPdfView() {
//            val fragmentManager: FragmentManager = supportFragmentManager
//
//            pdfViewerFragment = PdfViewerFragment()
//            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//
//            transaction.replace(
//                R.id.fragment_container,
//                pdfViewerFragment!!,
//                PDF_VIEWER_FRAGMENT_TAG
//            )
//            transaction.commitAllowingStateLoss()
//            fragmentManager.executePendingTransactions()
//    }
    
}