package me.alzz.base

import android.content.Context
import android.content.res.Resources
import io.reactivex.Observable
import java.io.File

/**
 * 缓存文件至 Cache 文件夹
 * Created by JeremyHe on 2018/5/9.
 */
class RxCache {
    companion object {
        fun <T> cache(ctx: Context, name: String): Observable<T> {
            ctx.cacheDir
            Observable
                    .just(name)
                    .map {
                        val cache = File(ctx.cacheDir, name)
                        if (!cache.exists()) {
                            throw Resources.NotFoundException()
                        }

                        val data = cache.readText()
                    }
        }
    }
}