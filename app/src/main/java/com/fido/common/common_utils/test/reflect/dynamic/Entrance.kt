package com.fido.common.common_utils.test.reflect.dynamic

class Entrance {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val iOperation = ProxyFactory.getInstance(RealObject()) as IOperation
            iOperation.operation()

            val iOperation2 = ProxyFactory.getInstance(RealObject2()) as IOperation2
            iOperation2.operation("just do it!")
        }
    }

}