package me.alzz.base.mvvm

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.alzz.Progress
import me.alzz.ext.get
import me.alzz.ext.use
import org.jetbrains.anko.toast
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1

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

/**
 * 双向绑定 [EditText] 和 [LiveData]
 * 要求 [EditText] 的宿主必须为 [LifecycleOwner]
 * @param setter EditText set to data [T]
 * @param getter get from data [T]
 */
fun <T, R> EditText.bindTo(data: LiveData<T>,
                           setter: (receiver: T, value: R) -> Unit,
                           getter: (receiver: T) -> R) {
    // EditText -> LiveData
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val r = data.value ?: return
            val str = s?.toString()?.trim() ?: return
            val cur = getter(r)
            val value = when (cur) {
                is String -> str
                is Int -> str.toIntOrNull() ?: return
                is Long -> str.toLongOrNull() ?: return
                is List<*> -> str.split(",").toList()
                else -> throw UnsupportedOperationException("bind to 操作未支持此类型")
            } as R

            if (value == cur) return
            setter(r, value)
        }

        override fun afterTextChanged(s: Editable?) {}
    })

    // LiveData -> EditText
    val owner = this.context as LifecycleOwner
    data.observe(owner, Observer {
        val str = when (val value = getter(it)) {
            is List<*> -> value.joinToString(",")
            else -> value.toString()
        }

        if (text.toString() == str) return@Observer
        this.setText(str)
    })
}

/**
 * 双向绑定 [EditText] 和 [LiveData]
 * 要求 [EditText] 的宿主必须为 [LifecycleOwner]
 * @param accessor 要绑定的具体某个属性
 */
fun <T, R> EditText.bindTo(data: LiveData<T>, accessor: KMutableProperty1<T, R>) {
    bindTo(
        data,
        { receiver: T, value: R -> accessor.set(receiver, value) },
        { accessor.get(it) })
}
