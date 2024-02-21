package com.fido.common.common_utils.test.java.generic

import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.fido.common.common_base_util.app


/**
 * @author: FiDo
 * @date: 2024/2/20
 * @des:  泛型测试
 */
class GenericTest {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val b = B<CharSequence>()
            b.set("abc")
            b.set(StringBuilder().append("def"))

        }
    }
}

// 泛型逆变/协变测试
class A<out T>(_item:T){
    private var item:T = _item

    fun get():T = item

}

class B<in T>(){

    fun set(t:T){
        println("你传入的是${t}")
    }

}