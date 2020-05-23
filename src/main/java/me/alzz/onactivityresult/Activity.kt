@file:JvmName("ActivityResultUtils")
package me.alzz.onactivityresult

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.suspendCoroutine

fun FragmentActivity.startActivityForResult(
    intent: Intent,
    callback: OnActivityResultCallback
): Int {
    val f = OnActivityResultFragment.of(this, callback)
    val requestCode = f.generateRequestCode()
    f.startActivityForResult(intent, requestCode)
    return requestCode
}

suspend fun FragmentActivity.awaitForResult(intent: Intent) = suspendCoroutine<ActivityResult> {
    val callback = object : OnActivityResultCallback {
        var requestCode = 0

        override fun onActivityResult(result: ActivityResult) {
            if (requestCode == result.requestCode) {
                it.resumeWith(Result.success(result))
            } else {
                Log.w("ActivityResultUtils", "request code not match! expect: $requestCode, but: $result")
            }
        }
    }

    callback.requestCode = startActivityForResult(intent, callback)
}