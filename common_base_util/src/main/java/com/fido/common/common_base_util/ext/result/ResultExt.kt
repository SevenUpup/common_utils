package com.fido.common.common_base_util.ext.result

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author: FiDo
 * @date: 2024/9/18
 * @des:
 */

// 自定义异常，表示内部出错了
class InnerException(message: String = "inner value error") : Exception(message)

//一对一依赖
//一对一依赖简单得说就是，方法B依赖方法A的返回值。最开始的例子就可以说是一对一依赖关系
@OptIn(ExperimentalContracts::class)
public inline fun <V, E> Result<V>.andThen(transform: (V) -> Result<E>): Result<E> {
    // kotlin 约定，告诉编译器 transform 最多执行一次
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess) {
        val value = getOrNull() ?: return Result.failure(InnerException())
        return transform(value)
    } else {
        val exception = exceptionOrNull() ?: return Result.failure(InnerException())
        return Result.failure(exception)
    }
}

//一对多依赖
//一对多依赖简单说就是，方法B和方法C依赖方法A的返回值。比如说，你需要看你各个平台账号的视频播放量，这时就需要先获取你的手机号（一般来说平台账号就是手机号），再通过手机号获取不同平台的播放量，最后相加获得
@OptIn(ExperimentalContracts::class)
public inline fun <V, E> Result<V>.dispatch(transform: (V) -> E): Result<E> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }

    if (isSuccess) {
        val value = getOrNull() ?: return Result.failure(InnerException())
        return kotlin.runCatching {
            transform(value)
        }
    } else {
        val exception = exceptionOrNull() ?: return Result.failure(InnerException())
        return Result.failure(exception)
    }
}

/**
 * 多对一依赖
 * 多对一依赖与一对多依赖正好相反，它是指方法C依赖于方法A和方法B的返回值。比如说，公司报销出差费用，需要发票单号；由于不同的人报销种类和费用不同，也需要获取个人的信息，代码示例如下：
 * //  获取个人信息
 * fun  getUserInfo():  Result<UserInfo>  {
 *       ...
 * }
 * //  获取发票单号
 * fun  getInvoiceNumber():  Result<String>  {
 *
 * }
 * //  报销费用
 * fun  reimbursement(userInfo:  UserInfo,  invoiceNumber:  String):  Result<Boolean>  {
 *       ...
 * }
 *
 */
@OptIn(ExperimentalContracts::class)
public inline fun <V> zip(block: () -> V): Result<V> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return runCatching {
        block()
    }
}

/**
 * zip  {
 *         val  userInfo  =  getUserInfo().getOrThrow()
 *         val  invoiceNumber  =  getInvoiceNumber().getOrThrow()
 *         userInfo  to  invoiceNumber
 * }.andThen  {
 *         reimbursement(it.first,  it.second)
 * }.onFailure  {  println("onFailure  $it")  }
 *         .onSuccess  {  println("onSuccess  $it")  }
 */


//选择关系 可选关系简单地说就是二选一（或者多选一）。比如说手机付款，先用零钱余额支付，如果支付失败（比如余额不足）则使用花呗支付。代码示例如下：
//  使用零钱支付
//fun  payByChange():  Result<Boolean>  { }
//  是否花呗支付
//fun  payByHuabei():  Result<Boolean>  {}

public fun <V> Result<V>.or(result: Result<V>): Result<V> {
    return when {
        isSuccess -> this
        else -> result
    }
}
//有了 or 方法，我们就可以简洁地处理选择的关系了。代码示例如下：
//payByChange().or(payByHuabei())
//        .onFailure  {  println("onFailure  $it")  }
//        .onSuccess  {  println("onSuccess  $it")  }


// ======================= Result集合 =====================
//  批量上传图片
//fun uploadFiles(paths: List<String>): List<Result<String>> {
//    return emptyList()
//}

//  获取成功结果的列表
public fun <V, R : Result<V>> valuesOf(results: List<R>): List<V> {
    return results.asIterable().filterValues()
}

public fun <V> Iterable<Result<V>>.filterValues(): List<V> {
    return filterValuesTo(ArrayList())
}

public fun <V, C : MutableCollection<in V>> Iterable<Result<V>>.filterValuesTo(destination: C): C {
    for (element in this) {
        if (element.isSuccess) {
            val value = element.getOrNull() ?: continue
            destination.add(value)
        }
    }
    return destination
}

//如果我们需要判断图片上传是否都执行成功了，我们可以创建 allSuccess 方法来判断是否全部执行成功。
public fun <V> Iterable<Result<V>>.allSuccess(): Boolean {
    return all(Result<V>::isSuccess)
}

// =================================  支持协程   ==========================================
/**
 * 在一对多依赖的例子中，是同步获取不同平台的视频播放量。但在开发中，更期望是并发获取
 * getUserPhoneNumber().coroutineDispatch  {  phoneNumber  ->
 *         val  bilbilVideoPlayNum  =  async  {
 *                 getBilbilVideoPlayNum(phoneNumber).bind()
 *         }
 *         val  tiktokVideoPlayNum  =  async  {
 *                 getTiktokVideoPlayNum(phoneNumber).bind()
 *         }
 *         bilbilVideoPlayNum.await()  +  tiktokVideoPlayNum.await()
 * }.onFailure  {  println("onFailure  $it")  }
 *   .onSuccess  {  println("onSuccess  $it")  }
 */
//  由于  async  需要一个  CoroutineScope  来启动，这里创建一个Result的CoroutineScope
public interface CoroutineResultScope<V> : CoroutineScope {
    public suspend fun <V> Result<V>.bind(): V
}

@PublishedApi
internal class CoroutineResultScopeImpl<V>(
    delegate: CoroutineScope,
) : CoroutineResultScope<V>, CoroutineScope by delegate {

    private val mutex = Mutex()
    var result: Result<V>? = null

    //  使用  bind  支持协程的结构化编程，这样当一个协程任务异常失败时，
    //  取消其他的协程任务
    override suspend fun <V> Result<V>.bind(): V {
        return if (isSuccess) {
            getOrNull() ?: throw InnerException()
        } else {
            mutex.withLock {
                if (result == null) {
                    result = Result.failure(this.exceptionOrNull() ?: InnerException())
                    coroutineContext.cancel()
                }
                throw CancellationException()
            }
        }
    }
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun <V, E> Result<V>.coroutineDispatch(crossinline block: suspend CoroutineResultScope<E>.(V) -> E): Result<E> {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (isSuccess) {
//        lateinit var receiver: CoroutineBindingScopeImpl<E>
        lateinit var receiver: CoroutineResultScopeImpl<E>
        return try {
            coroutineScope {
//                receiver = CoroutineBindingScopeImpl(this)
                receiver = CoroutineResultScopeImpl(this)
                val value = getOrNull() ?: throw InnerException()
                with(receiver) {
                    Result.success(block(value))
                }
            }
        } catch (ex: CancellationException) {
            receiver.result!!
        }
    } else {
        return Result.failure(exceptionOrNull() ?: InnerException())
    }
}
