package me.alzz.okhttp

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream

/**
 * Created by JeremyHe on 2020/8/16.
 */
class LogInterceptor : Interceptor {
    companion object {
        private const val CURL_COMMAND = "curl -X %s "
        private const val CURL_HEADER_FORMAT = "-H %s: %s "
        private const val TAG = "http"
    }

    private fun generateCURLCommandString(request: Request): String? {
        val url = request.url.toString()
        val method = request.method
        val headers = request.headers
        val sb = StringBuilder()
        sb.append(String.format(CURL_COMMAND, method))
        for (name in headers.names()) {
            sb.append(String.format(CURL_HEADER_FORMAT, name, headers[name]))
        }

        try {
            val body = request.body
            if (null != body) {
                ByteArrayOutputStream().use {
                    val sink = it.sink().buffer()
                    body.writeTo(sink)
                    sink.close()
                    val bodyString = it.toString()
                    sb.append(String.format("-d '%s' ", bodyString))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        sb.append(url)
        return sb.toString()
    }

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val rsp = chain.proceed(request)
            Log.d(TAG, "Request: ${generateCURLCommandString(request)}")

            val rspCode = rsp.code
            val rspHeaders = rsp.headers
            val rspBody = rsp.body?.string() ?: ""
            Log.d(TAG, "Response: ${rsp.code} \n${rspHeaders} \n${rspBody} ")

            return rsp.newBuilder()
                .code(rspCode)
                .headers(rspHeaders)
                .body(ResponseBody.create(rsp.body?.contentType(), rspBody))
                .build()
        } catch (e: Exception) {
            Log.d(TAG, "Response: exception = $e")
            throw e
        }
    }
}