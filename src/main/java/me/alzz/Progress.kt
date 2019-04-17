package me.alzz

import android.app.Activity
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import me.alzz.base.R

class Progress {
    companion object {
        private const val TAG = "Progress"
        private val progressMap = mutableMapOf<Activity, ProgressFragment>()

        fun show(activity: AppCompatActivity?, message: String) {
            activity ?: return

            val dialog = progressMap[activity] ?: ProgressFragment().apply {
                progressMap[activity] = this
                activity.lifecycle.addObserver(GenericLifecycleObserver { _, event ->
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        progressMap[activity]?.dismiss()
                        progressMap.remove(activity)
                    }
                })
            }

            dialog.setMessage(message)
            dialog.show(activity.supportFragmentManager, TAG)
        }

        fun dismiss(activity: Activity?) {
            activity ?: return

            progressMap[activity]?.dismiss()
        }
    }
}