package me.alzz.okhttp

import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(response: Response, private val url: String) : ResponseBody() {
    private val responseBody = response.body!!
    private lateinit var bufferedSource: BufferedSource

    var onProgressChange: ((url: String, percent: Int)-> Unit)? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (::bufferedSource.isInitialized) return bufferedSource
        bufferedSource = source(responseBody.source()).buffer()
        return bufferedSource
    }

    private fun source(source: BufferedSource): Source {
        return object : ForwardingSource(source) {
            var totalBytes = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytes: Long = super.read(sink, byteCount)
                totalBytes += if (bytes != -1L) bytes else 0

                val len = contentLength()
                if (len > 0) {
                    val p = (totalBytes * 100 / contentLength()).toInt()
                    onProgressChange?.invoke(url, minOf(100, maxOf(0, p)))
                }

                return bytes
            }
        }
    }
}