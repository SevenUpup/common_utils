package com.fido.common.kotlin_object

import android.view.View

/**
 * Kotlin中 object 关键字
 *
 * 在 Kotlin 中，object declaration 和 object expression 虽然都依赖 object 关键字，但它们面向的是完全不同的使用场景。
 * 前者提供的是具名、惰性初始化、可全局共享的单例；后者提供的是匿名、立即创建、用于一次性任务的临时实例。
 */

//object declaration  具名、惰性初始化、可全局共享的单例
object KtObject {

    private fun setClick(view: View){
        //object expression  匿名、立即创建、用于一次性任务的临时实例
        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }
        })
    }

}

//KT version 1.9
//data object DataObject{
//    val name = "DataObject"
//}
