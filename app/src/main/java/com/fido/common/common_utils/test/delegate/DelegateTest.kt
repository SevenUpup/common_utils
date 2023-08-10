package com.fido.common.common_utils.test.delegate

/**
@author FiDo
@description:
@date :2023/8/8 9:28
 */
class DelegateTest {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {

            val delegate = DelegateImp()
            delegate.sayHello()

            val delegate2 = RealDelegateImp(delegate)
            delegate2.sayHello()

            RealDelegateImp2().sayHello()

            RealDelegateImp3(DelegateImp()).sayHello()

            val list = listOf("one","two","three")
            val listNum = listOf(1,2,3)
            val listNum2 = listOf(1,2)
            println(list.associateWith { list.indexOf(it) })
            println(list.zip(listNum))
            println(list zip listNum2) //中缀表达式方式

            //unzip函数
            val numPairs:List<Pair<String,Int>> = listOf("one" to 1,"two" to 2,"three" to 3)
            println(numPairs.unzip()) //  ([one, two, three], [1, 2, 3])
            println(numPairs.unzip().first) //[one, two, three]
            println(numPairs.unzip().second) //[1, 2, 3]

            println(list.associateBy(
                //自行设计key和value
                keySelector = { it.first() }, valueTransform = { it.length }) //{o=3, t=5}
            )
            println(list.associate { it.first() to it.length }) // {o=3, t=5}

            val containers = listOf(
                listOf("one", "two"),
                listOf("three", "four", "five")
            )
            println(containers.flatMap { subs ->
                listOf(subs.size)  //[2, 3]
            })

            //filter To相关操作
            val filterToList = mutableListOf("1","2")
            val fliterToResult =  list.filterTo(filterToList) { it.length > 3 }
            println("filterToList=$filterToList fliterToResult=${fliterToResult} list=${list}")

            val numbers = listOf("one", "two", "three", "three")
            val plusNumbers = numbers + "four"
            println(numbers) //原始集合：[one, two, three, three]
            println(plusNumbers) //集合加操作：[one, two, three, three, four]

            val minusNum1 = numbers - listOf("three")
            val minusNum2 = numbers - "three"
            println(minusNum1)//集合减操作1：[one, two]
            println(minusNum2) //集合减操作2：[one, two, three]
            //注意：minus操作，如果第二个操作数是一个元素，那么 minus 移除其在原始集合中的 第一次 出现；
            // 如果是一个集合，那么移除其元素在原始集合中的 所有 出现。

            val numbers2 = listOf("one", "two", "three")
            //groupBy() 使用一个 lambda 函数并返回一个 Map。 在此 Map 中，每个键都是 lambda 结果，而对应的值是返回此结果的元素 List。
            //在带有两个 lambda 的 groupBy() 结果 Map 中，由 keySelector 函数生成的键映射到值转换函数的结果，而不是原始元素。
            println("------------groupBy-----------")
            println(numbers2.groupBy { it.first() })//{o=[one], t=[two, three]}
            println(
                numbers2.groupBy(keySelector = { it.length }, valueTransform = { it })
                //{3=[one, two], 5=[three]}
            )
            println("------------groupingBy-----------")
            //https://www.kotlincn.net/docs/reference/collection-grouping.html
            println(numbers2.groupingBy { it.first() }.eachCount()) //{o=1, t=2}

            println("生成随机集合-->shuffled list=${numbers.shuffled()} listNum maxOrNull=${listNum.maxOrNull()}")
        }
    }
}

//kotlin 委托
class RealDelegateImp2:ISay by DelegateImp()  //方式一
class RealDelegateImp3(val delegate: ISay) :ISay by delegate

interface ISay{
    fun sayHello()
}

class DelegateImp:ISay{
    override fun sayHello() {
        println("delegate say hello")
    }
}

/**
 *  delegate传入DelegateImp()
    RealImp 类将 sayHello() 方法的实现委托给了 DelegateImp 对象，从而实现了代码复用和模块化
    当调用 RealImp 的 sayHello() 方法时，实际上是调用了 DelegateImp 对象的 sayHello()方法。
 */
class RealDelegateImp(val delegate:ISay):ISay{
    override fun sayHello() {
        delegate.sayHello()
    }
}
