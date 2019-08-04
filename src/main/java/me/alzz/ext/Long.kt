package me.alzz.ext

import java.util.*

/**
 * Created by JeremyHe on 2019-08-04.
 */
fun Long.isToday(): Boolean {
    val c = Calendar.getInstance(Locale.CHINA)
    c.set(Calendar.HOUR_OF_DAY, 0)
    c.set(Calendar.MINUTE, 0)
    c.set(Calendar.SECOND, 0)
    c.set(Calendar.MILLISECOND, 0)
    return this in c.timeInMillis..(c.timeInMillis + 24 * 60 * 60 * 1000)
}