@file:JvmName("StringUtils")
package me.alzz.ext

import okhttp3.internal.and

/**
 * String 工具方法
 * Created by JeremyHe on 2018/4/15.
 */

/**
 * 判断字符串是否为空
 */
fun String?.isNotEmpty() = !this.isNullOrEmpty()

/**
 * 移除小数点后的0
 */
fun String.trimZero(): String {
    var result = this.replace("""\.0+$""".toRegex(), "")
    if (result.contains(".")) {
        result = result.trimEnd { it == '0' }
    }

    return result
}

fun String.isEmail() = this.matches("^[a-zA-Z0-9_\\-.]+@[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_-]+)+$".toRegex())

fun String.isPhone() = this.matches("^[1][3456789][0-9]{9}$".toRegex())

/**
 * 给手机号增加空格
 */
fun String.beautyPhone(): String {
    return when {
        this.length > 7 -> "${substring(0..2)} ${substring(3..6)} ${substring(7)}"
        this.length > 3 -> "${substring(0..2)} ${substring(3)}"
        else -> this
    }
}

fun randomString(len: Int): String {
    val charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..len).map { charset.random() }.joinToString()
}

fun ByteArray.toHexString(): String {
    val stringBuilder = StringBuilder()
    for (i in this.indices) {
        val v = this[i] and 0xFF
        val hv = Integer.toHexString(v)
        if (hv.length < 2) {
            stringBuilder.append(0)
        }
        stringBuilder.append(hv)
    }
    return stringBuilder.toString()
}
