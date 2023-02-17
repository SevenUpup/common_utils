package com.fido.common.common_base_util.util.toast.interfaces

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.fido.common.common_base_util.R

/**
 * @param gravity - 对齐方式
 * @param layoutRes - 吐司布局, 要求消息内容TextView的ID使用@android:id/message
 * @param xOffset - x轴偏移量
 * @param yOffset - y轴偏移量
 */
open class ToastGravityFactory @JvmOverloads constructor(
    private val gravity:Int = Gravity.CENTER,
    @LayoutRes private val layoutRes: Int = R.layout.layout_toast_gravity,
    private val xOffset :Int = 0,
    private val yOffset :Int = 0,
):ToastFactory {

    override fun onCreate(context: Context, msg: CharSequence, duration: Int, tag: Any?): Toast? {
        val toast = Toast.makeText(context,msg,duration)
        val view = View.inflate(context,layoutRes,null)
        toast.view = view
        view.findViewById<TextView>(android.R.id.message).text = msg
        toast.setGravity(gravity,xOffset, yOffset)
        return toast
    }
}