package com.fido.common.common_utils.design_pattern.a_kotlin

import java.io.PrintStream

/**
 * @author: FiDo
 * @date: 2025/3/11
 * @des:
 */

fun systemPrinterUtf8(){
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setErr(PrintStream(System.err, true, "UTF-8"))
}