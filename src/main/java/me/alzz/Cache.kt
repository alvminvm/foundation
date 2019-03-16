package me.alzz

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observable
import java.io.File

/**
 * 缓存文件至 Cache 文件夹
 * Created by JeremyHe on 2018/5/9.
 */
class Cache {
    companion object {
        fun <T> load(ctx: Context, name: String, type: Class<T>, validDays: Int): Observable<T> {
            return Observable
                    .create {
                        val cache = File(ctx.cacheDir, name)
                        if (!cache.exists()) {
                            it.onComplete()
                            return@create
                        }

                        val cachedTime = System.currentTimeMillis() - cache.lastModified()
                        if (cachedTime >= validDays * 24 * 60 * 60 * 1000) {
                            cache.delete()
                            it.onComplete()
                            return@create
                        }

                        val data = cache.readText()
                        val gson = Gson()
                        it.onNext(gson.fromJson(data, type))
                        it.onComplete()
                    }
        }

        fun <T> cache(ctx: Context, name: String, data: T) {
            val cache = File(ctx.cacheDir, name)
            if (cache.exists()) {
                cache.delete()
            }

            val gson = Gson()
            val json = gson.toJson(data)
            cache.bufferedWriter().use { it.write(json) }
        }
    }
}