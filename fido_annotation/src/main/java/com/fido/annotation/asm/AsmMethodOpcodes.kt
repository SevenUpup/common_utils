package com.fido.annotation.asm

import org.objectweb.asm.Opcodes

object AsmMethodOpcodes {

    const val INVOKESTATIC = Opcodes.INVOKESTATIC
    const val INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL
    const val INVOKESPECIAL = Opcodes.INVOKESPECIAL
    const val INVOKEDYNAMIC = Opcodes.INVOKEDYNAMIC
    const val INVOKEINTERFACE = Opcodes.INVOKEINTERFACE
}