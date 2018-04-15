package me.alzz.base

/**
 * String 工具方法
 * Created by JeremyHe on 2018/4/15.
 */

/**
 * 判断字符串是否为空
 */
fun String.isNotEmpty() = !this.isNullOrEmpty()

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