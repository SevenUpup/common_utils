package com.fido.common.common_utils.design_pattern.combine

/**
 * @author: FiDo
 * @date: 2024/11/13
 * @des:
 */
fun main(){

    // 创建文件和文件夹
    val file1: File1 = File1("File1.txt", 100)
    val file2: File1 = File1("File2.jpg", 500)
    val file3: File1 = File1("File3.doc", 200)

    val folder1 = DirectorySys("Folder1")
    val folder2 = DirectorySys("Folder2")

    // 将文件添加到文件夹
    folder1.addComponent(file1)
    folder1.addComponent(file2)


    // 将文件夹添加到另一个文件夹
    folder2.addComponent(file3)
    folder2.addComponent(folder1)

    // 展示文件夹内容
//    folder1.showDetail()
    folder2.showDetail()
}

internal interface FileSysComponent{
    fun showDetail()
}

internal class File1(
    val name:String,
    val size:Int
):FileSysComponent{

    override fun showDetail() {
        System.out.println("File1: " + name + ", Size: " + size + "KB");
    }

}


internal class DirectorySys(
    val name:String
):FileSysComponent{

    private val components = mutableListOf<FileSysComponent>()

    fun addComponent(component: FileSysComponent){
        this.components.add(component)
    }

    fun removeComponent(component: FileSysComponent){
        this.components.remove(component)
    }

    override fun showDetail() {
        System.out.println("Directory: " + name);
        components.forEach {
            it.showDetail()
        }
    }
}