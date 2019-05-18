package me.alzz.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import me.alzz.base.mvvm.BaseVM

fun <T : BaseVM> FragmentActivity.get(vm: Class<T>) = ViewModelProviders.of(this).get(vm)
fun <T : BaseVM> Fragment.get(vm: Class<T>) = ViewModelProviders.of(this).get(vm)

fun <T> LiveData<T>.use(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner, Observer {
        it ?: return@Observer
        block.invoke(it)
    })
}

