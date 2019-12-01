package me.alzz.base.mvvm

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import me.alzz.Progress
import me.alzz.ext.get
import me.alzz.ext.use
import org.jetbrains.anko.toast
import kotlin.reflect.KClass

/**
 * Created by JeremyHe on 2019-11-16.
 */
fun <T : BaseVM> AppCompatActivity.activity(vmClazz: KClass<T>): Lazy<T> {
    return lazy {
        val vm = get(vmClazz.java)
        vm.progress.use(this) {
            if (it.isNullOrEmpty()) {
                Progress.dismiss(this)
            } else {
                Progress.show(this, it)
            }
        }
        vm.desc.use(this) { toast(it) }
        vm.error.use(this) { toast(it) }
        vm
    }
}

fun <T : BaseVM> Fragment.activity(vmClazz: KClass<T>): Lazy<T?> {
    return lazy {
        val activity = context as? FragmentActivity
        if (activity == null) null else ViewModelProviders.of(activity).get(vmClazz.java)
    }
}

fun <T : BaseVM> Fragment.fragment(vmClazz: KClass<T>): Lazy<T> {
    return lazy {
        val vm = get(vmClazz.java)
        vm.progress.use(this) {
            val activity = context as? FragmentActivity ?: return@use
            if (it.isNullOrEmpty()) {
                Progress.dismiss(activity)
            } else {
                Progress.show(activity, it)
            }
        }
        vm.desc.use(this) { context?.toast(it) }
        vm.error.use(this) { context?.toast(it) }
        vm
    }
}