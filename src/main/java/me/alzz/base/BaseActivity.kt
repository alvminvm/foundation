package me.alzz.base

import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Created by JeremyHe on 2018/3/25.
 */
open class BaseActivity : AppCompatActivity() {

    protected var toolbar: Toolbar? = null
    private var titleTv: TextView? = null

    override fun setContentView(view: View) {
        super.setContentView(view)

        toolbar = toolbar ?: view.findViewById(R.id.toolbar)
        toolbar?.let {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(it.title.isNotEmpty())
        }

        titleTv = titleTv ?: view.findViewById(R.id.titleTv)
    }

    override fun setTitle(title: CharSequence?) {
        if (titleTv == null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            super.setTitle(title)
        } else {
            super.setTitle("")
            titleTv?.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}