package me.alzz.ext

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import io.reactivex.schedulers.Schedulers
import me.alzz.Cache

/**
 * RxJava 扩展方法
 * Created by JeremyHe on 2019/3/12.
 */

/**
 * 线程切换
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
}
fun <T> Single<T>.applySchedulers(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
}

/**
 * 主线程运行
 */
fun <T> Observable<T>.observeOnMain(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

/**
 * IO线程
 */
fun <T> Observable<T>.observeOnIo(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

fun Disposable.disposeBy(d: DisposableContainer): Disposable {
    d.add(this)
    return this
}

/**
 * 缓存结果
 */
fun <T> Observable<T>.cache(ctx: Context, name: String, type: Class<T>, validDays: Int): Single<T> {
    val cache = Cache.load(ctx, name, type, validDays).applySchedulers()
    val expensive = this
            .applySchedulers()
            .observeOn(Schedulers.io())
            .map { Cache.cache(ctx, name, it); it }
            .observeOn(AndroidSchedulers.mainThread())

    return Observable
            .concat(cache, expensive)
            .applySchedulers()
            .firstOrError()
}