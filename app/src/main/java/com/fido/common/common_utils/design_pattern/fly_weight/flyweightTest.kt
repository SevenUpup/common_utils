package com.fido.common.common_utils.design_pattern.fly_weight

/**
 * @author: FiDo
 * @date: 2024/11/14
 * @des:  享元模式
 */

internal fun main(){
    val factory = CharacterFactor()
    val a1 = factory.getCharacter("A")
    val b1 = factory.getCharacter("B")
    val a2 = factory.getCharacter("A")
    val b2 = factory.getCharacter("B")

    println("a1=${a1} b1 =${b1} a2=${a2} b2=${b2}")
}

//创建享元接口
internal interface Character{
    fun display()
}

internal class ConcreteCharacter(
    private val symbol:String
):Character{
    override fun display() {
        println("display symbol = $symbol")
    }
}

internal class CharacterFactor{
    private val characterMap = mutableMapOf<String,Character>()

    fun getCharacter(symbol:String):Character?{
        if (!characterMap.containsKey(symbol)) {
            characterMap.put(symbol,ConcreteCharacter(symbol))
        }
        return characterMap[symbol]
    }

}
