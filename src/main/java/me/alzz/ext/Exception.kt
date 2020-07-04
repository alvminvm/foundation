package me.alzz.ext

/**
 * Created by JeremyHe on 2020-07-04.
 */

/**
 * 捕获异常但忽略具体异常
 * @return 抛异常时返回 null
 */
inline fun <T> nullIfException(block: () -> T): T? {
    return try {
        block()
    } catch (e: Exception) {
        null
    }
}