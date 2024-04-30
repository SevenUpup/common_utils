package com.fido.common.common_utils.test.asm.apitest

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import java.lang.reflect.Modifier

/**
 * @author: FiDo
 * @date: 2024/4/26
 * @des:
 */
class ReadArrayListCoreApi {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            readArrayListByCoreApi()
        }

        private fun readArrayListByCoreApi() {
            val classReader = ClassReader(ArrayList::class.java.canonicalName)
            println(ArrayList::class.java.canonicalName)
            val classWrite = ClassWriter(classReader,ClassWriter.COMPUTE_FRAMES)
            classReader.accept(object :ClassVisitor(Opcodes.ASM9,classWrite){
                override fun visit(
                    version: Int,
                    access: Int,
                    name: String?,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<out String>?
                ) {

                    println("$version ${Modifier.toString(access)} $name $signature $superName ${interfaces?.joinToString(", ")}")
                    super.visit(version, access, name, signature, superName, interfaces)
                }

                override fun visitField(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    value: Any?
                ): FieldVisitor {
                    println("${Modifier.toString(access)} $name $descriptor $signature $value")
                    return super.visitField(access, name, descriptor, signature, value)
                }

                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?
                ): MethodVisitor {
                    println("${Modifier.toString(access)} $name $descriptor $signature ${exceptions?.joinToString(", ")}")
                    return super.visitMethod(access, name, descriptor, signature, exceptions)
                }

                override fun visitEnd() {
                    println("visitEnd")
                    super.visitEnd()
                }

            },ClassReader.SKIP_CODE)
        }
    }

}