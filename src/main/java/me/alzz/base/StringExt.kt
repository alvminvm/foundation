package me.alzz.base

import android.util.Log

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

/**
 * 查找淘宝商品 Id
 */
fun CharSequence.findTbItemId(): String? {
    if (this.contains("taobao.com") || this.contains("tmall.com")) {
        var regex = """item[iI]d=(\d*)""".toRegex()
        regex.find(this)?.let {
            Log.d("StringExt", "found tb item id")
            return it.groupValues[1]
        }

//        regex = """sku[iI]d=(\d*)""".toRegex()
//        regex.find(this)?.let {
//            Log.d("StringExt", "found tb sku id")
//            return it.groupValues[1]
//        }

        regex = """[?&]id=(\d{9,})""".toRegex()
        regex.find(this)?.let {
            Log.d("StringExt", "found tb id")
            return it.groupValues[1]
        }
    }

    return null
}

fun CharSequence.findTbShortUrl(): String? {
    if (this.contains("tb.cn")) {
        val regex = """(http://m\.tb\.cn/\w+) """.toRegex()
        regex.find(this)?.let {
            Log.d("StringExt", "found tb short url id")
            return it.groupValues[1]
        }
    }

    return null
}