package me.alzz.ext

import me.alzz.okhttp.AcceptJsonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by JeremyHe on 2019/3/12.
 */

/**
 * 设置接收类型为 json
 */
fun Retrofit.Builder.acceptJson(): Retrofit.Builder {
    val retrofit = this.build()
    val client = retrofit.callFactory() as OkHttpClient
    val clientBuilder = client
            .newBuilder()
            .addInterceptor(AcceptJsonInterceptor())

    return retrofit.newBuilder().client(clientBuilder.build())
}
