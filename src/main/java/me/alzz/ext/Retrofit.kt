package me.alzz.ext

import me.alzz.okhttp.AcceptJsonInterceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.http.Part
import java.io.File

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

/**
 * 文件转换成 Multipart
 * 配合 [Part] 注解，常用于文件上传
 */
fun File.asPart(name: String = "file"): MultipartBody.Part {
    return MultipartBody.Part.createFormData(name, this.name, this.asRequestBody())
}
