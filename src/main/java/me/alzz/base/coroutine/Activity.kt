package me.alzz.base.coroutine

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel

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

data class ActivityResult(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
)

/**
 * 获取与当前 Activity 关联的 指定 requestCode 的 channel
 */
private fun Activity.channel(requestCode: Int): Channel<ActivityResult> {
    val channels = (activityLocalGet("channels") ?: activityLocalPut("channels", mutableMapOf<Int, Channel<ActivityResult>>())) as MutableMap<Int, Channel<ActivityResult>>
    return channels[requestCode] ?: Channel<ActivityResult>().apply { channels[requestCode] = this }
}

/**
 * 跳转至 Activity 并等待结果返回
 */
suspend fun Activity.awaitForResult(intent: Intent, requestCode: Int): ActivityResult {
    startActivityForResult(intent, requestCode)
    return channel(requestCode).receive()
}