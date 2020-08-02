package me.alzz.ext

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import me.alzz.CommonDialog
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 与 Activity 相关的协程方法
 */

private val activityLocals = mutableMapOf<Activity, MutableMap<String, Any>>()

fun Activity.activityLocalGet(tag: String) = activityLocals[this]?.get(tag)

fun Activity.activityLocalPut(tag: String, data: Any): Any {
    val locals = activityLocals[this]
        ?: mutableMapOf<String, Any>().also {
            val owner = this as LifecycleOwner
            owner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        activityLocals.remove(this@activityLocalPut)
                        owner.lifecycle.removeObserver(this)
                    }
                }

            })
        }

    locals[tag] = data
    return data
}

/**
 * 弹出对话框询问用户意见后继续执行
 */
suspend fun AppCompatActivity.askUser(
    title: String,
    content: CharSequence,
    confirm: String = "确定",
    cancel: String = "",
    isCancelable: Boolean = true
) = suspendCoroutine<String> {
    val dialog = CommonDialog.show(title, content, confirm, cancel, supportFragmentManager)
    dialog.isCancelable = isCancelable
    dialog.onConfirm = { action ->
        dialog.onDismiss = null
        it.resume(action)
    }

    dialog.onCancel = { action ->
        dialog.onDismiss = null
        it.resume(action)
    }

    dialog.onDismiss = {
        it.resume("")
    }
}
