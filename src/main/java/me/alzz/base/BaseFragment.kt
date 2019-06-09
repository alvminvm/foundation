package me.alzz.base

import android.os.Bundle
import android.os.Handler
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_title.*

/**
 * Created by JeremyHe on 2018/3/31.
 */
open class BaseFragment: Fragment() {

    private val h = Handler()

    init { arguments = Bundle() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar?.let {
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    fun setTitle(@StringRes titleId: Int) {
        val title = getString(titleId)
        setTitle(title)
    }

    fun setTitle(title: String) {
        titleTv?.text = title
    }

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