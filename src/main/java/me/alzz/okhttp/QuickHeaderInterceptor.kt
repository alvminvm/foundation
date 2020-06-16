package me.alzz.okhttp

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by JeremyHe on 2020-05-19.
 */
class QuickHeaderInterceptor : Interceptor {

    private val ops = mutableListOf<Any>()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headers = request.headers
        var builder = request.newBuilder()
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            if (headers.value(i) != VALUE_STUB) continue

            for (op in ops) {
                if (op is AddOp) {
                    if (!op.predicate(name)) continue
                    val map = op.op(name, request)
                    map.forEach { builder.header(it.key, it.value) }

                    request = builder.build()
                    builder = request.newBuilder()
                } else if (op is ReplaceOp) {
                    if (!op.predicate(name)) continue
                    val value = op.op(name, request)
                    if (value.isEmpty()) continue

                    builder.header(name, value)

                    request = builder.build()
                    builder = request.newBuilder()
                }
            }
        }

        return chain.proceed(builder.build())
    }

    fun replace(predicate: (name: String) -> Boolean, op: (name: String, request: Request) -> String): QuickHeaderInterceptor {
        ops.add(ReplaceOp(predicate, op))
        return this
    }

    fun add(predicate: (name: String) -> Boolean, op: (name: String, request: Request) -> Map<String, String>): QuickHeaderInterceptor {
        ops.add(AddOp(predicate, op))
        return this
    }

    class ReplaceOp(
        val predicate: (name: String) -> Boolean,
        val op: (name: String, request: Request) -> String
    )

    class AddOp(
        val predicate: (name: String) -> Boolean,
        val op: (name: String, request: Request) -> Map<String, String>
    )

    companion object {
        const val VALUE_STUB = "<Header Stub>"

        fun headerStub(name: String) = "$name : $VALUE_STUB"
    }
}