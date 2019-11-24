package me.alzz.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import me.alzz.base.mvvm.BaseVM

fun <T : BaseVM> FragmentActivity.get(vm: Class<T>) = ViewModelProviders.of(this).get(vm)
fun <T : BaseVM> Fragment.get(vm: Class<T>) = ViewModelProviders.of(this).get(vm)

fun <T> LiveData<T>.use(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner, Observer {
        it ?: return@Observer
        block.invoke(it)
    })
}

