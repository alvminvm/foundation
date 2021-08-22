package me.alzz.ext

/**
 * 正则表达式相关
 * Created by JeremyHe on 2021/8/22.
 */

val REGEX_CHINESE = "[\u4e00-\u9fa5]".toRegex()
val REGEX_PHONE = "^[1][3456789][0-9]{9}$".toRegex()
val REGEX_EMAIL = "^[a-zA-Z0-9_\\-.]+@[a-zA-Z0-9_\\-]+(\\.[a-zA-Z0-9_-]+)+$".toRegex()
