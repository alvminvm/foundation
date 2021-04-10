package me.alzz

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Observable
import me.alzz.crypt.AesCbcWithIntegrity
import me.alzz.ext.withIo
import java.io.File
import java.lang.reflect.Type

/**
 * 缓存文件至 Cache 文件夹
 * Created by JeremyHe on 2018/5/9.
 */
class Cache {
    companion object {
        private var cacheDir: File? = null
        private val gson = Gson()

        fun init(context: Context) {
            cacheDir = context.cacheDir
        }

        fun <T> load(name: String, validDays: Float, type: Class<T>): Observable<T> {
            return Observable
                .create {
                    val data = load(name, validDays)
                    if (data.isNotEmpty()) {
                        val result = gson.fromJson(data, type)
                        if (result != null) it.onNext(result)
                    }

                    it.onComplete()
                }
        }

        fun <T> load(name: String, validDays: Float, type: Type): Observable<T> {
            return Observable
                .create {
                    try {
                        val data = load(name, validDays)
                        if (data.isEmpty()) {
                            it.onError(Throwable("cache data empty"))
                            return@create
                        }

                        val result = gson.fromJson(data, type) as? T
                        if (result == null) {
                            it.onError(Throwable("parse json fail"))
                            return@create
                        }

                        it.onNext(result)
                    } catch (t: Throwable) {
                        it.onError(t)
                    } finally {
                        it.onComplete()
                    }
                }
        }

        suspend fun <T> suspendLoad(name: String, validDays: Float, type: Type) = withIo {
            val data = load(name, validDays)
            if (data.isEmpty()) return@withIo null

            return@withIo gson.fromJson(data, type) as T
        }

        private fun load(name: String, validDays: Float): String {
            val cache = File(cacheDir, name)
            if (!cache.exists()) {
                return ""
            }

            val cachedTime = System.currentTimeMillis() - cache.lastModified()
            if (cachedTime >= validDays * 24L * 60 * 60 * 1000) {
                return ""
            }

            val encrypted = cache.readText()

            val data = try {
                val list = encrypted.split("!")
                val key = AesCbcWithIntegrity.keys(list[0])
                val civ = AesCbcWithIntegrity.CipherTextIvMac(list[1])
                AesCbcWithIntegrity.decryptString(civ, key)
            } catch (e: Exception) {
                encrypted
            }

            return data
        }

        suspend fun <T> suspendCache(name: String, data: T) = withIo { cache(name, data) }

        fun remove(name: String) {
            val cache = File(cacheDir, name)
            cache.delete()
        }

        fun <T> cache(name: String, data: T) {
            val cache = File(cacheDir, name)
            if (cache.exists()) {
                cache.delete()
            }

            val json = gson.toJson(data)
            val key = AesCbcWithIntegrity.generateKey()
            val civ = AesCbcWithIntegrity.encrypt(json, key)
            val encrypted = "$key!$civ"
            cache.bufferedWriter().use { it.write(encrypted) }
        }
    }
}