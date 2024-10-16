package com.fido.common.common_utils.ac_factory2

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.fido.common.common_base_ui.base.viewbinding.binding
import com.fido.common.common_base_util.ext.loge
import com.fido.common.common_utils.ac_factory2.view.MyTextView
import com.fido.common.databinding.AcLayoutFactory2Binding

/**
 * @author: FiDo
 * @date: 2024/10/8
 * @des:
 */
class LayoutFactory2Ac:AppCompatActivity() {

    private val binding:AcLayoutFactory2Binding by binding()

    private var _ttf:Typeface?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this),object :LayoutInflater.Factory2{
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                loge("name=$name")
                //自定义判断，替换/新建
                if (name == "TextView") { //直接将xml中的 TextView 解析后转成Button
                    return Button(context,attrs)
                }
                if (name == "androidx.appcompat.widget.AppCompatTextView") {
                    return MyTextView(context,attrs)
                }

                //系统创建appcompat控件，会有耗时，在上面直接替换或新建会快点，算是一种xml的布局优化
                val view = delegate.createView(parent, name, context, attrs)

                //统一字体更换测试
                if (_ttf == null) {
                    _ttf = Typeface.createFromAsset(assets,"Eracake.ttf")
                }
                if (view is TextView) {
                    view.setTypeface(_ttf)
                }

                return view
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }

        })

        super.onCreate(savedInstanceState)

        binding.apply {

        }
    }

}