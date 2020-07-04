package me.alzz.ext

import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by JeremyHe on 2020-05-31.
 */
suspend fun <T> withIo(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)

/**
 * Observable 转协程运行
 */
suspend fun <T> Observable<T>.await() = suspendCoroutine<T> {
    this
        .applySchedulers()
        .subscribe({ result ->
            it.resume(result)
        }) { t ->
            it.resumeWithException(t)
        }
}

