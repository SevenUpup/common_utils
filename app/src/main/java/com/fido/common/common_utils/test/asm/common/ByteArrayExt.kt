package com.fido.common.common_utils.test.asm.common

import java.io.File
import java.io.FileOutputStream

/**
 * @author: FiDo
 * @date: 2024/4/29
 * @des:
 */
fun ByteArray.toFile(fileName:String){
    runCatching {
        val fos = FileOutputStream(fileName)
        fos.write(this)
        fos.flush()
        fos.close()
    }
}

internal fun ByteArray.loadClass(fileName: String): Class<*>? {
    return Class.forName(fileName, true, Loader(this))
}

internal class Loader(private val bytes: ByteArray) : ClassLoader() {

    override fun findClass(name: String?): Class<*> {
        return defineClass(name, bytes, 0, bytes.size)
    }
}

internal val getRootPath
    get() = File("").absolutePath