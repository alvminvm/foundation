package me.alzz.base.mvvm

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

/**
 * Created by JeremyHe on 2019/3/12.
 */
open class BaseVM: ViewModel(), DisposableContainer {

    val d = CompositeDisposable()

    override fun onCleared() {
        d.dispose()
        super.onCleared()
    }

    override fun add(d: Disposable) = this.d.add(d)

    override fun remove(d: Disposable) = this.d.remove(d)

    override fun delete(d: Disposable) = this.d.delete(d)
}