package me.alzz.base

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.DialogFragment

/**
 * Created by JeremyHe on 2022/1/8.
 */
open class BaseDialogFragment: DialogFragment() {
    private val h = Handler(Looper.getMainLooper())

    override fun onDestroy() {
        h.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    protected fun afterAdd(action: () -> Unit) {
        h.postDelayed({
            if (isAdded) {
                action()
            } else {
                afterAdd(action)
            }
        }, 200)
    }

}