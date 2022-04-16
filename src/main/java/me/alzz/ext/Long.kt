package me.alzz.ext

import java.util.*

/**
 * 是否当天
 */
fun Long.isToday(): Boolean {
    val c = Calendar.getInstance(Locale.CHINA)
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return this in c.timeInMillis..(c.timeInMillis + 24 * 60 * 60 * 1000)
}

fun Long.beautyTime(): String {
    if (this <= 0) return "现在"

    val full = mutableListOf<String>()
    val result = {
        full
            .takeLast(2)
            .reversed()
            .joinToString(separator = "", postfix = "后")
    }

    val timeInSeconds = this / 1000
    full.add("${timeInSeconds % 60}秒")
    if (timeInSeconds < 60) return result()

    val timeInMinutes = timeInSeconds / 60
    full.add("${timeInMinutes % 60}分")
    if (timeInMinutes < 60) return result()

    val timeInHours = timeInMinutes / 60
    full.add("${timeInHours % 24}小时")
    if (timeInHours < 3) return result()
    if (timeInHours < 24) return "${timeInHours}小时后"

    val timeInDays = timeInHours / 24
    full.add("${timeInDays % 7}天")
    if (timeInDays < 2) return result()
    if (timeInDays < 7) return "${timeInDays}天后"

    val timeInWeeks = timeInDays / 7
    full.add("${timeInWeeks}周")
    if (timeInWeeks < 2) return result()
    if (timeInWeeks < 4) return "${timeInWeeks}周后"

    return "很久以后"
}