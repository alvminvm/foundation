package me.alzz.ext

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
 * 给手机号增加空格
 */
fun String.beautyPhone(): String {
    return when {
        this.length > 7 -> "${substring(0..2)} ${substring(3..6)} ${substring(7)}"
        this.length > 3 -> "${substring(0..2)} ${substring(3)}"
        else -> this
    }
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

        regex = """com/i(\d{9,}).htm""".toRegex()
        regex.find(this)?.let {
            Log.d("StringExt", "found item html id")
            return it.groupValues[1]
        }
    }

    return null
}

/**
 * 查询淘口令中的短链
 */
fun CharSequence.findTbShortUrl(): String? {
    if (this.contains("tb.cn")) {
        val regex = """(http://m\.tb\.cn/[\w\.]+) """.toRegex()
        regex.find(this)?.let {
            Log.d("StringExt", "found tb short url id")
            return it.groupValues[1]
        }
    }

    return null
}

/**
 * 查找商品名称
 */
fun CharSequence.findTbProductName(): String? {
    var name: String? = null
    if (this.contains("【")) {
        if (this[0] == '【') {
            val regex = """【(\S+)】""".toRegex()
            regex.find(this)?.let {
                Log.d("StringExt", "found tb product name")
                name = it.groupValues[1]
            }
        } else {
            name = this.substring(0, this.indexOf("【"))
        }
    } else if (this.contains("手淘") || this.contains("淘宝") || this.contains("天猫")) {
        name = this.lines()[0]
    }

    name?.let { if (name!!.contains("分享")) name = null }
    return name
}