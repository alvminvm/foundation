package me.alzz.base.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import me.alzz.ext.disposeBy

/**
 * Created by JeremyHe on 2019/3/12.
 */
open class BaseVM: ViewModel(), DisposableContainer {

    val progress = MediatorLiveData<String>()

    // TODO: 2020/11/29 更换成事件通知
    val desc = vmLiveData<String>()
    val error = vmLiveData<String>()

    private val d = CompositeDisposable()
    private val disposableMap = mutableMapOf<String, Disposable>()

    override fun onCleared() {
        d.dispose()
        super.onCleared()
    }

    override fun add(d: Disposable) = this.d.add(d)

    override fun remove(d: Disposable) = this.d.remove(d)

    override fun delete(d: Disposable) = this.d.delete(d)

    infix fun <S> MediatorLiveData<String>.emptyBy(source: LiveData<S>) {
        this.addSource(source) { this.value = "" }
    }

    infix fun <T> MutableLiveData<T>.n(target: MediatorLiveData<String>): MutableLiveData<T> {
        target.addSource(this) { target.value = "" }
        return this
    }

    fun setProgress(msg: String, isCancelable: Boolean = false) {
        if (isCancelable) {
            progress.postValue("$msg#cancelable#")
        } else {
            progress.postValue(msg)
        }
    }

    fun <T> vmLiveData(source: LiveData<T>? = null): MutableLiveData<T> {
        return if (source == null) {
            MutableLiveData<T>()
        } else {
            MediatorLiveData<T>().apply {
                addSource(source) {
                    this.postValue(it)
                }
            }
        } n progress
    }

    fun <T, R> vmLiveData(source: LiveData<R>, convert: (R) -> T): MutableLiveData<T> {
        return MediatorLiveData<T>().apply {
            addSource(source) {
                val t = convert(it)
                this.postValue(t)
            }
        } n progress
    }

    private fun dispose(taskName: String) {
        disposableMap[taskName]?.apply {
            dispose()
            remove(d)
        }
    }

    fun Disposable.disposeBy(taskName: String): Disposable {
        dispose(taskName)
        disposableMap[taskName] = this
        disposeBy(this@BaseVM)
        return this
    }
}