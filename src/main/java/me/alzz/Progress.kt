package me.alzz

import android.app.Activity
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.support.v7.app.AppCompatActivity

class Progress {
    companion object {
        private const val TAG = "Progress"
        private val progressMap = mutableMapOf<Activity, ProgressFragment>()

        fun show(activity: AppCompatActivity?, message: String) {
            activity ?: return

            val dialog = progressMap[activity]
                ?: (activity.supportFragmentManager.findFragmentByTag(TAG) as? ProgressFragment
                ?: ProgressFragment()).apply {
                    progressMap[activity] = this
                    activity.lifecycle.addObserver(GenericLifecycleObserver { _, event ->
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            progressMap[activity]?.dismissAllowingStateLoss()
                            progressMap.remove(activity)
                        }
                    })
            }

            dialog.setMessage(message)
            if (!dialog.isAdded) {
                dialog.show(activity.supportFragmentManager, TAG)
            }
        }

        fun dismiss(activity: Activity?) {
            activity ?: return

            progressMap[activity]?.dismissAllowingStateLoss()
        }
    }
}