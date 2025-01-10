package com.fido.common.common_utils.design_pattern.memento

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  备忘录模式
 */

fun main(){

    //创建一个原始类
    val original = Original("111")
    //创建一个存储类存储备忘录
    val storage = Storage(original.createMemento())
    original.status = "222"
    //恢复原始状态
    original.restoreMemento(storage.memento)
    println(original.status)

    val editor = TextEditor()
    val history = TextEditorHistory()

    // 设置文本并保存备忘录
    editor.setText("Hello")
    println("Current Text: ${editor.getText()}")
    history.addMemento(editor.createMemento())

    // 修改文本
    editor.setText("Hello, World!")
    println("Current Text: ${editor.getText()}")
    history.addMemento(editor.createMemento())

    // 修改文本
    editor.setText("Hello, Kotlin!")
    println("Current Text: ${editor.getText()}")

    // 撤销到上一个状态
    editor.restore(history.getLastMemento()!!)
    println("After Undo: ${editor.getText()}")

    // 再次撤销
    editor.restore(history.getLastMemento()!!)
    println("After Undo Again: ${editor.getText()}")


}

//发起人，可以创建和恢复备忘录,并且有自己的状态
internal class TextEditor{

    private var text:String = ""

    fun createMemento():MementoText{
        return MementoText(text)
    }

    fun restore(memento: MementoText){
        text = memento.text
    }

    fun getText() = text

    fun setText(text: String){
        this.text = text
    }
}
//备忘录
internal class MementoText(val text:String){}
//管理者
internal class TextEditorHistory{
    private val history = mutableListOf<MementoText>()
    //保存备忘录
    fun addMemento(memento: MementoText){
        history.add(memento)
    }
    //获取最近的备忘录
    fun getLastMemento():MementoText?{
        return if (history.isNotEmpty()) history.removeAt(history.size-1) else null
    }
}

//创建一个备忘录
class Memento(val status:String){}

//创建一个原始类
class Original(
    var status: String
){

    fun createMemento():Memento{
        return Memento(status)
    }

    fun restoreMemento(memento: Memento){
        status = memento.status
    }

}

//一个存储备忘录的类
internal class Storage(val memento: Memento){}