package me.alzz.ext

import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias Error = String?

/**
 * 获取此 url 跳转之后的链接
 */
suspend fun String.redirectUrl() = suspendCoroutine<Pair<String?, Error>> {
    val client = OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build()
    val request = Request.Builder().url(this).get().build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            it.resume(null to "请求失败")
        }

        override fun onResponse(call: Call, response: Response) {
            var url = response.headers["location"]
            if (!url.isNullOrBlank()) {
                it.resume(url to null)
                return
            }

            url = response.body?.string() ?: ""
            if (url.contains("http")) {
                url = url.removePrefix("±img=").removeSuffix("±")
                it.resume(url to null)
                return
            }

            it.resume(null to "未找到重定向地址")
        }
    })
}