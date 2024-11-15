package com.fido.common.common_base_ui.ext.edittext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.*

/**
@author FiDo
@description:
@date :2023/6/13 16:31
 */

/**
 * 使用第二种方式
 * usage 1. et.textChanges()
            .filterNot { it.isNullOrBlank() }
            .debounce(300)
            .flatMapLatest { //前面的看时间间隔进行结合 , 中间的可能跳过某些元素 , 不要中间值 , 只重视最新的数据
                search(it)
            }
            .onEach {
                toast("textChanges=${it}")
            }
            .launchIn(lifecycleScope)
        2. 直接用 throttleSearch
 */
private fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                trySend(s)
            }

            override fun afterTextChanged(s: Editable?) {
                trySend(s).onFailure { exception -> exception?.printStackTrace() }
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

/**
 * EditText 防抖搜索
 * @param debounceTime  指定时间内发送的只保留最后一个，其他将被丢弃，防抖用的
 * @param doSearchBlock 用于转成自己需要的结果block,一般可能从service 获取后convert成自己需要的Bean
 * @param collectBlock  处理结果，更新UI等
 * @param lifecycle     对应这里的flow协程的作用域，所以textChanges中的awaitClose或者作用域结束时被调用，即lifecycle执行 onDestroy()时
 */
fun <T> EditText.throttleSearch(
    lifecycle: Lifecycle,
    debounceTime:Long = 300,
    doSearchBlock:(searchKey:CharSequence?)->T,
    collectBlock:(T)->Unit,
){
    textChanges()
        .filterNot { it.isNullOrBlank() }
        .debounce(debounceTime)
        .flowOn(Dispatchers.IO)
        .flatMapLatest {
            flow<T> {
                emit(doSearchBlock(it))
            }
        }.onEach {
            collectBlock(it)
        }.launchIn(lifecycle.coroutineScope)
}

private fun EditText.onTextChangedFlow() = callbackFlow<Editable> {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            s?.let { value ->
                trySend(value)
//                    .onFailure { exception -> exception?.printStackTrace() }
            }
        }
    }

    addTextChangedListener(watcher)
    awaitClose { removeTextChangedListener(watcher) }
}