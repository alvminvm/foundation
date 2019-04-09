package me.alzz.base

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_title.*

/**
 * Created by JeremyHe on 2018/3/25.
 */
open class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        toolbar?.let {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(toolbar.title.isNotEmpty())
        }
    }

    override fun setTitle(title: CharSequence?) {
        if (titleTv == null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            super.setTitle(title)
        } else {
            super.setTitle("")
            titleTv.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    fun setTitleGravity(gravity: Int) {
        titleTv?.gravity = gravity
    }

}