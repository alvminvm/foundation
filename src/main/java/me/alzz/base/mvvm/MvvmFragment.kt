package me.alzz.base.mvvm

import android.support.v7.app.AppCompatActivity
import me.alzz.Progress
import me.alzz.base.BaseFragment
import me.alzz.ext.get
import me.alzz.ext.use
import org.jetbrains.anko.toast
import kotlin.reflect.KClass

/**
 * Created by JeremyHe on 2019-05-26.
 */
open class MvvmFragment: BaseFragment() {

    private fun BaseVM.subscribeBase() {
        val activity = context as? AppCompatActivity
        this.progress.use(this@MvvmFragment) {
            if (it.isNullOrEmpty()) {
                Progress.dismiss(activity)
            } else {
                Progress.show(activity, it)
            }
        }
        this.desc.use(this@MvvmFragment) { context?.toast(it) }
        this.error.use(this@MvvmFragment) { context?.toast(it) }
    }

    fun <T : BaseVM> fragment(vmClazz: KClass<T>): Lazy<T> {
        return lazy {
            val vm = get(vmClazz.java)
            vm.subscribeBase()
            vm
        }
    }
}