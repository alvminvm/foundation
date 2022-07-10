package me.alzz

import android.app.Activity
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle

class Progress {
    companion object {
        private const val TAG = "Progress"
        private val progressMap = mutableMapOf<Activity, ProgressFragment>()

        fun show(activity: FragmentActivity?, message: String, isCancelable: Boolean = false) {
            activity ?: return
            if (activity.isFinishing) return

            val dialog = progressMap[activity]
                ?: (activity.supportFragmentManager.findFragmentByTag(TAG) as? ProgressFragment
                ?: ProgressFragment()).apply {
                    progressMap[activity] = this
                    activity.lifecycle.addObserver(GenericLifecycleObserver { _, event ->
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            progressMap[activity]?.dismissSafely()
                            progressMap.remove(activity)
                        }
                    })
            }

            dialog.isCancelable = isCancelable
            dialog.setMessage(message)
            if (!dialog.isAdded) {
                try {
                    dialog.showNow(activity.supportFragmentManager, TAG)
                } catch (t: Throwable) {
                    Log.w(TAG, "show progress exception", t)
                }
            }
        }

        fun dismiss(activity: Activity?) {
            activity ?: return

            progressMap[activity]?.dismissSafely()
        }

        fun ProgressFragment.dismissSafely() {
            try {
                this.dismiss()
            } catch (e: Exception) {
                this.dismissAllowingStateLoss()
            }
        }
    }
}