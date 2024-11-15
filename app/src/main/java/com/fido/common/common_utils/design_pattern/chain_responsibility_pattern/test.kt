package com.fido.common.common_utils.design_pattern.chain_responsibility_pattern

/**
 * @author: FiDo
 * @date: 2024/11/13
 * @des:  责任链模式/链式责任模式
 */

fun main() {

    val errorLogger = ErrorLogger(Logger.LEVEL_ERROR)
    val debugLogger = DebugLogger(Logger.LEVEL_DEBUG)
    val consoleLogger = ConsoleLogger(Logger.LEVEL_INFO)

    errorLogger.setNextLogger(debugLogger)
    debugLogger.setNextLogger(consoleLogger)

    errorLogger.log(Logger.LEVEL_INFO, "This is an information.")
    errorLogger.log(Logger.LEVEL_DEBUG, "This is a debug level information.")
    errorLogger.log(Logger.LEVEL_ERROR, "This is an error information.")
}

internal abstract class Logger{
    protected var _level = 0
    protected var _nextLogger:Logger?=null

    companion object{
        const val LEVEL_INFO = 1
        const val LEVEL_DEBUG = 2
        const val LEVEL_ERROR = 3
    }

    fun setNextLogger(logger: Logger){
        this._nextLogger = logger
    }

    fun log(level:Int,msg:String){
        if (_level <= level) {
            write(msg)
        }
        if (_nextLogger != null) {
            _nextLogger?.log(level, msg)
        }
    }

    abstract fun write(msg:String)
}

internal class ConsoleLogger(
    val level:Int
) :Logger(){

    init {
        _level = level
    }

    override fun write(msg: String) {
        println("Console Logger $msg")
    }

}

internal class DebugLogger(
    val level:Int
) :Logger(){

    init {
        _level = level
    }

    override fun write(msg: String) {
        println("Debug Logger $msg")
    }
}

internal class ErrorLogger(
    val level:Int
) :Logger(){

    init {
        _level = level
    }

    override fun write(msg: String) {
        println("Error Logger $msg")
    }
}


