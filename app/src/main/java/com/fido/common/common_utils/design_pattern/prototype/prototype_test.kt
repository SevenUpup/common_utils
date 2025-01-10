package com.fido.common.common_utils.design_pattern.prototype

import com.fido.common.common_base_util.deepCopy
import kotlin.Cloneable
/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  原型模式
 * 原型模式（Prototype Pattern） 是一种创建型设计模式，它允许通过复制现有对象（原型）来创建新对象，而不是通过实例化一个类的方式。这样可以避免使用构造函数创建多个相似对象，减少代码的重复，并且提高对象创建的效率，尤其是当创建对象的成本较高时。该模式可以支持克隆复杂的对象结构，适用于一些需要根据现有对象创建新对象的场景。
 *
 * 原型模式的核心思想：
 * 复制对象：原型模式通过复制现有对象来创建新对象，而不是创建新的实例。
 * 避免重复创建：通过克隆现有对象，可以节省创建新对象的时间和开销，尤其是当对象的创建过程比较复杂时。
 * 浅拷贝与深拷贝：原型模式中的复制通常分为浅拷贝和深拷贝：
 * 浅拷贝：复制对象时仅复制基本数据类型的值，对于引用类型的属性，复制的是引用地址。
 * 深拷贝：复制对象时不仅复制基本数据类型的值，还会递归地复制引用类型的对象，确保新对象和原对象完全独立。
 * 原型模式的角色：
 * Prototype（原型接口）：定义了一个克隆方法（通常是 clone()），用于复制自身。
 * ConcretePrototype（具体原型类）：实现克隆方法，具体提供复制逻辑。
 * Client（客户端）：通过调用原型的克隆方法来创建新对象。
 */
fun main(){

    val originalStudent = Student("66")
    val copyStudent = originalStudent.copy()

    // 输出原型和克隆对象
    println("original Student: $originalStudent")
    println("Cloned Student: $copyStudent")

    // 验证两个对象是否是不同的实例
    println("Are both students the same object? ${originalStudent === copyStudent}")

    val student2 = Student2("77")
    val cloneStudent2 = student2.clone()
    println("student2=$student2 cloneStudent2=${cloneStudent2} is same = ${student2 === cloneStudent2}")

    println("Kotlin 的 data class 已内置 copy 方法，可直接用作原型模式的实现。")
    val person = Person("小明", Address("合肥"))
    val prototypePerson = person.copy()
    // 修改原型对象，验证深拷贝
    prototypePerson.name = "小艾"
    prototypePerson.address = Address("安庆")
    println(person)
    println(prototypePerson)

    val origin = Original("66", Address("南京"))
    val copy = origin.copy()
    copy.name = "77"
    copy.address = Address("武汉")
    println("original=$origin")
    println("copy=$copy")


    //浅拷贝？
    val original = Person1("Alice", 30, Address1("New York", "5th Avenue"))
    val copied = original.copy()

    println("Before modification:")
    println("Original: $original")
    println("Copied: $copied")

    // 修改引用类型字段
    copied.address.city = "Los Angeles"

    println("After modification:")
    println("Original: $original") // address.city 会被修改为 Los Angeles，说明是浅拷贝
    println("Copied: $copied")
}

data class Address1(var city: String, var street: String)
data class Person1(var name: String, var age: Int, var address: Address1) {
    fun deepCopy(): Person1 {
        return Person1(name, age, Address1(address.city, address.street))
    }
}

internal data class Original(var name: String,var address: Address)

internal data class Address(val city:String)
internal data class Person(var name: String,var address: Address):Cloneable{

    override fun clone(): Person {
        return copy()
    }
}

internal interface Prototype{
    fun copy():Prototype
}

internal class Student (
    val name:String
):Prototype{
    override fun copy(): Prototype {
        return Student(name)
    }
}


class Student2(var name: String) : Cloneable {
    override fun toString(): String {
        return "Student{" +
                "name='" + name + '\'' +
                '}'
    }

    //浅拷贝
    public override fun clone(): Student2 {
        try {
            return super.clone() as Student2
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }
        return Student2(name)
    }
}