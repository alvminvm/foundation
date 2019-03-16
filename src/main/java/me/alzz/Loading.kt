package me.alzz

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.ref.WeakReference


/**
 * Created by JeremyHe on 2018/4/5.
 */
class Loading {
    companion object {
        private val loadingMap = mutableMapOf<String, WeakReference<View>>()

        private lateinit var view: (ctx: Context)->View
        private lateinit var show: (v: View)->Unit
        private lateinit var hide: (v: View)->Unit

        private fun findView(activity: Activity): View? {
            return loadingMap[activity.toString()]?.get()
        }

        private fun cacheView(activity: Activity, view: View) {
            loadingMap[activity.toString()] = WeakReference(view)
        }

        fun init(view: (ctx: Context) -> View, show: (v: View) -> Unit, hide: (v: View)->Unit) {
            Companion.view = view
            Companion.show = show
            Companion.hide = hide
        }

        fun show(activity: Activity?) {
            activity ?: return

            // activity根部的ViewGroup，其实是一个FrameLayout
            val rootContainer = activity.findViewById(android.R.id.content) as FrameLayout
            var view = findView(activity)

            if (view == null) {
                val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                view = Companion.view.invoke(activity)
                rootContainer.addView(view, lp)
                cacheView(activity, view)
            }

            show.invoke(view)
        }

        fun hide(activity: Activity?) {
            activity ?: return

            val view = findView(activity)
            view ?: return

            hide.invoke(view)
        }
    }
}