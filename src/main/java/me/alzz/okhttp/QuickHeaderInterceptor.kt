package me.alzz.okhttp

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by JeremyHe on 2020-05-19.
 */
class QuickHeaderInterceptor(private val replace: (name: String, request: Request) -> String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers
        val builder = request.newBuilder()
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            if (headers.value(i) == VALUE_STUB) {
                builder.header(name, replace(name, request))
            }
        }

        return chain.proceed(builder.build())
    }

    companion object {
        const val VALUE_STUB = "<Header Stub>"

        fun headerStub(name: String) = "$name : $VALUE_STUB"
    }
}