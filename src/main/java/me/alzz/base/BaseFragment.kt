package me.alzz.base

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

/**
 * Created by JeremyHe on 2018/3/31.
 */
open class BaseFragment: Fragment() {

    private val h = Handler()

    var title: String? = null
        set(value) {
            field = value
            afterAdd { titleTv?.text = title }
        }

    init { arguments = Bundle() }

    private var toolbar: Toolbar? = null
    private var titleTv: TextView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar = toolbar ?: view?.findViewById(R.id.toolbar)
        titleTv = titleTv ?: view?.findViewById(R.id.titleTv)
        toolbar?.let {
            (activity as? AppCompatActivity)?.setSupportActionBar(it)
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    fun setTitle(@StringRes titleId: Int) {
        title = getString(titleId)
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