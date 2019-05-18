package me.alzz.base.mvvm

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import me.alzz.ext.disposeBy

/**
 * Created by JeremyHe on 2019/3/12.
 */
open class BaseVM: ViewModel(), DisposableContainer {

    val progress = MediatorLiveData<String>()
    val desc = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    private val d = CompositeDisposable()
    private val disposableMap = mutableMapOf<String, Disposable>()

    init {
        progress.addSource(desc) { progress.value = "" }
        progress.addSource(error) { progress.value = "" }
    }

    override fun onCleared() {
        d.dispose()
        super.onCleared()
    }

    override fun add(d: Disposable) = this.d.add(d)

    override fun remove(d: Disposable) = this.d.remove(d)

    override fun delete(d: Disposable) = this.d.delete(d)

    fun dispose(taskName: String) {
        disposableMap[taskName]?.apply {
            dispose()
            remove(d)
        }
    }

    fun Disposable.disposeBy(taskName: String) {
        dispose(taskName)
        disposableMap[taskName] = this
        this.disposeBy(this@BaseVM)
    }
}