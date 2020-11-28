package me.alzz.ext

import android.util.Log

/**
 * Created by JeremyHe on 2020-07-04.
 */

/**
 * 捕获异常但忽略具体异常
 * @return 抛异常时返回 null
 */
inline fun <T> tryElseNull(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        Log.w("ExceptionUtils", "return null if exception", e)
        null
    }
}

/**
 * 捕获异常但忽略具体异常
 */
inline fun <T> tryIgnore(block: () -> T) {
    try {
        block()
    } catch (e: Exception) {
        Log.w("ExceptionUtils", "ignore exception", e)
    }
}