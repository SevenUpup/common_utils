package com.fido.common.common_utils.test.asm.apitest

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.lang.reflect.Modifier

/**
 * @author: FiDo
 * @date: 2024/4/29
 * @des:
 */
class ReadArrayListTresApi {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            readArrayListTresApi()
        }

        private fun readArrayListTresApi() {
            val classReader = ClassReader(ArrayList::class.java.canonicalName)
            val classNode = ClassNode(Opcodes.ASM9)
            classReader.accept(classNode,ClassReader.SKIP_CODE)
            classNode.apply {
                println("name = $name")
                fields.forEach {
                    println("field:${it.name} ${it.access.accessName} ${it.desc} ${it.value}")
                }
                println("module = ${this.module}")
                methods.forEach {
                    println("method:${it.name} ${it.access.accessName} ${it.desc} ${it.instructions}")
                }
            }
        }
    }
}

val Int.accessName: String
    get() = Modifier.toString(this)
