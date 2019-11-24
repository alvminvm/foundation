package me.alzz.base.mvvm

import androidx.appcompat.app.AppCompatActivity
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