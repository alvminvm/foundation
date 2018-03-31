package me.alzz.base

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_title.*

/**
 * Created by JeremyHe on 2018/3/25.
 */
open class BaseActivity: AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        toolbar?.let {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun setTitle(title: CharSequence?) {
        if (toolbar == null) {
            super.setTitle(title)
        } else {
            titleTv.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }
}