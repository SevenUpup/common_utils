package com.fido.utils

import java.io.File
import java.io.IOException

/**
 * @author: FiDo
 * @date: 2024/6/12
 * @des:
 */
object FileUtils {

    /**
     * Write text to a file.
     *
     * @param text The text to write to the file.
     * @param filePath The path where the file will be saved.
     * @return true if the file was written successfully, false otherwise.
     */
    fun writeTextToFile(text: String, filePath: String): Boolean {
        return try {
            val file = File(filePath)
            file.writeText(text)
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get the desktop path of the current user.
     *
     * @return The path to the desktop.
     */
    fun getDesktopPath(): String {
        return System.getProperty("user.home") + File.separator + "Desktop"
    }

    /**
     * Write text to a file on the desktop.
     *
     * @param text The text to write to the file.
     * @param fileName The name of the file.
     * @return true if the file was written successfully, false otherwise.
     */
    fun writeTextToDesktop(text: String, fileName: String): Boolean {
        val desktopPath = getDesktopPath()
        val filePath = desktopPath + File.separator + fileName
        return writeTextToFile(text, filePath)
    }
}

/*
fun main() {
    val textToWrite = "Hello, this is a test text."

    // Save to desktop
    val fileName = "testFile.txt"
    val isSavedToDesktop = FileUtils.writeTextToDesktop(textToWrite, fileName)
    println("File saved to desktop: $isSavedToDesktop")

    // Save to specified path
    val specifiedPath = "/path/to/your/directory/testFile.txt"
    val isSavedToSpecifiedPath = FileUtils.writeTextToFile(textToWrite, specifiedPath)
    println("File saved to specified path: $isSavedToSpecifiedPath")
}*/
