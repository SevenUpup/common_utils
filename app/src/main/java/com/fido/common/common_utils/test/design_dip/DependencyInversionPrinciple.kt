package com.fido.common.common_utils.test.design_dip

/**
 * @author: HuTao
 * @date: 2026/7/10
 * @des:  依赖倒置 测试
 */

/**
 * 定义一个抽象的本地存储接口
 */
interface LocalStorage {
    fun init(context: String)
    fun get(key: String): String?
    fun put(key: String, value: String)
}

class SpStorage:LocalStorage{
    override fun init(context: String) {
        println("SpStorage init context: $context")
    }

    override fun get(key: String): String? {
        return "get $key from SharedPreferences value=${key}_value"
    }

    override fun put(key: String, value: String) {
        println("put $key to SharedPreferences value=$value")
    }
}

class MMKVStorage:LocalStorage{
    override fun init(context: String) {
        println("MMKVStorage init context: $context")

    }
    override fun get(key: String): String? {
        return "get $key from MMKV value=${key}_value"
    }

    override fun put(key: String, value: String) {
        println("put $key to MMKV value=$value")
    }
}

object LocalStorageManager {

    private var localStorage: LocalStorage?=null

    fun init(context: String, localStorage: LocalStorage = SpStorage()){
        this.localStorage = localStorage
        localStorage.init(context)
    }

    fun get(key: String): String? {
        return localStorage?.get(key) ?: throw RuntimeException("LocalStorage is not initialized,call init first")
    }
    fun put(key: String, value: String) {
        localStorage?.put(key, value) ?: throw RuntimeException("LocalStorage is not initialized,call init first")
    }
}

fun main() {
    // 未初始化(init) 直接get 会 抛出异常
//    println("Default storage: ${LocalStorageManager.get("key")}")

    // 测试默认实现 (SharedPreferences)
    LocalStorageManager.init("context")
    println("Default storage: ${LocalStorageManager.get("key")}")

    // 测试 MMKV 实现
    LocalStorageManager.init("context", MMKVStorage())
    println("MMKV storage: ${LocalStorageManager.get("key_mmkv")}")

    // 测试存储操作
    LocalStorageManager.put("test_key", "test_value")
    println("Stored value: ${LocalStorageManager.get("test_key")}")

}
