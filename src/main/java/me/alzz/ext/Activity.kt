package me.alzz.ext

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

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
