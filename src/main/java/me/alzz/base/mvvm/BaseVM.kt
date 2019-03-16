package me.alzz.base.mvvm

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by JeremyHe on 2019/3/12.
 */
class BaseVM: ViewModel() {
    val d = CompositeDisposable()

    override fun onCleared() {
        d.dispose()
        super.onCleared()
    }
}