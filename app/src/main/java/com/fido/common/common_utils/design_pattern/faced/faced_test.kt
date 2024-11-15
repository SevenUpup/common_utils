package com.fido.common.common_utils.design_pattern.faced

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des: 外观模式
 */

fun main(){
    var computer = Computer()
    computer.start()
    computer.shutdown()
}

// 一些子类
internal class CPU{
  fun start(){
      println("cpu start")
  }

    fun shutdown(){
        println("cpu shutdown")
    }
}

internal class RAM{
    fun start(){
        println("RAM start")
    }

    fun shutdown(){
        println("RAM shutdown")
    }
}

internal class ROM{
    fun start(){
        println("ROM start")
    }

    fun shutdown(){
        println("ROM shutdown")
    }
}

//外观类
internal class Computer{
    private var cup:CPU = CPU()
    private var ram:RAM = RAM()
    private var rom:ROM = ROM()

    fun start(){
        cup.start()
        ram.start()
        rom.start()
    }

    fun shutdown(){
        cup.shutdown()
        ram.shutdown()
        rom.shutdown()
    }
}