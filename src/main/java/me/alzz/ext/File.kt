package me.alzz.ext

import java.io.File
import java.util.*

/**
 * Created by JeremyHe on 2020/12/13.
 */

fun File.getHead(size: Int = 28): String {
    val bytes = ByteArray(size)
    this.inputStream().use {
        it.read(bytes, 0, bytes.size)
    }

    return bytes.toHexString()
}

fun File.getFileType(): FileType? {
    val maxSize = FileType.values().maxOf { it.head.length }
    val head = this.getHead(maxSize).uppercase(Locale.getDefault())
    return FileType.values().find {
        if (head.length < it.head.length) return@find false
        if (it.head.contains("_")) {
            val miniHead = head.substring(0, it.head.length)
            for (i in miniHead.indices) {
                if (it.head[i] == '_') continue
                if (it.head[i] == miniHead[i]) continue
                return@find false
            }

            true
        } else {
            head.startsWith(it.head)
        }
    }
}