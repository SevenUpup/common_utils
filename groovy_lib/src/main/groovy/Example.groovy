
class Example {
    static void main(String[] args) {
        println "Hello Groovy"
        println 'Hellow World'

        def result = noReturnTypeFun()
        println("result=$result")

        def strLen = getStrLen('hehaha')
        println strLen

        // =================== list ==================
        println('================= list ===============')
        def list = [1, "1", true]
        //list元素赋值，不用担心数组越界，如果超过长度list会自动向该索引填充
        list[4] = "tt"
        //遍历list
        for (a in list) {
            print a
        }

        // ================= map ====================
        println('================= map ===============')

        def map = [key: "测试", key2: true, key3: 456]
        println(URLEncoder.encode(map.key, "utf-8"))
        println(map['key2'])
        println(map['key3'])
        //定义新的map的key
        map.key5 = false
        map.anotherkey = "another"
        println map.anotherkey

        def aa = "测试"
        def map1 = [aa: "11", (aa): "22"]
        println map1.aa
        println map1.(aa)
        println(map1."测试")

        // ================ range =================
        println("=============range================")
        //包含12345
        def range = 1..5
        println range.getFrom()
        println range.getTo()
        //包含1234个元素
        def range2 = 1..<5
        println range2.getFrom()
        println(range2.getTo())

        //闭包
        def closure = { String param1, int params2 ->
            println param1 + params2
            //返回值
            "return type value"
        }
        closure.call("groovy",22)
        println(closure("hello",11))

        def closure2 = {
            println("hello $it")
        }

        text(1,"66"){
            println "text fun result => $it"
        }

        println("-------------running translate 脚本--------------")


    }

    static def text(int a,String b,Closure closure){
        def ab = a+b
        closure.call(ab)
    }

    static def noReturnTypeFun() {
        //最后一行代码的执行结果就是本函数的返回值
        1
    }

    //指定返回类型
    static int getStrLen(String str) {
        return str.length()
    }
}