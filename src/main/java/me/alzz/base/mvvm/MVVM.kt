package me.alzz.base.mvvm

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders
import me.alzz.Progress
import me.alzz.ext.get
import me.alzz.ext.use
import org.jetbrains.anko.toast
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

/**
 * Created by JeremyHe on 2019-11-16.
 */
fun <T : BaseVM> AppCompatActivity.activity(vmClazz: KClass<T>): Lazy<T> {
    return lazy {
        val vm = get(vmClazz.java)
        bind(vm)
        vm
    }
}

fun <T : BaseVM> Fragment.activity(vmClazz: KClass<T>): Lazy<T> {
    return lazy {
        val activity = context as? FragmentActivity ?: throw IllegalArgumentException("need activity")
        ViewModelProviders.of(activity).get(vmClazz.java)
    }
}

private fun <T : BaseVM> LifecycleOwner.bind(vm: T) {
    val context = this as? Context ?: (this as? Fragment)?.context ?: return
    vm.progress.use(this) {
        val activity = context as? FragmentActivity ?: return@use
        if (it.isNullOrEmpty()) {
            Progress.dismiss(activity)
        } else {
            val msg = it.removeSuffix("#cancelable#")
            val isCancelable = msg != it
            Progress.show(activity, msg, isCancelable)
        }
    }
    vm.desc.use(this) { context.toast(it) }
    vm.error.use(this) { context.toast(it) }
}

fun <T : BaseVM> Fragment.fragment(vmClazz: KClass<T>): Lazy<T> {
    return lazy {
        val vm = get(vmClazz.java)
        bind(vm)
        vm
    }
}