package com.fido.common.common_utils.design_pattern.delegate.property_delegate

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author: FiDo
 * @date: 2024/6/26
 * @des:  属性委托测试
 */
class PropertyDelegateTest {


    val textStr by TextDelegate()
    var textStr2 by TextDelegate()

    var textStr3 by TextDelegate2()
    var textStr4 by this::textStr3

    //延迟委托
    val name: String by lazy {
        println("第一次调用初始化")
        "abc123"
    }

    //观察者委托
    var observableValue:String by Delegates.observable("初始值"){property, oldValue, newValue ->
        println("打印值: $oldValue -> $newValue ")
    }
    //还能使用 vetoable 委托，和 observable 一样可以观察属性的变化，不同的是 vetoable 可以决定是否使用新值
    var age: Int by Delegates.vetoable(18) { property, oldValue, newValue ->
        newValue > oldValue
    }


}

class TextDelegate {
    operator fun getValue(thisRef: Any?,property:KProperty<*>):String{
        return "我是代理类赋值的文本"
    }

    operator fun setValue(thisRef: Any?,property: KProperty<*>,value:Any){
        println("设置的值为${value}  thisRef=${thisRef}  property=${property.name}")
    }

}

class TextDelegate2 : ReadWriteProperty<Any,String>{
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return "TextDelegate2${thisRef}"
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        println("TextDelegate2  thisRef=${thisRef}  property=${property.name}  value=${value}")
    }

}
