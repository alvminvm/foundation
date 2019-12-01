package me.alzz.base

import android.os.Bundle
import android.os.Handler
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_title.*

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar?.let {
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
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