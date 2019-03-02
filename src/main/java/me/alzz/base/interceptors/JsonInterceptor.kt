package me.alzz.base.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * Created by JeremyHe on 2019/3/2.
 */
class JsonInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader("Accept", "application/json,text/json")
                .build()
        val resp = chain.proceed(request)
        val body = resp.body()?.string() ?: ""
        if (body.isEmpty()) {
//            CrashReport.postCatchedException(RuntimeException("body 为空!!!"))
            return resp.newBuilder().body(ResponseBody.create(resp.body()!!.contentType(), body)).build()
        }

        val start = body.indexOf("{")
        val end = body.lastIndexOf("}") + 1
        if (body.length == end - start) {
            return resp.newBuilder().body(ResponseBody.create(resp.body()!!.contentType(), body)).build()
        }

//        CrashReport.postCatchedException(RuntimeException("body 内容不正确. $body"))
        return resp
                .newBuilder()
                .body(ResponseBody.create(resp.body()?.contentType(), body.substring(start, end)))
                .build()
    }
}