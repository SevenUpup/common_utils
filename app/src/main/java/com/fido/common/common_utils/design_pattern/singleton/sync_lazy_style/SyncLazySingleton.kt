package com.fido.common.common_utils.design_pattern.singleton.sync_lazy_style

/**
 * @author: FiDo
 * @date: 2024/6/25
 * @des:  带同步锁的 饿汉式单例
 */
class SyncLazySingleton private constructor(){

    companion object{
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SyncLazySingleton()
        }
    }

}