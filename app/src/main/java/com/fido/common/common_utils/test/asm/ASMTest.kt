package com.fido.common.common_utils.test.asm

import com.fido.common.common_utils.test.decorator.Mi2ProtableBattery
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileInputStream


/**
 * @author: FiDo
 * @date: 2024/4/9
 * @des:
 */
class ASMTest {


    private val AA = "AA"
    private val BB = "BB"

    val DD = 44

    val EE = true
    var CC = 66

    companion object{
        private var C = false
        private val D = true
        private val str = "str"
        private var mChar = "mChar"
        val Str = "str"
        var Char = "mChar"
        var GG = 66
        val DD = "11"

        @JvmStatic
        fun main(args: Array<String>) {
//            val path = getClassFilePath(User::class.java)
            val path = ASMJavaTest.getClassFilePath(TimeAsmView::class.java)
            println("path = $path")

            GG = 55

            println(Mi2ProtableBattery::class.java.canonicalName)
            val classReader = ClassReader(FileInputStream(path))
            val classNode = ClassNode(Opcodes.ASM9)
            classReader.accept(classNode,0)
            classNode.fields.forEach {
                println("visit field:${it.name} , desc = ${it.desc}")
            }
            classNode.methods.forEach {
                println("visit method:${it.name} , desc = ${it.desc}")
            }
//            val classVisitor: ClassVisitor = object : ClassVisitor(Opcodes.ASM9) {
//                override fun visitField(
//                    access: Int,
//                    name: String,
//                    descriptor: String,
//                    signature: String,
//                    value: Any
//                ): FieldVisitor {
//                    println("visit field:$name , desc = $descriptor")
//                    return super.visitField(access, name, descriptor, signature, value)
//                }
//
//                override fun visitMethod(
//                    access: Int,
//                    name: String,
//                    descriptor: String,
//                    signature: String,
//                    exceptions: Array<String>
//                ): MethodVisitor {
//                    println("visit method:$name , desc = $descriptor")
//                    return super.visitMethod(access, name, descriptor, signature, exceptions)
//                }
//            }
//            classReader.accept(classVisitor,0)
        }

        fun getClassFilePath(clazz: Class<*>): String? {
            // file:/Users/zhy/hongyang/repo/BlogDemo/app/build/intermediates/javac/debug/classes/
            val buildDir = clazz.protectionDomain.codeSource.location.file
            val fileName = clazz.simpleName + ".class"
            println("buildDir =$buildDir fileName=$fileName")
            val file = File(
                buildDir + clazz.getPackage().name.replace("[.]".toRegex(), "/") + "/",
                fileName
            )
            return file.absolutePath
        }

    }

}