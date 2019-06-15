package me.alzz

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observable
import java.io.File
import java.lang.reflect.Type

/**
 * 缓存文件至 Cache 文件夹
 * Created by JeremyHe on 2018/5/9.
 */
class Cache {
    companion object {
        fun <T> load(ctx: Context, name: String, validDays: Int, type: Class<T>): Observable<T> {
            return load(ctx, name, validDays) {
                val gson = Gson()
                gson.fromJson(it, type)
            }
        }

        fun <T> load(ctx: Context, name: String, validDays: Int, type: Type): Observable<T> {
            return load(ctx, name, validDays) {
                val gson = Gson()
                gson.fromJson(it, type) as T
            }
        }

        fun <T> load(ctx: Context, name: String, validDays: Int, block: (String)->T): Observable<T> {
            return Observable
                .create {
                    val cache = File(ctx.cacheDir, name)
                    if (!cache.exists()) {
                        it.onComplete()
                        return@create
                    }

                    val cachedTime = System.currentTimeMillis() - cache.lastModified()
                    if (cachedTime >= validDays * 24L * 60 * 60 * 1000) {
                        it.onComplete()
                        return@create
                    }

                    val data = cache.readText()
                    val result = block(data)
                    it.onNext(result)
                    it.onComplete()
                }
        }

        fun <T> cache(ctx: Context, name: String, data: T) {
            cache(ctx, name, data) {
                val gson = Gson()
                gson.toJson(data)
            }
        }

        fun <T> cache(ctx: Context, name: String, data: T, transform: (T)->String) {
            val cache = File(ctx.cacheDir, name)
            if (cache.exists()) {
                cache.delete()
            }

            val json = transform(data)
            cache.bufferedWriter().use { it.write(json) }
        }
    }
}