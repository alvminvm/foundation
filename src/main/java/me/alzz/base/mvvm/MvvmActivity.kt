package me.alzz.base.mvvm

import me.alzz.Progress
import me.alzz.base.BaseActivity
import me.alzz.ext.get
import me.alzz.ext.use
import org.jetbrains.anko.toast
import kotlin.reflect.KClass

/**
 * Created by JeremyHe on 2019-05-26.
 */
open class MvvmActivity: BaseActivity() {

    private fun BaseVM.subscribeBase() {
        this.progress.use(this@MvvmActivity) {
            if (it.isNullOrEmpty()) {
                Progress.dismiss(this@MvvmActivity)
            } else {
                Progress.show(this@MvvmActivity, it)
            }
        }
        this.desc.use(this@MvvmActivity) { toast(it) }
        this.error.use(this@MvvmActivity) { toast(it) }
    }

    fun <T : BaseVM> activity(vmClazz: KClass<T>): Lazy<T> {
        return lazy {
            val vm = get(vmClazz.java)
            vm.subscribeBase()
            vm
        }
    }
}