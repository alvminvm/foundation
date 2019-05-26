package me.alzz.base.mvvm

import me.alzz.Progress
import me.alzz.base.BaseActivity
import me.alzz.ext.use
import org.jetbrains.anko.toast

/**
 * Created by JeremyHe on 2019-05-26.
 */
open class MvvmActivity: BaseActivity() {

    protected fun subscribeBase(vm: BaseVM) {
        vm.progress.use(this) {
            if (it.isNullOrEmpty()) {
                Progress.dismiss(this)
            } else {
                Progress.show(this, it)
            }
        }
        vm.desc.use(this) { toast(it) }
        vm.error.use(this) { toast(it) }
    }
}